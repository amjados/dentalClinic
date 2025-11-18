import React, { useState } from 'react';
import { useMatrix } from '../../context/MatrixContext';

export const MessageInput: React.FC = () => {
    const [message, setMessage] = useState('');
    const [isSending, setIsSending] = useState(false);
    const { sendMessage, currentRoom } = useMatrix();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        if (!message.trim() || !currentRoom || isSending) {
            return;
        }

        setIsSending(true);
        const success = await sendMessage(message.trim());

        if (success) {
            setMessage('');
        } else {
            alert('Failed to send message. Please try again.');
        }

        setIsSending(false);
    };

    const handleKeyPress = (e: React.KeyboardEvent<HTMLTextAreaElement>) => {
        if (e.key === 'Enter' && !e.shiftKey) {
            e.preventDefault();
            handleSubmit(e);
        }
    };

    return (
        <form onSubmit={handleSubmit} className="message-input border-t border-gray-200 p-4 bg-white">
            <div className="flex gap-2">
                <textarea
                    value={message}
                    onChange={(e) => setMessage(e.target.value)}
                    onKeyPress={handleKeyPress}
                    placeholder={currentRoom ? 'Type a message...' : 'Select a room to start chatting'}
                    disabled={!currentRoom || isSending}
                    rows={1}
                    className="
            flex-1 px-4 py-2 border border-gray-300 rounded-lg 
            focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent
            disabled:bg-gray-100 disabled:cursor-not-allowed
            resize-none
          "
                />
                <button
                    type="submit"
                    disabled={!message.trim() || !currentRoom || isSending}
                    className="
            px-6 py-2 bg-blue-500 text-white rounded-lg font-medium
            hover:bg-blue-600 transition-colors
            disabled:bg-gray-300 disabled:cursor-not-allowed
            focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2
          "
                >
                    {isSending ? 'Sending...' : 'Send'}
                </button>
            </div>
            <div className="text-xs text-gray-500 mt-2">
                Press Enter to send, Shift+Enter for new line
            </div>
        </form>
    );
};
