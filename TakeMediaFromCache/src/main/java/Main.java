import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by Viktoria Naboychenko on 15.02.2018.
 * <p>
 * Программа копирует mp3 файлы из кеша для браузера Chrome в заданную пользователем директорию.
 * Если в метаданных файла есть название трека и исполнителя, то файл в папке назначения будет назван по шаблону
 * "Исполнитель - Название трека.mp3", иначе будет использовано имя по умолчанию "UnknownName[random long].mp3".
 * Возможны два режима работы :
 * 1. Простое копирование mp3 файлов из кеша в папку назначения с заменой названия.
 * 2. Объединение mp3 файлов относящихся к одному треку в один и помещение полученных треков в папку назначения.
 */

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        String defaultCachePath = "C:\\Users\\" + System.getProperty("user.name") + "\\AppData\\Local\\Google\\Chrome\\User Data\\Default\\Media Cache\\";

        System.out.println(
                "Press Enter for using the default cache path \"" + defaultCachePath + "\"\n" +
                        "Or input cache path:");
        String cachePath = scanner.nextLine();
        System.out.println("Input destination path: ");
        String destPath = scanner.next();

        try {
            MediaAnalyzer ma = new MediaAnalyzer(destPath, cachePath);

            System.out.println("Concatenate files?(y/n)");
            String mode = scanner.next();

            switch (mode) {
                case "y":
                    ma.findMediaWithConcat();
                    break;
                case "n":
                    ma.findMedia();
                    break;
                default:
                    System.out.println("Incorrect input");
            }

            System.out.println(ma.getFilesNumber() + " files were created in " + destPath);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

    }

}
