import { StrictMode, useEffect } from 'react'
import { createRoot } from 'react-dom/client'
import { createContext, useContext, useState } from 'react';
import React, { useMemo } from "react";

import { Provider, useDispatch, useSelector } from 'react-redux';
import { dcStore, increment, addBy, dcStoreSetUsername, dcStoreSetAge } from './store';

import { BrowserRouter, Routes, Route, Link } from 'react-router-dom';

import Home2 from './pages/Home2';
import About2 from './pages/About2';
import Contact2 from './pages/Contact2';

function Home() {
    return <h2>Home Page</h2>;
}
function About() {
    return <h2>About Page</h2>;
}
function Contact() {
    return <h2>Contact Page</h2>;
}

function Parent() {
    const [countP, setCount] = useState(10);
    const [other, setOther] = useState(0);

    const userMemo = useMemo(() => ({ id: 1, name: 'John', age: 25 }), []);

    return (
        <div>
            <button onClick={() => setOther(other + 1)}>Other: {other}</button>
            <button onClick={() => setCount(countP + 1)}>Count: {countP}</button>

            <Child countXc={countP} />  {/* Re-renders when other changes! */}

            <ChildMemo countXc={countP} />  {/* Re-renders when other changes! */}

            <ChildCustom userXc={userMemo} /> {/* Re-renders only when id changes */}

        </div>
    );
}

function Child({ countXc }: { countXc: number }) {
    console.log('Child rendered -1 ');
    return <div>{countXc} - 1</div>;
}

// ✅ Solution: Wrap with React.memo
const ChildMemo = React.memo(function Child({ countXc }: { countXc: number }) {
    console.log('Child rendered -2 ');
    return <div>{countXc} - 2</div>;
});


// Custom comparison function
const ChildCustom = React.memo(
    function Child({ userXc }: { userXc: { id: number; name: string } }) {
        return <div>{userXc.name}</div>;
    },
    (prevProps, nextProps) => {
        // Return true if same (don't re-render)
        // Return false if different (re-render)
        return prevProps.userXc.id === nextProps.userXc.id;
    }
);



function Counter() {
    const { value, empName } = useSelector((dcStore: any) => dcStore.counter);
    const { username, age } = useSelector((dcStore: any) => dcStore.user);


    const dispatch = useDispatch();
    return (
        <div>
            <p>{value}</p>, <p>{empName}</p>
            <button onClick={() => dispatch(increment())}>+1</button>
            <button onClick={() => dispatch(addBy({ value: age, name: username }))}>+{age}, {username}</button>
        </div>
    );
}



type ThemeCtx = { theme: "light" | "dark"; toggleTheme: () => void };

// Create context (no default)
const ThemeContext = createContext<ThemeCtx | null>(null);

function ThemeProvider({ children }: { children: React.ReactNode }) {

    const [theme, setTheme] = useState<"light" | "dark">("light");

    const toggleTheme = () => setTheme(prev => (prev === "light" ? "dark" : "light"));

    // Avoid re-renders of consumers when only functions re-create
    const value = useMemo(() => ({ theme, toggleTheme }), [theme]);

    return <ThemeContext.Provider value={value}>{children}</ThemeContext.Provider>;
}

function Header() {

    const ctx = useContext(ThemeContext);

    if (!ctx) return null; // or throw new Error("ThemeProvider missing");

    const { theme, toggleTheme } = ctx;

    const styles = {
        background: theme === "light" ? "#fff" : "#333",
        color: theme === "light" ? "#000" : "#fff",
        padding: "12px",
    };

    return (
        <header style={styles}>
            <span style={{ marginRight: 12 }}>Theme: {theme}</span>
            <button onClick={toggleTheme}>Toggle Theme</button>
        </header>
    );
}



function FormB({ initVal }: { initVal: { taskId: number, taskName: string } }) {
    const [testList, setTaskList] = useState([initVal]);

    useEffect(() => {
        setTaskList(prev => [...prev, { taskId: prev.length + 1, taskName: `useEffect-task ${prev.length + 1}` }]);
    }, []);

    // 4. Cleanup function (important!)
    useEffect(() => {
        //const timer = setInterval(() => {
        console.log('FormB-Tick Date:[ %s ]', new Date().toLocaleTimeString());
        //}, 2000);

        // ✅ Cleanup - runs when component unmounts
        return () => {
            //clearInterval(timer);
            console.log('FormB-on unmount Cleanup');
        };
    }, []);

    // ✅ CORRECT - Create async function inside
    useEffect(() => {
        const fetchData = async () => {
            try {
                const fetchURL = import.meta.env.VITE_API_URL;
                console.log('FormB-fetchURL:', fetchURL);

                const response = await fetch(fetchURL + '/api/data');
                const data = await response.json();
                console.log('Fetched data:', data);
            } catch (error) {
                console.error('FormB-fetchError:', error);
            }
        };

        fetchData();
    }, []);

    return (
        <div>
            <h1>Form B</h1>
            2 seconds interval to add task automatically
            <br />
            <button onClick={() => setTaskList(prev => [...prev, { taskId: prev.length + 1, taskName: `task ${prev.length + 1}` }])}>
                Add Task
            </button>
            <br />
            <ul>
                {testList.map(item => (
                    <li key={item.taskId}>{item.taskName}</li>
                ))}
            </ul>
        </div>
    );
}

