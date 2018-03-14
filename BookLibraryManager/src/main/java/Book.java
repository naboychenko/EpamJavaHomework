import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Viktoria Naboychenko on 14.03.2018.
 */
public class Book {
    private String isbn;
    private String title;
    private List<String> authors;
    private String addedDate;
    private int amount;

    public String getIsbn() {
        return isbn;
    }

    public Book(String isbn, String title, ArrayList<String> authors) {
        this.isbn = isbn;
        this.title = title;
        this.authors = authors;
        setAddedDate();
        this.amount = 1;
    }

    private void setAddedDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate localDate = LocalDate.now();
        this.addedDate = dtf.format(localDate);
    }

    public String getTitle() {
        return title;
    }

    public String getAuthors() {
        return authors.toString();
    }

    public String getAddedDate() {
        return addedDate;
    }

    public int getAmount() {
        return amount;
    }
}
