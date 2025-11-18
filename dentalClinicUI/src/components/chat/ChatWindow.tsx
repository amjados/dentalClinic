import React from 'react';
import { useMatrix } from '../../context/MatrixContext';
import { ChatRoomList } from './ChatRoomList';
import { MessageList } from './MessageList';
import { MessageInput } from './MessageInput';

export const ChatWindow: React.FC = () => {
    const { user, currentRoom, isInitialized, logout } = useMatrix();

    if (!user) {
        return (
            <div className="flex items-center justify-center h-screen bg-gray-100">
                <div className="text-center">
                    <h2 className="text-2xl font-bold mb-4">Matrix Chat</h2>
                    <p className="text-gray-600">Please log in to access chat</p>
                </div>
            </div>
        );
    }

    if (!isInitialized) {
        return (
            <div className="flex items-center justify-center h-screen bg-gray-100">
                <div className="text-center">
                    <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500 mx-auto mb-4"></div>
                    <p className="text-gray-600">Connecting to Matrix server...</p>
                </div>
            </div>
        );
    }

    return (
        <div className="chat-window flex h-screen bg-white">
            {/* Sidebar - Room List */}
            <div className="w-80 border-r border-gray-200 flex flex-col">
                <div className="p-4 border-b border-gray-200 bg-blue-500 text-white">
                    <div className="flex justify-between items-center">
                        <div>
                            <h2 className="font-bold text-lg">Matrix Chat</h2>
                            <p className="text-sm text-blue-100">{user.displayName || user.userId}</p>
                        </div>
                        <button
                            onClick={logout}
                            className="px-3 py-1 bg-blue-600 hover:bg-blue-700 rounded text-sm transition-colors"
                        >
                            Logout
                        </button>
                    </div>
                </div>
                <div className="flex-1 overflow-y-auto">
                    <ChatRoomList />
                </div>
            </div>

            {/* Main Chat Area */}
            <div className="flex-1 flex flex-col">
                {currentRoom ? (
                    <>
                        <div className="p-4 border-b border-gray-200 bg-white">
                            <h3 className="font-semibold text-lg">Chat Room</h3>
                            <p className="text-sm text-gray-500">{currentRoom}</p>
                        </div>
                        <MessageList />
                        <MessageInput />
                    </>
                ) : (
                    <div className="flex-1 flex items-center justify-center text-gray-500">
                        <div className="text-center">
                            <svg
                                className="w-16 h-16 mx-auto mb-4 text-gray-400"
                                fill="none"
                                stroke="currentColor"
                                viewBox="0 0 24 24"
                            >
                                <path
                                    strokeLinecap="round"
                                    strokeLinejoin="round"
                                    strokeWidth={2}
                                    d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z"
                                />
                            </svg>
                            <p className="text-lg font-medium">Select a room to start chatting</p>
                            <p className="text-sm mt-2">Choose a conversation from the list</p>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
};
