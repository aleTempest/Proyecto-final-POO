package org.ale;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final String DB_PATH = "/home/ale/Desktop/sis.db";
    private Thread clientThread;
    private ServerSocket serverSocket;
    private Socket clientSocket;

    public Server() {
        try {
            this.serverSocket = new ServerSocket(12345);
            while (true) {
                this.clientSocket = serverSocket.accept();
                this.clientThread = new Thread(new ClientHandler(clientSocket,DB_PATH));
                this.clientThread.start();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


}