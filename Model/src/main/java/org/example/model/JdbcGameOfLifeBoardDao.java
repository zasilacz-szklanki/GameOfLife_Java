package org.example.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JdbcGameOfLifeBoardDao implements Dao<GameOfLifeBoard> {

    private int id;
    private final String url = "jdbc:derby:GameOfLifeDB;create=true";

    public JdbcGameOfLifeBoardDao(int id) {
        //TODO zamiast id trzeba bedzie przekazac nazwe planszy
        this.id = id;
        createTable();
    }

    private void createTable() {
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            String createTableSQL = "CREATE TABLE GameOfLifeBoard (" +
                    "id INT," +
                    "x  INT," +
                    "y  INT," +
                    "value INT)";
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

    @Override
    public GameOfLifeBoard read() {
        String querySQL = "SELECT id,x,y,value FROM GameOfLifeBoard WHERE id = ?";
        int _id,_x,_y,_value;
        GameOfLifeBoard board=null;


        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(querySQL)) {

            pstmt.setInt(1, this.id);
            ResultSet rs = pstmt.executeQuery();

            List<Integer> xs = new ArrayList<>();
            List<Integer> ys = new ArrayList<>();
            List<Integer> values = new ArrayList<>();

            while (rs.next()) {
                _id = rs.getInt("id");
                _x = rs.getInt("x");
                xs.add(_x);//wiersze
                _y = rs.getInt("y");
                ys.add(_y);//kolumny
                _value = rs.getInt("value");
                values.add(_value);
                System.out.println(_id + " " + _x + " " + _y + " " + _value);
            }

            if (values.isEmpty()) {
                System.out.println("Nie istnieje plansza o id: " + this.id);
                return null;
            }

            int rows = Collections.max(xs) + 1;
            int columns = Collections.max(ys) + 1;

            System.out.println("Size: "+columns + " " + rows);

            boolean[][] v=new boolean[rows][columns];

            System.out.println("Ar size: "+values.size());

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    v[i][j] = values.get(i * columns + j) == 1;
                }
            }

            board = new GameOfLifeBoard(v);
            System.out.println("Pobrana plansza o id " + this.id + ":\n" + board);

        } catch (SQLException e) {
            System.err.println("Błąd podczas pobierania planszy: " + e.getMessage());
            e.printStackTrace();
        }

        return board;
    }

    @Override
    public void write(GameOfLifeBoard board) {
        String insertSQL = "INSERT INTO GameOfLifeBoard (id,x,y,value) VALUES (?,?,?,?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

            for(int i=0;i<board.getBoard().size();i++) {
                for (int j = 0; j < board.getBoard().get(0).size(); j++) {
                    pstmt.setInt(1, this.id);
                    pstmt.setInt(2, i);//wiersze
                    pstmt.setInt(3, j);//kolumny
                    pstmt.setInt(4, board.get(i, j) ? 1 : 0);
                    pstmt.executeUpdate();
                }
            }
            System.out.println("Plansza została wstawiona do bazy danych.");
        } catch (SQLException e) {
            System.err.println("Błąd podczas wstawiania planszy: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws Exception {
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

    public void setId(int id){
        this.id=id;
    }

    public static void main(String[] args) {
        boolean write_asdf=!true;
        boolean[][] init = {
                {false, false},
                {false, true},
                {true, true},
        };
        try(JdbcGameOfLifeBoardDao dao = new JdbcGameOfLifeBoardDao(1)) {
            if(write_asdf){
                GameOfLifeBoard board = new GameOfLifeBoard(init);
                dao.write(board);
            }
            else{
                //dao.setId(1);
                GameOfLifeBoard board=dao.read();
                System.out.println(board);
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