function FormA() {

    const [taskList, setTaskList] = useState([{ taskId: 1, taskName: 'task 1' }]);

    return (
        <div>
            <h1>Task List</h1>
            <button onClick={() => setTaskList(prev => [...prev, { taskId: prev.length + 1, taskName: `task ${prev.length + 1}` }])}>
                Add Task
            </button>
            {taskList.map(taskIn => (
                <div key={taskIn.taskId}>
                    {taskIn.taskName}
                </div>
            ))}
        </div>
    );
}
export { FormA };

function AutoFillForm({ count = 10 }: { count?: number }) {
    const items = Array.from({ length: count }, (_, i) => i);
    return (
        <div>
            <h1>Form auto-filled!</h1>
            {count} items:
            <br />
            {items.map(i => <span key={i}>{i} {i < count - 1 ? ',' : ''} </span>)}
        </div>
    );
}

export { AutoFillForm };

// Child Component
function WelcomeMessage(user: { user: { name: string; age: number } }) {
    return <h2>Hello, {user.user.name} 👋, you are {user.user.age} years old.</h2>;
}

// Parent Component
function LoginStatus() {

    const dispatch = useDispatch();
    const username = useSelector((dcStore: any) => dcStore.user.username);
    const userAge = useSelector((dcStore: any) => dcStore.user.age);

    //const [username, setUsername] = useState('Amjad'); // <-- state
    //const [userAge, setAge] = useState(30); // <-- state

    useEffect(() => { console.log('username:', username); }, [username]);
    useEffect(() => { console.log('userAge:', userAge); }, [userAge]);

    return (
        <div style={{ textAlign: 'center', marginTop: 30 }}>
            {/* passing state as props */}
            <WelcomeMessage user={{ name: username, age: userAge }} />

            <input
                value={username}
                type="text"
                onChange={(e) => {
                    //setUsername(e.target.value);
                    dispatch(dcStoreSetUsername(e.target.value));
                }} // update state
                placeholder="Enter your name"
            />
            <input
                value={userAge}
                type="number"
                onChange={(e) => {
                    //setAge(Number(e.target.value) || 0);
                    dispatch(dcStoreSetAge(Number(e.target.value) || 0));

                }} // update state
                placeholder="Enter your age"
            />

            {/* Button toggles state */}
            <button onClick={() => dispatch(dcStoreSetAge(userAge + 1))}>Age++</button>
        </div>
    );
}

export { LoginStatus };



function UserStatus() {
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    return (
        <div style={{ textAlign: 'center', marginTop: '30px' }}>
            <h2>Welcome to My App</h2>

            {/* Conditional rendering using a ternary operator */}
            {isLoggedIn ? (
                <p>✅ You are logged in!</p>
            ) : (
                <p>❌ Please log in to continue.</p>
            )}

            {/* Button toggles state */}
            <button onClick={() => setIsLoggedIn(!isLoggedIn)}>
                {isLoggedIn ? 'Logout' : 'Login'}
            </button>
        </div>
    );
}

export default UserStatus;

createRoot(document.getElementById('test-root-2')!).render(
    <StrictMode>

        1st Router:
        <BrowserRouter>
            {/* Navigation Links */}
            <nav>
                <Link to="/test/test-index-2/2">Home-2</Link><br />
                <Link to="/test/test-index-2/about2">About-2</Link><br />
                <Link to="/test/test-index-2/contact2">Contact-2</Link><br />
            </nav>

            {/* Routes - which component to show */}
            <Routes>
                <Route path="/test/test-index-2/2" element={<Home2 />} />
                <Route path="/test/test-index-2/about2" element={<About2 />} />
                <Route path="/test/test-index-2/contact2" element={<Contact2 />} />
            </Routes>
        </BrowserRouter>
        <br />

        2nd Router:

        <BrowserRouter>
            {/* Navigation Links */}
            <nav>
                <Link to="/test/test-index-2">Home-1</Link><br />
                <Link to="/test/test-index-2/about">About-2</Link><br />
                <Link to="/test/test-index-2/contact">Contact-3</Link><br />
            </nav>

            {/* Routes - which component to show */}
            <Routes>
                <Route path="/test/test-index-2" element={<Home />} />
                <Route path="/test/test-index-2/about" element={<About />} />
                <Route path="/test/test-index-2/contact" element={<Contact />} />
            </Routes>
        </BrowserRouter>

        <br />

        <Parent />

        <UserStatus />
        <Provider store={dcStore}>
            <LoginStatus />
        </Provider>
        <FormA />
        <AutoFillForm count={15} />
        <FormB initVal={{ taskId: 1000, taskName: 'task 1000' }} />

        <ThemeProvider>
            <Header />
            {/* other components that need theme go here */}
        </ThemeProvider>


        <Provider store={dcStore}>
            <Counter />
        </Provider>

    </StrictMode>,
)
