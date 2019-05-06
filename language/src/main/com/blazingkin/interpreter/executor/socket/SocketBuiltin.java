package com.blazingkin.interpreter.executor.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorCommaDelimited;
import com.blazingkin.interpreter.variables.BLZResource;
import com.blazingkin.interpreter.variables.BLZSocket;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;
import com.blazingkin.interpreter.variables.VariableTypes;

public class SocketBuiltin implements InstructionExecutorCommaDelimited {

    public Value run(Value[] args) throws BLZRuntimeException{
        if (args.length == 0){
            throw new BLZRuntimeException("No arguments passed to socket builtin");
        }
        Value mode = args[0];
        if (mode.type != VariableTypes.String) {
            throw new BLZRuntimeException("First argument passed to socket builtin was not a mode");
        }
        String modeString = (String) args[0].value;
        if (modeString.equals("create")) {
            return handleCreate(args);
        } else if (modeString.equals("accept")) {
            return handleAccept(args);
        } else if (modeString.equals("get_resource")) {
            return handleResource(args);
        } else if (modeString.equals("close")) {
            return handleClose(args);
        }
        throw new BLZRuntimeException("Did not know how to handle mode "+modeString+" in socket built in");
    }

    // "create", host, port
    // If the host is "server", then create a server socket instead
    private Value handleCreate(Value[] args) throws BLZRuntimeException {
        if (args.length != 3) {
            throw new BLZRuntimeException("Socket Create expects three arguments, \"create\", host_name, and port");
        }
        if (args[1].type != VariableTypes.String || args[2].type != VariableTypes.Integer){
            throw new BLZRuntimeException("Socket Create recieved wrong argument types, expects host_name as string and port as integer");
        }
        String hostname = (String) args[1].value;
        int port = Variable.getIntValue(args[2]).intValue();
        try {
            if (hostname.equals("server")) { 
                ServerSocket sock = new ServerSocket(port);
                return Value.socket(new BLZSocket(sock));
            }
            Socket sock = new Socket(hostname, port);
            return Value.socket(new BLZSocket(sock));
        } catch (IOException e){
            throw new BLZRuntimeException("Unexpected socket IO exception "+e.getMessage());
        }
        
    }

    // "accept", sock
    private Value handleAccept(Value[] args) throws BLZRuntimeException {
        if (args.length != 2){
            throw new BLZRuntimeException("Socket Accept expects two arguments, \"accept\", socket");
        }
        if (args[1].type != VariableTypes.Socket) {
            throw new BLZRuntimeException("Socket Accept expects a socket object as the second type");
        }
        BLZSocket sock = (BLZSocket) args[1].value;
        if (sock.type != BLZSocket.SocketType.Server) {
            throw new BLZRuntimeException("Cannot accept with a client socket");
        }
        try {
            Socket s = sock.serverSocket.accept();
            return Value.socket(new BLZSocket(s));
        } catch (IOException e){
            throw new BLZRuntimeException("Unexpected socket IO exception "+e.getMessage());
        }
    }

    // "close", socket
    private Value handleClose(Value[] args) throws BLZRuntimeException {
        if (args.length != 2) {
            throw new BLZRuntimeException("Close Socket must have 2 arguments, \"close\", and the socket");
        }
        if (args[1].type != VariableTypes.Socket) {
            throw new BLZRuntimeException("Close Socket expects a socket");
        }
        BLZSocket sock = (BLZSocket) args[1].value;
        try {
            switch (sock.type) {
                case Client:
                    sock.socket.close();
                break;
                case Server:
                    sock.serverSocket.close();
                break;
            }
        } catch (IOException e) {
            throw new BLZRuntimeException("Unexpected socket IO exception "+e.getMessage());
        }
        return Value.nil();
    }

    private Value handleResource(Value[] args) throws BLZRuntimeException {
        if (args.length != 2) {
            throw new BLZRuntimeException("Resource From Socket must have 2 arguments, \"get_resource\", and the socket");
        }
        if (args[1].type != VariableTypes.Socket) {
            throw new BLZRuntimeException("Resource From Socket expects a socket");
        }
        BLZSocket sock = (BLZSocket) args[1].value;
        try {
            switch (sock.type) {
                case Client:
                BLZResource newResource = new BLZResource(sock.socket.getInputStream(), sock.socket.getOutputStream());
                    return Value.resource(newResource);
                case Server:
                    throw new BLZRuntimeException("Cannot get input / output streams for a sever socket. Instead get a client socket using accept");
            }
        } catch (IOException e) {
            throw new BLZRuntimeException("Unexpected socket IO exception "+e.getMessage());
        }
        return Value.nil();
    }

}