import java.sql.*;

/**
 * Created by Viktoria Naboychenko on 12.03.2018.
 */
public class DatabaseManager {
    private String databaseLocation;
    private Statement statement;
    private Connection connection;
    private PreparedStatement preparedStatement;

    public boolean connectionIsNull() {
        return connection == null;
    }

    public DatabaseManager(String databaseLocation) {
        this.databaseLocation = databaseLocation;
    }

    public void connectDatabase() {
        try {
            connection = null;
            String url = "jdbc:sqlite:" + databaseLocation;
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            closeAll();
        }
    }

    public void createTable() {
        try {

            String sql = "CREATE TABLE IF NOT EXISTS library (\n"
                    + "	id integer PRIMARY KEY,\n"
                    + "	isbn text NOT NULL UNIQUE,\n"
                    + "	title text NOT NULL,\n"
                    + "	author text NOT NULL,\n"
                    + "	added_date text NOT NULL,\n"
                    + "	amount integer NOT NULL\n"
                    + ");";

            statement.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void insert(Book book) {
        try {

            closePreparedStatement();
            preparedStatement = null;
            ResultSet rs = selectBookByISBN(book.getIsbn());

            if (rs.next()) {
                System.out.println("Book with isbn = " + book.getIsbn() + " already exist in db");
                update(rs.getInt("amount") + 1, rs.getString("isbn"));
                return;
            }

            String insertTableSQL = "INSERT INTO library"
                    + "(isbn, title, author, added_date, amount) VALUES"
                    + "(?,?,?,?,?)";

            preparedStatement = connection.prepareStatement(insertTableSQL);

            preparedStatement.setString(1, book.getIsbn());
            preparedStatement.setString(2, book.getTitle());
            preparedStatement.setString(3, book.getAuthors());
            preparedStatement.setString(4, book.getAddedDate());
            preparedStatement.setInt(5, book.getAmount());
            preparedStatement.executeUpdate();

            System.out.println("Record is inserted");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public ResultSet selectBookByISBN(String isbn) {
        try {
            closePreparedStatement();
            preparedStatement = null;
            String select = "SELECT isbn, title, author, added_date, amount FROM library WHERE isbn = ?";

            preparedStatement = connection.prepareStatement(select);
            preparedStatement.setString(1, isbn);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public ResultSet selectAll(String orderedBy) {
        try {
            String select = "SELECT isbn, title, author, added_date, amount FROM library ORDER BY " + orderedBy + " ASC";

            return statement.executeQuery(select);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void update(int amount, String isbn) {
        try {
            String sql = "UPDATE library SET amount = ? "
                    + "WHERE isbn = ?";

            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, amount);
            preparedStatement.setString(2, isbn);

            preparedStatement.executeUpdate();
            System.out.println("Table is updated");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void closePreparedStatement() {
        try {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        } catch (SQLException e) {

            System.out.println(e.getMessage());

        }
    }

    public void closeAll() {
        closePreparedStatement();
        try {
            if (statement != null) {
                statement.close();
            }

            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {

            System.out.println(e.getMessage());

        }
    }
}
