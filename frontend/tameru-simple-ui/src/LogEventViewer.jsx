import React, {useState} from 'react';
import {JSONToHTMLTable} from "@kevincobain2000/json-to-html-table";
import ScrollToTop from "react-scroll-to-top";
import './LogEventViewer.css';
import logfmt from 'logfmt';

const buildUrl = ({size, query, filter, cursor}) => {
    let url = `/?size=${size}&query=${encodeURIComponent(query)}`;
    if (filter) {
        url += `&filter=${encodeURIComponent(filter)}`;
    }
    if (cursor) {
        url += `&cursor=${encodeURIComponent(cursor)}`;
    }
    return url;
};


const LogEventViewer = () => {
    const [logs, setLogs] = useState([]);
    const [query, setQuery] = useState('');
    const [filter, setFilter] = useState('');
    const [size, setSize] = useState(30);
    const [isLoading, setIsLoading] = useState(false);
    const [jsonToTable, setJsonToTable] = useState(true);
    const [showLoadMore, setShowLoadMore] = useState(false);

    const fetchLogs = async () => {
        let url = buildUrl({size, query, filter})
        setIsLoading(true);
        try {
            const response = await fetch(url);
            const data = await response.json();
            setLogs(data);
            setShowLoadMore(data.length >= size);
        } catch (error) {
            console.error('Error fetching logs:', error);
        } finally {
            setIsLoading(false);
        }
    };

    const fetchMoreLogs = async () => {
        if (logs.length === 0) {
            return;
        }
        var lastLog = logs[logs.length - 1];
        const url = buildUrl({size, query, filter, cursor: `${lastLog.timestamp},${lastLog.eventId}`});
        setIsLoading(true);
        try {
            const response = await fetch(url);
            const moreLogs = await response.json();
            setLogs([...logs, ...moreLogs]);
            setShowLoadMore(moreLogs.length >= size);
        } catch (error) {
            console.error('Error fetching more logs:', error);
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
    const shouldLogfmtToTable = log => jsonToTable && log.message && /^[a-zA-Z0-9_]+=/.test(log.message);

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
                <td>{shouldJsonToTable(log) ? <JSONToHTMLTable data={JSON.parse(log.message)}
                                                               tableClassName="table"/> : (shouldLogfmtToTable(log) ?
                    <JSONToHTMLTable data={logfmt.parse(log.message)}
                                     tableClassName="table"/> : log.message)}</td>
                <td>
                    <JSONToHTMLTable
                        data={log.metadata || []}
                        tableClassName="table"
                    />
                </td>
            </tr>)}
            </tbody>
        </table>
        {showLoadMore && <button onClick={fetchMoreLogs}
                                 disabled={isLoading}>Load More</button>}
        <ScrollToTop smooth style={{paddingLeft: '5px'}}/>
    </div>);
};

export default LogEventViewer;
