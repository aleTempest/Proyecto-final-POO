package org.ale.DAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDAO extends DAO{
    public UserDAO(String url) {
        super(url);
    }

    public ArrayList<String> getTeams() {
        var arr = new ArrayList<String>();
        try {
            var sql = "SELECT * FROM TEAMS";
            var result = execQuery(sql);
            while (result.next()) {
                var team_name = result.getString("team_name");
                var captain = result.getString("captain");
                arr.add(team_name + "," + captain);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return arr;
    }

    public void insertUser(String enrollment,String firstName,String lastName,String phone_number,String career,Integer team_id) {
        try {
            var sql = "INSERT INTO USERS (enrollment, first_name, last_name, phone_number, career, team_id) VALUES" +
                    " (?,?,?,?,?,?)";
            this.connection = connect();
            var statement = connection.prepareStatement(sql);
            statement.setString(1,enrollment);
            statement.setString(2,firstName);
            statement.setString(3,lastName);
            statement.setString(4,phone_number);
            statement.setString(5,career);
            statement.setInt(6,team_id);
            statement.executeQuery();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void changePlayerFromTeam(String enrollment, Integer newTeam) {
        try {
            var sql = "UPDATE USERS SET team_id = ? WHERE enrollment = ?";
            var connection = connect();
            var statement = connection.prepareStatement(sql);
            statement.setString(1,enrollment);
            statement.setInt(2,newTeam);
            statement.executeQuery();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void modifyUser(String enrollment,String firstName,String lastName,String phone_number,String career,Integer team_id) {
        try {
            var sql = "UPDATE USERS" +
                    " SET first_name = ?, last_name = ?,phone_number = ?,career = ?, team_id = ? WHERE enrollment = ?";
            var connection = connect();
            var statement = connection.prepareStatement(sql);
            statement.setString(1,firstName);
            statement.setString(2,lastName);
            statement.setString(3,phone_number);
            statement.setString(4,career);
            statement.setInt(5,team_id);
            statement.setString(6,enrollment);
            statement.executeQuery();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteUser(String enrollment) {
        try {
            var sql = "DELETE FROM USERS WHERE enrollment = " + enrollment;
            connection = connect();
            var statement = connection.prepareStatement(sql);
            statement.executeQuery();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public Boolean auth(String enrollment, String password) {
        try {
            var sql = "SELECT * FROM CREDENTIALS WHERE " + "enrollment = " + enrollment + " AND password = " + password;
            var result = execQuery(sql);
            return result.getString("enrollment").equals(enrollment) && result.getString("password").equals(password);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public ArrayList<String> getHistory(String id) {
        var arr = new ArrayList<String>();
        try {
            var sql = "SELECT * FROM GAME_HISTORY WHERE id = " + id;
            var result = execQuery(sql);
            while (result.next()) {
                var winning_date = result.getString("winning_date");
                var loser_team = result.getString("team_B");
                var row = winning_date + "," + loser_team;
                arr.add(row);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return arr;
    }

    public String printSelect(String enrollment) {
        var str = "";
        try {
            var sql = "SELECT * FROM USERS WHERE enrollment = " + enrollment;
            var result = execQuery(sql);
            str = result.getString("first_name");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return str;
    }
}
