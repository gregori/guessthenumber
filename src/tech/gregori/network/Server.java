package tech.gregori.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;

public class Server extends Network {
    private ServerSocket serverSocket;

    public Server(int port) {
        super(port);
    }

    @Override
    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        clientSocket = serverSocket.accept();
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    @Override
    public void stop() throws IOException {
        super.stop();
        serverSocket.close();
    }
}
