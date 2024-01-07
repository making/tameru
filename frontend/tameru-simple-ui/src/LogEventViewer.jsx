import React, {useState} from 'react';
import {JSONToHTMLTable} from "@kevincobain2000/json-to-html-table";
import './LogEventViewer.css';

const LogEventViewer = () => {
    const [logs, setLogs] = useState([]);
    const [query, setQuery] = useState('');
    const [filter, setFilter] = useState('');
    const [size, setSize] = useState(30);
    const [isLoading, setIsLoading] = useState(false);
    const [jsonToTable, setJsonToTable] = useState(false);

    const fetchLogs = async () => {
        let url = `/?size=${size}&query=${encodeURIComponent(query)}`;
        if (filter) {
            url += `&filter=${encodeURIComponent(filter)}`;
        }
        setIsLoading(true);
        try {
            const response = await fetch(url);
            const data = await response.json();
            setLogs(data);
        } catch (error) {
            console.error('Error fetching logs:', error);
        } finally {
            setIsLoading(false);
        }
    };

    const handleKeyDown = (e) => {
        if (e.key === 'Enter') {
            fetchLogs();
        }
    };

    const shouldJsonToTable = log => jsonToTable && log.message && log.message.startsWith('{') && log.message.endsWith('}');

    return (<div>
        {isLoading && <div className="overlay">Loading...</div>}

        <input type="text"
               placeholder="Search Query"
               onChange={(e) => setQuery(e.target.value)}
               onKeyDown={handleKeyDown}
               disabled={isLoading}
               style={{width: '200px'}}
        />&nbsp;
        <input type="text" placeholder="Metadata Filter (e.g. level=='error')"
               onChange={(e) => setFilter(e.target.value)}
               onKeyDown={handleKeyDown}
               disabled={isLoading}
               style={{width: '400px'}}
        />&nbsp;
        <input type="number"
               min="1"
               placeholder="Size"
               onChange={(e) => setSize(Number(e.target.value))}
               onKeyDown={handleKeyDown}
               disabled={isLoading}
               style={{width: '50px'}}
        />&nbsp;

        <label>
            <input
                type="checkbox"
                checked={jsonToTable}
                onChange={(e) => setJsonToTable(e.target.checked)}
                disabled={isLoading}
            />
            json to table
        </label>&nbsp;
        <button onClick={fetchLogs}
                disabled={isLoading}
        >View Logs
        </button>

        <table className="table">
            <thead>
            <tr>
                <th>Event ID</th>
                <th>Timestamp</th>
                <th>Message</th>
                <th>Metadata</th>
            </tr>
            </thead>
            <tbody>
            {logs.map(log => <tr key={log.eventId}>
                <td>{log.eventId}</td>
                <td>{log.timestamp}</td>
                <td>{shouldJsonToTable(log) ?
                    <JSONToHTMLTable data={JSON.parse(log.message)} tableClassName="table"/> : log.message}</td>
                <td>
                    <JSONToHTMLTable
                        data={log.metadata || []}
                        tableClassName="table"
                    />
                </td>
            </tr>)}
            </tbody>
        </table>
    </div>);
};

export default LogEventViewer;
