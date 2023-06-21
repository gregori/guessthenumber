package tech.gregori.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public abstract class Network {
    protected Socket clientSocket;
    protected PrintWriter out;
    protected BufferedReader in;
    protected int port;
    protected String ip;

    public Network(int port) {
        this.port = port;
    }

    public Network(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public abstract void start() throws IOException;

    public void stop() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

    public void sendMessage(String msg) {
        out.println(msg);
    }

    public String receiveMessage() throws IOException {
        return in.readLine();
    }
}
