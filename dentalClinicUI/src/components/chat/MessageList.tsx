import React, { useEffect, useRef } from 'react';
import { useMatrix } from '../../context/MatrixContext';
import type { MatrixMessage } from '../../utils/matrixService';

export const MessageList: React.FC = () => {
    const { messages, user } = useMatrix();
    const messagesEndRef = useRef<HTMLDivElement>(null);

    // Auto-scroll to bottom when new messages arrive
    useEffect(() => {
        messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
    }, [messages]);

    const formatTime = (date: Date) => {
        return new Date(date).toLocaleTimeString('en-US', {
            hour: '2-digit',
            minute: '2-digit',
        });
    };

    return (
        <div className="message-list flex-1 overflow-y-auto p-4 space-y-4 bg-gray-50">
            {messages.length === 0 ? (
                <div className="flex items-center justify-center h-full text-gray-500">
                    No messages yet. Start the conversation!
                </div>
            ) : (
                messages.map((message: MatrixMessage) => {
                    const isOwnMessage = message.sender === user?.userId;

                    return (
                        <div
                            key={message.eventId}
                            className={`flex ${isOwnMessage ? 'justify-end' : 'justify-start'}`}
                        >
                            <div
                                className={`
                  max-w-[70%] rounded-lg px-4 py-2 shadow-sm
                  ${isOwnMessage
                                        ? 'bg-blue-500 text-white'
                                        : 'bg-white text-gray-800 border border-gray-200'
                                    }
                `}
                            >
                                {!isOwnMessage && (
                                    <div className="text-xs font-semibold mb-1 text-gray-600">
                                        {message.sender.split(':')[0].substring(1)}
                                    </div>
                                )}
                                <div className="text-sm break-words">{message.message}</div>
                                <div
                                    className={`text-xs mt-1 ${isOwnMessage ? 'text-blue-100' : 'text-gray-500'
                                        }`}
                                >
                                    {formatTime(message.timestamp)}
                                </div>
                            </div>
                        </div>
                    );
                })
            )}
            <div ref={messagesEndRef} />
        </div>
    );
};
