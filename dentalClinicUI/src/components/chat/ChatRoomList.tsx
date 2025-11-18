import React from 'react';
import { useMatrix } from '../../context/MatrixContext';
import type { MatrixRoom } from '../../utils/matrixService';

export const ChatRoomList: React.FC = () => {
    const { rooms, currentRoom, selectRoom } = useMatrix();

    return (
        <div className="chat-room-list">
            <h3 className="text-lg font-bold mb-4 px-4">Chat Rooms</h3>
            <div className="space-y-2">
                {rooms.length === 0 ? (
                    <div className="px-4 py-8 text-center text-gray-500">
                        No active conversations
                    </div>
                ) : (
                    rooms.map((room: MatrixRoom) => (
                        <div
                            key={room.roomId}
                            onClick={() => selectRoom(room.roomId)}
                            className={`
                px-4 py-3 cursor-pointer hover:bg-gray-100 transition-colors
                ${currentRoom === room.roomId ? 'bg-blue-50 border-l-4 border-blue-500' : ''}
              `}
                        >
                            <div className="flex justify-between items-start">
                                <div className="flex-1 min-w-0">
                                    <h4 className="font-semibold text-sm truncate">
                                        {room.name || 'Unnamed Room'}
                                    </h4>
                                    {room.topic && (
                                        <p className="text-xs text-gray-600 truncate">{room.topic}</p>
                                    )}
                                    {room.lastMessage && (
                                        <p className="text-xs text-gray-500 truncate mt-1">
                                            {room.lastMessage}
                                        </p>
                                    )}
                                </div>
                                {room.unreadCount && room.unreadCount > 0 && (
                                    <span className="ml-2 bg-blue-500 text-white text-xs rounded-full px-2 py-1">
                                        {room.unreadCount}
                                    </span>
                                )}
                            </div>
                        </div>
                    ))
                )}
            </div>
        </div>
    );
};
