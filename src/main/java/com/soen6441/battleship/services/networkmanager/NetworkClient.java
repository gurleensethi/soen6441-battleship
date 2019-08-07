package com.soen6441.battleship.services.networkmanager;

import com.corundumstudio.socketio.SocketIOClient;
import com.soen6441.battleship.data.repository.PlayerConnected;
import com.soen6441.battleship.services.NetworkPlayer;
import io.socket.client.IO;
import io.socket.client.Socket;


import java.util.logging.Logger;

public class NetworkClient {
    private static NetworkClient sInstance;
    private static final Logger logger = Logger.getLogger(NetworkClient.class.getName());
    private String playerName;
    private String displayName;

    public static NetworkClient getInstance() {
        if (sInstance == null) {
            sInstance = new NetworkClient();
        }
        return sInstance;
    }

    private NetworkClient() {

    }

    public void init(String playerName, String displayName) {
        this.playerName = playerName;
        this.displayName = displayName;
        try {
            Socket ioSocket = IO.socket("http://localhost:5000");
            addListeners(ioSocket);
            ioSocket.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void addListeners(Socket ioSocket) {
        ioSocket.on(io.socket.client.Socket.EVENT_CONNECT, args -> {
            logger.info("Connection established, sending event to server...");
            ioSocket.emit("chatevent", "test");
            ioSocket.emit(NetworkEvent.PLAYER_CONNECTED, playerName);
        }).on(io.socket.client.Socket.EVENT_DISCONNECT, objects -> {
            logger.info("Client disconnected...");
        });
    }
}
