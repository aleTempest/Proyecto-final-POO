package org.ale;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

class ClientHandler implements Runnable {
    private Socket clientSocket;
    private String DB_PATH;
    public ClientHandler(Socket clienteSocket, String DB_PATH) {
        this.clientSocket = clienteSocket;
        this.DB_PATH = DB_PATH;
    }

    private ArrayList<String> clientInputHandler(Command command, String argument) {
        var dao = new DAO(DB_PATH);
        var arr = new ArrayList<String>();
        switch (command) {
            case GET_TEAMS -> arr.addAll(dao.getTeams());
            case GET_HISTORY -> arr.addAll(dao.getTeamHistory(argument));
            case GET_ALL_USERS -> arr.addAll(dao.getAllUsers());
            case GET_USER -> arr.add(dao.getUser(argument));
            case GET_TODAY_GAMES -> arr.addAll(dao.getTodayGames());
            case DELETE_USER -> dao.deleteUser(argument); // no debería ir aquí
            default -> System.out.println("bad");
        }
        return arr;
    }

    private void clientInputHandler(Command command, String[] arguments) {
        var dao = new DAO(DB_PATH);
        switch (command) {
            case AUTH_USER -> {
                var enrollment = arguments[1];
                var password = arguments[2];
                System.out.println(enrollment + "," + password);
                System.out.println(dao.auth(enrollment,password));
            }
            case MODIFY_USER -> {
                var enrollment = arguments[1];
                var firstName = arguments[2];
                var lastName = arguments[3];
                var phoneNumber = arguments[4];
                var career = arguments[5];
                var teamId = Integer.parseInt(arguments[6]);
                dao.modifyUser(enrollment,firstName,lastName,phoneNumber,career,teamId);
            }
            case INSERT_USER -> {
                var enrollment = arguments[1];
                var firstName = arguments[2];
                var lastName = arguments[3];
                var phoneNumber = arguments[4];
                var career = arguments[5];
                var teamId = Integer.parseInt(arguments[6]);
                dao.insertUser(enrollment,firstName,lastName,phoneNumber,career,teamId);
            }
            case INSERT_CREDENTIALS -> {
                var enrollment = arguments[1];
                var password = arguments[2];
                dao.insertCredentials(enrollment,password);
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
