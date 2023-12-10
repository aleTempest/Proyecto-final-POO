package org.ale;

import org.ale.DAO.UserDAO;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.BufferedReader;

public class Client {

    private Socket clientSocket;
    private PrintWriter outputStream;
    private BufferedReader inputStream;

    public Client(String address, int port) {
        try {
            clientSocket = new Socket(address, port);
            outputStream = new PrintWriter(clientSocket.getOutputStream(), true);
            inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            Thread serverListener = new Thread(this::listenToServer);
            serverListener.start();
            System.out.println("Connected to the server.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listenToServer() {
        try {
            String serverMessage;
            while ((serverMessage = inputStream.readLine()) != null) {
                System.out.println("Server says: " + serverMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        outputStream.println(message);
    }

    public void closeConnection() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client("localhost", 12345);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String message = scanner.nextLine();
            if (message.equalsIgnoreCase("exit")) {
                client.closeConnection();
                break;
            }

            client.sendMessage(message);
        }
    }
}
