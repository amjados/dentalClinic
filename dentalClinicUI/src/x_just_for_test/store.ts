// store.ts
import { configureStore, createSlice } from '@reduxjs/toolkit';
import type { PayloadAction } from '@reduxjs/toolkit';


const counterSlice = createSlice({
    name: 'counter',
    initialState: { value: 0, empName: 'Amjad' },
    reducers: {
        increment: (statObj) => { statObj.value += 1; statObj.empName = 'Ali-' + statObj.value; },
        addBy: (statObj, action: PayloadAction<{ value: number; name: string }>) => {
            statObj.value += action.payload.value;
            statObj.empName = 'Moh:' + action.payload.name;
        },
    },
});

const userSlice = createSlice({
    name: 'user',
    initialState: { username: 'Amjad', age: 30 },
    reducers: {
        dcStoreSetUsername: (s, a: PayloadAction<string>) => { s.username = a.payload; },
        dcStoreSetAge: (s, a: PayloadAction<number>) => { s.age = a.payload; },
    },
});

export const { increment, addBy } = counterSlice.actions;
export const { dcStoreSetUsername, dcStoreSetAge } = userSlice.actions;

export const dcStore = configureStore({ reducer: { counter: counterSlice.reducer, user: userSlice.reducer } });
export type RootState = ReturnType<typeof dcStore.getState>;
export type AppDispatch = typeof dcStore.dispatch;
