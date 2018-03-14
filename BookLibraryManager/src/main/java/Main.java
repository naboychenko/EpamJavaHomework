/**
 * Created by Viktoria Naboychenko on 14.03.2018.
 * Программа позволяет работать с БД sqlite. В БД хранится информация о книгах.
 * Добавлять книги можно по isbn. Остальные данные о книге запрашиваются с ресурса openlibrary.org и добаляются в базу.
 * Также можно посмтреть список книг в базе, отстортированных по названию, автору, дате добавления, isbn, количеству.
 * Пользователю предоставляется возможность менять значение "количество книг" для каждой книги.
 */
public class Main {
    public static void main(String[] args) {
        BookLibraryManager.start();
    }
}
