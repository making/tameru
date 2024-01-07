import {useState} from 'react'
import './App.css'
import LogEventViewer from "./LogEventViewer.jsx";

function App() {
    const [count, setCount] = useState(0)

    return (
        <>
            <h1><a href={'/'}>Tameru Log Viewer</a></h1>
            <LogEventViewer/>
        </>
    )
}

export default App
