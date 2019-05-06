package com.blazingkin.interpreter.variables;

import java.net.ServerSocket;
import java.net.Socket;

public class BLZSocket {

    public Socket socket;
    public ServerSocket serverSocket;
    public SocketType type;
    public BLZSocket(Socket sock){
        this.socket = sock;
        type = SocketType.Client;
    }

    public BLZSocket(ServerSocket servSock){
        this.serverSocket = servSock;
        type = SocketType.Server;
    }

    public enum SocketType {
        Server,
        Client
    }


}