import { createClient } from 'redis';
import { WebSocketServer, WebSocket } from 'ws';

const client = createClient({
    url: 'redis://10.0.0.177'
});

await client.connect()

client.on('error', err => console.log('Redis Client Error', err));

const wss = new WebSocketServer({ port: 8080 });

wss.on('connection', function connection(ws) {
    ws.on('message', async function message(data) {
        data = JSON.parse(data);
        switch (data.action) {
            case 'get':
                console.log('get', data.boardId);
                const value = await client.get(data.boardId);
                const response1 = { boardId: data.boardId, boardData: value };
                ws.send(JSON.stringify(response1));
                break;
            case 'set':
                await client.set(data.boardId, data.boardData);
                console.log('set', data.boardId );
                wss.clients.forEach(function each(client) {
                    if (client.readyState === WebSocket.OPEN) {
                        const response2 = { boardId: data.boardId, boardData: data.boardData };
                        client.send(JSON.stringify(response2));
                    }
                }
            );
                break;
        }
    });
});
    
process.on('SIGINT', async () => {
    console.log('Closing WebSocket server...');
    await new Promise((resolve) => wss.close(resolve));
    console.log('Closing Redis client...');
    client.quit(() => {
        console.log('Redis client closed.');
        process.exit(0);
    });
});
