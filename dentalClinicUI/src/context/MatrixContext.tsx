import React, { createContext, useContext, useState, useEffect } from 'react';
import type { ReactNode } from 'react';
import { matrixService } from '../utils/matrixService';
import type { MatrixUser, MatrixRoom, MatrixMessage } from '../utils/matrixService';

interface MatrixContextType {
  user: MatrixUser | null;
  rooms: MatrixRoom[];
  currentRoom: string | null;
  messages: MatrixMessage[];
  isInitialized: boolean;
  login: (username: string, password: string) => Promise<boolean>;
  logout: () => void;
  selectRoom: (roomId: string) => void;
  sendMessage: (message: string) => Promise<boolean>;
  createRoom: (patientId: number, employeeId: number) => Promise<string | null>;
  refreshRooms: () => void;
}

const MatrixContext = createContext<MatrixContextType | undefined>(undefined);

export const useMatrix = () => {
  const context = useContext(MatrixContext);
  if (!context) {
    throw new Error('useMatrix must be used within a MatrixProvider');
  }
  return context;
};

interface MatrixProviderProps {
  children: ReactNode;
}

export const MatrixProvider: React.FC<MatrixProviderProps> = ({ children }) => {
  const [user, setUser] = useState<MatrixUser | null>(null);
  const [rooms, setRooms] = useState<MatrixRoom[]>([]);
  const [currentRoom, setCurrentRoom] = useState<string | null>(null);
  const [messages, setMessages] = useState<MatrixMessage[]>([]);
  const [isInitialized, setIsInitialized] = useState(false);

  // Initialize Matrix client when user logs in
  useEffect(() => {
    if (user) {
      initializeClient();
    }
  }, [user]);

  // Load messages when room changes
  useEffect(() => {
    if (currentRoom && isInitialized) {
      loadRoomMessages(currentRoom);
    }
  }, [currentRoom, isInitialized]);

  const initializeClient = async () => {
    if (!user) return;

    const success = await matrixService.initializeClient(user.userId, user.accessToken);
    if (success) {
      setIsInitialized(true);
      refreshRooms();
      
      // Listen for new messages
      matrixService.onMessage((roomId, message) => {
        if (roomId === currentRoom) {
          setMessages((prev) => [...prev, message]);
        }
        // Update room list to show new message notification
        refreshRooms();
      });
    }
  };

  const login = async (username: string, password: string): Promise<boolean> => {
    const matrixUser = await matrixService.login(username, password);
    if (matrixUser) {
      setUser(matrixUser);
      // Store user in localStorage for persistence
      localStorage.setItem('matrixUser', JSON.stringify(matrixUser));
      return true;
    }
    return false;
  };

  const logout = () => {
    matrixService.stopClient();
    setUser(null);
    setRooms([]);
    setCurrentRoom(null);
    setMessages([]);
    setIsInitialized(false);
    localStorage.removeItem('matrixUser');
  };

  const refreshRooms = () => {
    const userRooms = matrixService.getRooms();
    setRooms(userRooms);
  };

  const selectRoom = (roomId: string) => {
    setCurrentRoom(roomId);
    setMessages([]); // Clear messages while loading
  };

  const loadRoomMessages = async (roomId: string) => {
    const roomMessages = await matrixService.getRoomMessages(roomId, 50);
    setMessages(roomMessages);
  };

  const sendMessage = async (message: string): Promise<boolean> => {
    if (!currentRoom) return false;
    
    const success = await matrixService.sendMessage(currentRoom, message);
    if (success) {
      // Message will be added via the event listener
      return true;
    }
    return false;
  };

  const createRoom = async (patientId: number, employeeId: number): Promise<string | null> => {
    const roomId = await matrixService.createRoom(patientId, employeeId);
    if (roomId) {
      refreshRooms();
      return roomId;
    }
    return null;
  };

  // Restore session from localStorage on mount
  useEffect(() => {
    const storedUser = localStorage.getItem('matrixUser');
    if (storedUser) {
      try {
        const parsedUser = JSON.parse(storedUser);
        setUser(parsedUser);
      } catch (error) {
        console.error('Failed to restore session:', error);
        localStorage.removeItem('matrixUser');
      }
    }
  }, []);

  const value: MatrixContextType = {
    user,
    rooms,
    currentRoom,
    messages,
    isInitialized,
    login,
    logout,
    selectRoom,
    sendMessage,
    createRoom,
    refreshRooms,
  };

  return <MatrixContext.Provider value={value}>{children}</MatrixContext.Provider>;
};
