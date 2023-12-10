package org.ale;

import org.ale.DAO.UserDAO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class ClientHandler implements Runnable {
    private Socket clientSocket;
    private String DB_PATH;
    public ClientHandler(Socket clienteSocket, String DB_PATH) {
        this.clientSocket = clienteSocket;
        this.DB_PATH = DB_PATH;
    }

    private void clientInputHandler(Command command, String argument) {
        var userDAO = new UserDAO(DB_PATH);
        switch (command) {
            case GET_USER -> System.out.println(userDAO.printSelect(argument));
            case GET_HISTORY -> System.out.println(userDAO.getHistory(argument));
            case GET_TEAMS -> System.out.println(userDAO.getTeams());
        }
    }

    private void clientInputHandler(Command command, String[] arguments) {
        var userDAO = new UserDAO(DB_PATH);
        switch (command) {
            case AUTH_USER -> {
                var enrollment = arguments[1];
                var password = arguments[2];
                System.out.println(enrollment + "," + password);
                System.out.println(userDAO.auth(enrollment,password));
            }
        }
    }


    @Override
    public void run() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
            String msg;
            while ((msg = input.readLine()) != null) {
                var split = msg.split(" ");
                Command command;
                if (split.length == 1) {
                    command = Command.valueOf(msg.toUpperCase());
                    this.clientInputHandler(command,"");
                } else {
                    command = Command.valueOf(split[0].toUpperCase());
                }
                if (split.length == 2) {
                    var argument = split[1];
                    this.clientInputHandler(command,argument);
                } else {
                    this.clientInputHandler(command,split);
                }
            }

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        var server = new Server();
    }
}
