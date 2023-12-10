package org.ale;

import java.util.ArrayList;
import java.sql.*;

public class DAO {
    private String url;
    protected Connection connection;
    public DAO(String url) {
        this.url = url;
    }

    protected ResultSet execQuery(String query) {
        try {
            connection = connect();
            PreparedStatement statement = connection.prepareStatement(query);
            return statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected Connection connect() {
        try {
            return DriverManager.getConnection("jdbc:sqlite:" + url);
        } catch (SQLException e) {
            System.out.println("Error connecting... " + e.getMessage());
        }
        return null;
    }

    protected void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
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



    public ArrayList<String> getAllUsers() {
        try {
            var arr = new ArrayList<String>();
            var sql = "SELECT * FROM USERS";
            var result = execQuery(sql);
            while (result.next()) {
                var firstName = result.getString("first_name");
                var lastName = result.getString("last_name");
                var enrollment = result.getString("enrollment");
                var phone_number = result.getString("phone_number ");
                var team_id = result.getString("team_id ");
                var career = result.getString("career ");
                var str = enrollment + "," + firstName + "," + lastName + "," + phone_number + "," + career + "," + team_id;
                arr.add(str);
            }
            return arr;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
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

    public void insertCredentials(String enrollment, String password) {
        try {
            var sql = "INSERT INTO CREDENTIALS VALUES (?,?)";
            var connection = connect();
            var statement = connection.prepareStatement(sql);
            statement.setString(1,enrollment);
            statement.setString(2,password);
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private int getNextId(String tableName, String colName) {
        try {
            var sql = "SELECT COUNT(game_id) FROM " + tableName.toUpperCase();
            var nextId = execQuery(sql).getInt("COUNT(" + colName.toLowerCase() + ")");
            return nextId;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    public void insertGame(Integer teamId1, Integer teamId2, String date) {
        try {
            var nextId = "SELECT COUNT(game_id) FROM GAMES";
            var sql = "INSERT INTO GAMES VALUES (game_id, team_A, team_B,game_date) " +
                    "(?,?,?,?)";
            var connection = connect();
            var statement = connection.prepareStatement(sql);
            statement.setInt(1,Integer.parseInt(nextId));
            statement.setInt(2,teamId1);
            statement.setInt(3,teamId2);
            statement.setString(4,date);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // public void insertHistory()

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

    public void deleteTeam(String team_id) {

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

    public ArrayList<String> getTodayGames() {
        var arr = new ArrayList<String>();
        try {
            var sql = "SELECT * FROM TODAY_GAMES";
            var result = execQuery(sql);
            while (result.next()) {
                var gameId = result.getString("game_id");
                var gameDate = result.getDate("game_date").toString();
                var teamId1 = result.getInt("team_A");
                var teamId2 = result.getInt("team_B");
                var teamName1 = result.getString("team_A_name");
                var teamName2 = result.getString("team_B_name");
                arr.add(gameId + "," + gameDate + "," + teamId1 + "," + teamId2 + "," + teamName1 + "," + teamName2);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return arr;
    }

    public ArrayList<String> getHistory() {
        var arr = new ArrayList<String>();
        try {
            var sql = "SELECT * FROM GAME_HISTORY";
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

    public ArrayList<String> getTeamHistory(String id) {
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

    public String getUser(String p_enrollment) {
        var str = "";
        try {
            var sql = "SELECT * FROM USERS WHERE enrollment = " + p_enrollment;
            var result = execQuery(sql);
            var firstName = result.getString("first_name");
            var lastName = result.getString("last_name");
            var enrollment = result.getString("enrollment");
            var phone_number = result.getString("phone_number");
            var team_id = result.getString("team_id");
            var career = result.getString("career");
            str = enrollment + "," + firstName + "," + lastName + "," + phone_number + "," + career + "," + team_id;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return str;
    }

    public static void main(String[] args) {
        var test = new DAO("/home/ale/Desktop/sis.db");
        System.out.println(test.getNextId("GAMES","game_id"));
    }
}
