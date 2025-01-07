package org.example.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JdbcGameOfLifeBoardDao implements Dao<GameOfLifeBoard> {
    private int id = -1;
    private final String boardName;
    private String url = "jdbc:derby:GameOfLifeDB;create=true";
    private Connection conn = null;

    public JdbcGameOfLifeBoardDao(String boardName) {
        this.boardName = boardName;
    }

    public JdbcGameOfLifeBoardDao(String boardName, String url) {
        this.boardName = boardName;
        this.url = url;
    }

    public String getBoardName() {
        return this.boardName;
    }

    public List<String> getBoardNames() throws DbReadException {
        String querySql = "SELECT name FROM BoardName";
        List<String> list = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(querySql)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String str = rs.getString("name");
                list.add(str.trim());
            }

        } catch (SQLException e) {
            throw new DbReadException();
        }

        return Collections.unmodifiableList(list);
    }

    private void getIdByName() throws DbException {
        String querySql = "SELECT id FROM BoardName WHERE name = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(querySql)) {

            pstmt.setString(1, this.boardName);
            ResultSet rs = pstmt.executeQuery();

            List<Integer> ids = new ArrayList<>();
            int id;

            while (rs.next()) {
                id = rs.getInt("id");
                ids.add(id);
            }

            if (ids.isEmpty()) {
                throw new DbException();
            }

            this.id = ids.get(0);

        } catch (SQLException e) {
            throw new DbReadException();
        }
    }

    public void createTables() throws DbException {
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            String createTableSql = "CREATE TABLE GameOfLifeBoard (id INT, x  INT, y  INT, value INT)";
            stmt.executeUpdate(createTableSql);

            createTableSql = "CREATE TABLE BoardName (id INT PRIMARY KEY, name CHAR(32) NOT NULL)";
            stmt.executeUpdate(createTableSql);

        } catch (SQLException e) {
            if ("X0Y32".equals(e.getSQLState())) {
                throw new DbTableExistException();
            } else {
                throw new DbException();
            }
        }
    }

    private int getNextId() throws DbException {
        String querySql = "SELECT MAX(id) AS id_max FROM BoardName";

        int nextId = -1;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(querySql)) {

            ResultSet rs = pstmt.executeQuery();

            if (!rs.next()) {
                throw new DbException();
            }

            nextId = rs.getInt("id_max") + 1;

        } catch (SQLException e) {
            throw new DbException();
        }

        return nextId;
    }

    @Override
    public GameOfLifeBoard read() throws DbException {
        try {
            getIdByName();
        } catch (DbException e) {
            throw new DbException();
        }

        String querySql = "SELECT id,x,y,value FROM GameOfLifeBoard WHERE id = ?";
        int x;
        int y;
        int value;
        GameOfLifeBoard board = null;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(querySql)) {

            pstmt.setInt(1, this.id);
            ResultSet rs = pstmt.executeQuery();

            List<Integer> xs = new ArrayList<>();
            List<Integer> ys = new ArrayList<>();
            List<Integer> values = new ArrayList<>();

            while (rs.next()) {
                id = rs.getInt("id");
                x = rs.getInt("x");
                xs.add(x);//wiersze
                y = rs.getInt("y");
                ys.add(y);//kolumny
                value = rs.getInt("value");
                values.add(value);
            }

            if (values.isEmpty()) {
                throw new DbReadException();
            }

            int rows = Collections.max(xs) + 1;
            int columns = Collections.max(ys) + 1;

            boolean[][] v = new boolean[rows][columns];

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    v[i][j] = values.get(i * columns + j) == 1;
                }
            }

            board = new GameOfLifeBoard(v);

        } catch (SQLException e) {
            throw new DbReadException();
        }

        return board;
    }

    @Override
    public void write(GameOfLifeBoard board) throws DbException {
        try {
            this.id = getNextId();
        } catch (DbException e) {
            throw new DbException();
        }

        String querySql = "INSERT INTO GameOfLifeBoard (id,x,y,value) VALUES (?,?,?,?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(querySql)) {

            for (int i = 0; i < board.getBoard().size(); i++) {
                for (int j = 0; j < board.getBoard().get(0).size(); j++) {
                    pstmt.setInt(1, this.id);
                    pstmt.setInt(2, i);//wiersze
                    pstmt.setInt(3, j);//kolumny
                    pstmt.setInt(4, board.get(i, j) ? 1 : 0);
                    pstmt.executeUpdate();
                }
            }

        } catch (SQLException e) {
            throw new DbWriteException();
        }

        querySql = "INSERT INTO BoardName (id,name) VALUES (?,?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(querySql)) {

            pstmt.setInt(1, this.id);
            pstmt.setString(2, this.boardName);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new DbWriteException();
        }
    }

    @Override
    public void close() throws DbException {
        if (this.conn != null) {
            try {
                this.conn.close();
            } catch (SQLException e) {
                throw new DbException();
            }
        }
    }
}
