package org.example.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GameOfLifeBoardDB {

    private final String url = "jdbc:derby:GameOfLifeDB;create=true";

    public void createTable() {
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            String createTableSQL = "CREATE TABLE GameOfLifeBoard (" +
                    "id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," +
                    "board LONG VARCHAR)";
            stmt.executeUpdate(createTableSQL);

            System.out.println("Tabela GameOfLifeBoard została utworzona.");
        } catch (SQLException e) {
            if ("X0Y32".equals(e.getSQLState())) {
                System.out.println("Tabela już istnieje.");
            } else {
                System.err.println("Błąd podczas tworzenia tabeli: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void insertBoard(String board) {
        String insertSQL = "INSERT INTO GameOfLifeBoard (board) VALUES (?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

            pstmt.setString(1, board);
            pstmt.executeUpdate();

            System.out.println("Plansza została wstawiona do bazy danych.");
        } catch (SQLException e) {
            System.err.println("Błąd podczas wstawiania planszy: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String retrieveBoard(int id) {
        String querySQL = "SELECT board FROM GameOfLifeBoard WHERE id = ?";
        String board = null;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(querySQL)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                board = rs.getString("board");
                System.out.println("Pobrana plansza o id " + id + ":\n" + board);
            } else {
                System.out.println("Nie istnieje plansza o id: " + id);
            }
        } catch (SQLException e) {
            System.err.println("Błąd podczas pobierania planszy: " + e.getMessage());
            e.printStackTrace();
        }

        return board;
    }

    public void clearTable() {
        String deleteSQL = "DELETE FROM GameOfLifeBoard";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(deleteSQL);
            System.out.println("Tabela GameOfLifeBoard została wyczyszczona.");
        } catch (SQLException e) {
            System.err.println("Błąd podczas czyszczenia tabeli: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        GameOfLifeBoardDB db = new GameOfLifeBoardDB();

        // Sprawdzanie istnienia tabeli i tworzenie, jeśli nie istnieje
        //db.createTable();

        // Wstawianie przykładowych plansz
        String board1 = "0010\n0110\n1001\n0000";
        String board2 = "1111\n0000\n1111\n0000";
        String board3 = "asdf\n0000\n1111\n0000";
        String board4 = "asdf\nasdf\nasdf\nasdf";
        //db.insertBoard(board1);
        //db.insertBoard(board2);
        //db.insertBoard(board3);
        //db.insertBoard(board4);

        // Pobieranie planszy
        db.retrieveBoard(1);
        db.retrieveBoard(2);
        db.retrieveBoard(3);
        db.retrieveBoard(4);

        try {
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
            System.out.println("Baza danych została poprawnie zamknięta.");
        } catch (SQLException e) {
            if ("XJ015".equals(e.getSQLState())) {
                System.out.println("Derby zostało zamknięte.");
            } else {
                System.err.println("Błąd podczas zamykania bazy danych: " + e.getMessage());
            }
        }
    }
}
