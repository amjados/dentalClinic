import * as sdk from 'matrix-js-sdk';

const HOMESERVER_URL = import.meta.env.VITE_MATRIX_HOMESERVER_URL || 'https://matrix.org';
const BACKEND_API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8082';

export interface MatrixUser {
    userId: string;
    displayName?: string;
    accessToken: string;
}

export interface MatrixRoom {
    roomId: string;
    name?: string;
    topic?: string;
    lastMessage?: string;
    unreadCount?: number;
}

export interface MatrixMessage {
    eventId: string;
    sender: string;
    message: string;
    timestamp: Date;
    type: string;
}

/**
 * Service for Matrix client operations
 */
class MatrixClientService {
    private client: any = null;
    private isInitialized = false;

    /**
     * Initialize Matrix client with credentials
     */
    async initializeClient(userId: string, accessToken: string): Promise<boolean> {
        try {
            this.client = sdk.createClient({
                baseUrl: HOMESERVER_URL,
                accessToken: accessToken,
                userId: userId,
            });

            await this.client.startClient({ initialSyncLimit: 10 });
            this.isInitialized = true;
            console.log('Matrix client initialized successfully');
            return true;
        } catch (error) {
            console.error('Failed to initialize Matrix client:', error);
            return false;
        }
    }

    /**
     * Login to Matrix homeserver
     */
    async login(username: string, password: string): Promise<MatrixUser | null> {
        try {
            const tempClient = sdk.createClient({ baseUrl: HOMESERVER_URL });
            const response = await tempClient.login('m.login.password', {
                user: username,
                password: password,
            });

            return {
                userId: response.user_id,
                accessToken: response.access_token,
                displayName: username,
            };
        } catch (error) {
            console.error('Login failed:', error);
            return null;
        }
    }

    /**
     * Create a new room via backend API
     */
    async createRoom(patientId: number, employeeId: number): Promise<string | null> {
        try {
            const response = await fetch(
                `${BACKEND_API_URL}/matrix/rooms/patient/${patientId}/employee/${employeeId}`,
                {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                }
            );

            if (response.ok) {
                const data = await response.json();
                return data.roomId;
            }
            return null;
        } catch (error) {
            console.error('Failed to create room:', error);
            return null;
        }
    }

    /**
     * Send a message to a room
     */
    async sendMessage(roomId: string, message: string): Promise<boolean> {
        if (!this.client || !this.isInitialized) {
            console.error('Matrix client not initialized');
            return false;
        }

        try {
            await this.client.sendTextMessage(roomId, message);
            return true;
        } catch (error) {
            console.error('Failed to send message:', error);
            return false;
        }
    }

    /**
     * Get rooms for current user
     */
    getRooms(): MatrixRoom[] {
        if (!this.client || !this.isInitialized) {
            return [];
        }

        const rooms = this.client.getRooms();
        return rooms.map((room: any) => ({
            roomId: room.roomId,
            name: room.name || 'Unnamed Room',
            topic: room.currentState.getStateEvents('m.room.topic', '')?.getContent().topic,
            unreadCount: room.getUnreadNotificationCount(),
        }));
    }

    /**
     * Get messages from a room
     */
    async getRoomMessages(roomId: string, limit: number = 50): Promise<MatrixMessage[]> {
        if (!this.client || !this.isInitialized) {
            return [];
        }

        try {
            const room = this.client.getRoom(roomId);
            if (!room) return [];

            const timeline = room.timeline;
            const messages: MatrixMessage[] = [];

            for (let i = Math.max(0, timeline.length - limit); i < timeline.length; i++) {
                const event = timeline[i];
                if (event.getType() === 'm.room.message') {
                    messages.push({
                        eventId: event.getId() || '',
                        sender: event.getSender() || '',
                        message: event.getContent().body || '',
                        timestamp: new Date(event.getTs()),
                        type: event.getContent().msgtype || 'm.text',
                    });
                }
            }

            return messages;
        } catch (error) {
            console.error('Failed to get room messages:', error);
            return [];
        }
    }

    /**
     * Listen for new messages
     */
    onMessage(callback: (roomId: string, message: MatrixMessage) => void): void {
        if (!this.client || !this.isInitialized) {
            return;
        }

        this.client.on('Room.timeline', (event: any, room: any) => {
            if (event.getType() === 'm.room.message') {
                callback(room.roomId, {
                    eventId: event.getId() || '',
                    sender: event.getSender() || '',
                    message: event.getContent().body || '',
                    timestamp: new Date(event.getTs()),
                    type: event.getContent().msgtype || 'm.text',
                });
            }
        });
    }

    /**
     * Join a room
     */
    async joinRoom(roomId: string): Promise<boolean> {
        if (!this.client || !this.isInitialized) {
            return false;
        }

        try {
            await this.client.joinRoom(roomId);
            return true;
        } catch (error) {
            console.error('Failed to join room:', error);
            return false;
        }
    }

    /**
     * Leave a room
     */
    async leaveRoom(roomId: string): Promise<boolean> {
        if (!this.client || !this.isInitialized) {
            return false;
        }

        try {
            await this.client.leave(roomId);
            return true;
        } catch (error) {
            console.error('Failed to leave room:', error);
            return false;
        }
    }

    /**
     * Stop the Matrix client
     */
    stopClient(): void {
        if (this.client) {
            this.client.stopClient();
            this.isInitialized = false;
        }
    }

    /**
     * Check if client is initialized
     */
    isClientInitialized(): boolean {
        return this.isInitialized;
    }
}

export const matrixService = new MatrixClientService();
