import java.sql.ResultSet;
import java.util.Scanner;

/**
 * Created by Viktoria Naboychenko on 12.03.2018.
 */
public class BookLibraryManager {

    private static void showMenu() {
        System.out.println(
                "\nSelect an action:\n" +
                        "0. Exit.\n" +
                        "1. Add book by ISBN.\n" +
                        "2. View list of stored books.\n" +
                        "3. View information about book by ISBN.\n" +
                        "4. Change number of books by ISBN.");
    }

    public static void start() {
        System.out.println("Book Library Manager.");
        DatabaseManager dbManager = new DatabaseManager("library.db");
        String action;
        Scanner scanner = new Scanner(System.in);

        dbManager.connectDatabase();
        dbManager.createTable();
        if (dbManager.connectionIsNull()) {
            System.out.println("Database connection failed.");
            return;
        }
        while (true) {
            showMenu();

            action = scanner.nextLine();

            switch (action) {
                case "0":
                    dbManager.closeAll();
                    return;
                case "1":
                    addBookByISBN(scanner, dbManager);
                    break;
                case "2":
                    getBooksList(scanner, dbManager);
                    break;
                case "3":
                    getBookByISBN(scanner, dbManager);
                    break;
                case "4":
                    changeNumberOfBooksByISBN(scanner, dbManager);
                    break;
            }
        }
    }

    private static void changeNumberOfBooksByISBN(Scanner scanner, DatabaseManager dbManager) {
        System.out.println("Enter isbn:");
        String isbn = scanner.nextLine();
        System.out.println("Enter new amount:");
        int amount = scanner.nextInt();
        dbManager.update(amount, isbn);
    }

    private static void getBookByISBN(Scanner scanner, DatabaseManager dbManager) {
        System.out.println("Enter isbn:");
        String isbn = scanner.nextLine();
        ResultSet rs = dbManager.selectBookByISBN(isbn);
        if (rs != null)
            BookInfoOutput.show(rs);
    }

    private static void addBookByISBN(Scanner scanner, DatabaseManager dbManager) {
        System.out.println("Enter isbn:");
        String isbn = scanner.nextLine();
        BookInfoProvider bookInfoProvider = new BookInfoProvider();
        Book book = bookInfoProvider.get(isbn);
        if (book == null) {
            System.err.println("Book with isbn = " + isbn + " does not exist on openlibrary.org");
        } else
            dbManager.insert(book);
    }

    private static void getBooksList(Scanner scanner, DatabaseManager dbManager) {
        System.out.println("Sort books by:\n" +
                "1. ISBN.\n" +
                "2. Added date.\n" +
                "3. Author.\n" +
                "4. Title.\n" +
                "5. Amount.");
        String sort = scanner.nextLine();
        String orderedBy = "id";
        switch (sort) {
            case "1":
                orderedBy = "isbn";
                break;
            case "2":
                orderedBy = "added_date";
                break;
            case "3":
                orderedBy = "author";
                break;
            case "4":
                orderedBy = "title";
                break;
            case "5":
                orderedBy = "amount";
                break;
            default:
                System.out.println("Wrong input.");
        }
        ResultSet rs = dbManager.selectAll(orderedBy);
        if (rs != null)
            BookInfoOutput.show(rs);

    }
}
