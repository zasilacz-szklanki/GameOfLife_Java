package org.example.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JdbcGameOfLifeBoardDao implements Dao<GameOfLifeBoard> {
    private int id = -1;
    private final String boardName;
    private String url = "jdbc:derby:GameOfLifeDB;create=true";
    private String driverManager = "jdbc:derby:;shutdown=true";

    public JdbcGameOfLifeBoardDao(String boardName) {
        this.boardName = boardName;
    }

    public JdbcGameOfLifeBoardDao(String boardName, String url, String driverManager) {
        this.boardName = boardName;
        this.url = url;
        this.driverManager = driverManager;
    }

    //do usuniecia w wersji finalnej
    private void testDB() {
        String querySql = "SELECT * FROM BoardName";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(querySql)) {

            ResultSet rs = pstmt.executeQuery();

            System.out.println("#########");

            while (rs.next()) {
                System.out.println("id: " + rs.getInt("id") + ", name: " + rs.getString("name"));
            }

        } catch (SQLException e) {
            System.err.println("Błąd podczas pobierania planszy: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<String> getBoardNames() throws DbException {
        String querySql = "SELECT name FROM BoardName";
        List<String> list = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(querySql)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                list.add(rs.getString("name"));
            }

        } catch (SQLException e) {
            System.err.println("Błąd podczas pobierania planszy: " + e.getMessage());
            throw new DbReadException("Błąd podczas pobierania planszy: " + e.getMessage());
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
                System.out.println("id: " + id);
            }

            if (ids.isEmpty()) {
                System.out.println("Nie istnieje plansza o nazwie: " + this.boardName);
                throw new DbException("Nie istnieje plansza o nazwie: " + this.boardName);
            } else if (ids.size() == 1) {
                this.id = ids.get(0);
            } else {
                System.out.println("Znaleziono więcej niż jedną planszę o nazwie: " + this.boardName);
                this.id = ids.get(0);
                return;
            }

            System.out.println("Pobrana plansza o id: " + this.id + " oraz nazwie : " + this.boardName);

        } catch (SQLException e) {
            System.err.println("Błąd podczas pobierania planszy: " + e.getMessage());
            throw new DbReadException("Błąd podczas pobierania planszy: " + e.getMessage());
        }
    }

    public void createTables() throws DbException {
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            String createTableSql = "CREATE TABLE GameOfLifeBoard (id INT, x  INT, y  INT, value INT)";
            stmt.executeUpdate(createTableSql);

            createTableSql = "CREATE TABLE BoardName (id INT PRIMARY KEY, name CHAR(32) NOT NULL)";
            stmt.executeUpdate(createTableSql);

            System.out.println("Tabela GameOfLifeBoard została utworzona.");
            System.out.println("Tabela BoardName została utworzona.");
        } catch (SQLException e) {
            if ("X0Y32".equals(e.getSQLState())) {
                System.out.println("Tabele już istnieją.");
                throw new DbTableExistException("Tabele już istnieją.");
            } else {
                System.out.println("Błąd podczas tworzenia tabeli: " + e.getMessage());
                throw new DbException("Błąd podczas tworzenia tabeli: " + e.getMessage());
            }
        }
    }

    private int getNextId() throws DbException {
        String querySql = "SELECT MAX(id) AS id_max FROM BoardName";

        int nextId = -1;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(querySql)) {

            ResultSet rs = pstmt.executeQuery();

            if(!rs.next()){
                throw new DbException("Brak wyniku");
            }

            nextId = rs.getInt("id_max") + 1;

        } catch (SQLException e) {
            System.out.println("Błąd podczas zapytania: " + e.getMessage());
            throw new DbException("Błąd podczas zapytania: " + e.getMessage());
        }

        return nextId;
    }

    @Override
    public GameOfLifeBoard read() throws DbException {
        try {
            getIdByName();
        } catch (DbException e) {
            throw new DbException("Nie istnieje plansza: " + e.getMessage());
        }

        String querySql = "SELECT id,x,y,value FROM GameOfLifeBoard WHERE id = ?";
        int id;
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
                System.out.println(id + " " + x + " " + y + " " + value);
            }

            if (values.isEmpty()) {
                System.out.println("Nie istnieje plansza o id: " + this.id);
                return null;
            }

            int rows = Collections.max(xs) + 1;
            int columns = Collections.max(ys) + 1;

            System.out.println("Size: " + columns + " " + rows);

            boolean[][] v = new boolean[rows][columns];

            System.out.println("Ar size: " + values.size());

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    v[i][j] = values.get(i * columns + j) == 1;
                }
            }

            board = new GameOfLifeBoard(v);
            System.out.println("Pobrana plansza o id " + this.id + ":\n" + board);

        } catch (SQLException e) {
            System.err.println("Błąd podczas pobierania planszy: " + e.getMessage());
            throw new DbReadException("Błąd podczas pobierania planszy: " + e.getMessage());
        }

        return board;
    }

    @Override
    public void write(GameOfLifeBoard board) throws DbException {
        try {
            this.id = getNextId();
        } catch (DbException e) {
            throw new DbException("Nie znaleziono id: " + this.id + " " + e.getMessage());
        }

        String insertSql = "INSERT INTO GameOfLifeBoard (id,x,y,value) VALUES (?,?,?,?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(insertSql)) {

            for (int i = 0; i < board.getBoard().size(); i++) {
                for (int j = 0; j < board.getBoard().get(0).size(); j++) {
                    pstmt.setInt(1, this.id);
                    pstmt.setInt(2, i);//wiersze
                    pstmt.setInt(3, j);//kolumny
                    pstmt.setInt(4, board.get(i, j) ? 1 : 0);
                    pstmt.executeUpdate();
                }
            }
            System.out.println("Plansza została wstawiona do bazy danych: " + this.id);
        } catch (SQLException e) {
            System.err.println("Błąd podczas wstawiania planszy: " + e.getMessage());
            throw new DbWriteException("Błąd podczas wstawiania planszy: " + e.getMessage());
        }

        insertSql = "INSERT INTO BoardName (id,name) VALUES (?,?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(insertSql)) {

            pstmt.setInt(1, this.id);
            pstmt.setString(2, this.boardName);
            pstmt.executeUpdate();

            System.out.println("Nazwa planszy została wstawiona do bazy danych: " + this.boardName);
        } catch (SQLException e) {
            System.err.println("Błąd podczas wstawiania nazwy planszy: " + e.getMessage());
            throw new DbWriteException("Błąd podczas wstawiania nazwy planszy: " + e.getMessage());
        }
    }

    @Override
    public void close() throws Exception {
        try {
            DriverManager.getConnection(this.driverManager);
            System.out.println("Baza danych została poprawnie zamknięta.");
        } catch (SQLException e) {
            if ("XJ015".equals(e.getSQLState())) {
                System.out.println("Derby zostało zamknięte.");
            } else {
                System.err.println("Błąd podczas zamykania bazy danych: " + e.getMessage());
                throw new DbException("Błąd podczas zamykania bazy danych: " + e.getMessage());
            }
        }
    }

    //do usuniecia w finalnej wersji
    public static void main(String[] args) {
        boolean write = !true;
        boolean[][] init = {
                {false, false},
                {false, true},
                {true, false},
        };

        /*
        JdbcGameOfLifeBoardDao dao = new JdbcGameOfLifeBoardDao("PlanszaTestowa");//Kim był Testow?
        try {
            dao.createTables();
        } catch (DbException e) {
            System.err.println(e.getMessage());
        }
        */


        try (JdbcGameOfLifeBoardDao dao = new JdbcGameOfLifeBoardDao("p3")) {
            if (write) {
                //GameOfLifeBoard board = new GameOfLifeBoard(init);
                //dao.write(board);
                GameOfLifeBoard board = new GameOfLifeBoard(3, 3);
                dao.write(board);
            } else {
                GameOfLifeBoard board = dao.read();
                System.out.println(dao.getBoardNames());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }
}
