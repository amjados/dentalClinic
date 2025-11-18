// src/utils/env.ts
// Utility to access environment variables safely

export const API_URL = import.meta.env.VITE_API_URL as string;
export const LOGIN_API_URL = import.meta.env.VITE_LOGIN_API_URL as string;

// Add more env exports as needed
export function getEnv(name: string, fallback = ''): string {
    const vite = (typeof import.meta !== 'undefined' && (import.meta as any).env) || {};
    return vite?.[name] ?? process.env[name] ?? fallback;
}
