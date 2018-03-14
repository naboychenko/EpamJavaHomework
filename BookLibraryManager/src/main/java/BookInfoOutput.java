import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Viktoria Naboychenko on 13.03.2018.
 */
public class BookInfoOutput {
    public static void show(ResultSet rs) {
        System.out.printf("%-22s%-22s%-22s%-22s%-22s\n", "isbn", "author", "added date", "amount", "title");
        try {
            while (rs.next()) {
                System.out.printf(
                        "%-22s%-22s%-22s%-22s%-22s\n",
                        rs.getString("isbn"),
                        rs.getString("author"),
                        rs.getString("added_date"),
                        rs.getInt("amount"),
                        rs.getString("title")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
