package com.blazingkin.interpreter.variables;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URLConnection;
import java.util.Scanner;
import java.io.FileWriter;

public class BLZResource {

    private URI location;
    private Scanner scanner;
    private BufferedWriter writer;

    public BLZResource(URI location){
        this.location = location;
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
                    scanner = new Scanner(location.toURL().openStream());
                    scanner.useDelimiter("");
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
                    scanner = new Scanner(location.toURL().openStream());
                    scanner.useDelimiter("");
                    File f = new File(location);
                    writer = new BufferedWriter(new FileWriter(f));
                }
            break;
        }
    }

    public void write(String s) throws IOException {
        writer.write(s);
        writer.flush();
    }

    public String read() throws IOException {
        return scanner.next();
    }

    public boolean hasNext() {
        return scanner != null && scanner.hasNext();
    }

    public void open(FileMode mode) throws IOException{
        if (location.toURL().getProtocol().equals("file")){
            openFile(mode);
        }
        URLConnection connection = location.toURL().openConnection();
        switch (mode){
            case Read:
                openScanner(connection);
            break;
            case Write:
                openWriter(connection);
            break;
            case ReadWrite:
                openScanner(connection);
                openWriter(connection);
            break;
        }
    }

    private void openScanner(URLConnection connection) throws IOException {
        if (scanner == null){
            connection.setDoInput(true);
            scanner = new Scanner(connection.getInputStream());
            scanner.useDelimiter("");
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

}