package com.blazingkin.interpreter.variables;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URLConnection;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.BufferedInputStream;



public class BLZResource {

    private URI location;
    private InputStream scanner;
    private BufferedWriter writer;
    private ResourceType type;
    private boolean canRead = false;

    public BLZResource(URI location){
        this.location = location;
        try {
            if (location.toURL().getProtocol().equals("file")){
                type = ResourceType.File;
            }else{
                type = ResourceType.Net;
            }
        }catch(MalformedURLException e){
            type = ResourceType.Generic;
        }
    }

    public BLZResource(InputStream in, OutputStream out) {
        scanner = in;
        writer = new BufferedWriter(new OutputStreamWriter(out));
        type = ResourceType.Generic;
    }

    private void openFile(FileMode mode) throws IOException {
        switch (mode){
            case Create:
                {
                    File f = new File(location);
                    f.createNewFile();
                    writer = new BufferedWriter(new FileWriter(f));
                }
            break;
            case Read:
                {
                    canRead = true;
                    scanner = new BufferedInputStream(location.toURL().openStream());
                }
            break;
            case Write:
                {
                    File f = new File(location);
                    writer = new BufferedWriter(new FileWriter(f));
                }
            break;
            case ReadWrite:
                {
                    canRead = true;
                    scanner = new BufferedInputStream(location.toURL().openStream());
                    File f = new File(location);
                    writer = new BufferedWriter(new FileWriter(f));
                }
            break;
        }
    }

    public String toString(){
        if (location != null){
            return "<Resource " + location.toString() + ">";
        }else{
            return "<Resource>";
        }
    }

    public void write(String s) throws IOException {
        writer.write(s);
        writer.flush();
    }

    public String read() throws IOException {
        int read = scanner.read();
        byte[] input = {(byte) read};
        if (read == -1) {
            canRead = false;
            return "";
        }
        return new String(input, "UTF-8");
    }

    public boolean hasNext() {
        return canRead;
    }

    public void open(FileMode mode) throws IOException{
        switch (type) {
            case File:
            {
                openFile(mode);
            }
            break;
            case Generic:
            break;
            case Net:
            {
                URLConnection connection = location.toURL().openConnection();
                switch (mode){
                    case Read:
                        canRead = true;
                        openScanner(connection);
                    break;
                    case Write:
                        openWriter(connection);
                    break;
                    case ReadWrite:
                        canRead = true;
                        openScanner(connection);
                        openWriter(connection);
                    break;
                }
            }
            break;
        }
        if (location.toURL().getProtocol().equals("file")){
            openFile(mode);
            return;
        }
        
    }

    private void openScanner(URLConnection connection) throws IOException {
        if (scanner == null){
            connection.setDoInput(true);
            scanner = new BufferedInputStream(connection.getInputStream());
        }
    }

    private void openWriter(URLConnection connection) throws IOException {
        if (writer == null){
            connection.setDoOutput(true);
            writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        }
    }

    public void close() throws IOException {
        if (scanner != null){
            scanner.close();
            scanner = null;
        }
        if (writer != null){
            writer.close();
            writer = null;
        }
    }

    public enum FileMode {
        Read,
        Write,
        ReadWrite,
        Create
    }

    public enum ResourceType {
        File,
        Net,
        Generic
    }

}