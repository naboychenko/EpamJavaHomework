import java.util.Scanner;

/**
 * Created by Viktoria Naboychenko on 15.02.2018.
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
