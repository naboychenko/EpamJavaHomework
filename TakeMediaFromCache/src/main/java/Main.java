import java.util.Scanner;

/**
 * Created by Viktoria Naboychenko on 15.02.2018.
 
 Программа копирует mp3 файлы из кеша для браузера Chrome в заданную пользователем директорию.
 Если в метаданных файла есть название трека и исполнителя, то файл в папке назначения будет назван по шаблону
 "Исполнитель - Название трека.mp3", иначе будет использовано имя по умолчанию "UnknownArtist - UnknownTitle(Number).mp3",
 где Number порядковый номер трека с именем по умолчанию в папке назначения.
 */

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Destination Path: ");

        String destPath = scanner.next();
        destPath = destPath.charAt(destPath.length() - 1) == '\\' ? destPath : destPath + "\\";

        MediaAnalyzer ma = new MediaAnalyzer(destPath);
        ma.findMedia();
        System.out.println(ma.getCount() + " files was copied from cache to " + destPath);

    }

}
