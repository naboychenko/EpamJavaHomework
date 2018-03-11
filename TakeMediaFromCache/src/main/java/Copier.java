import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.*;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Viktoria Naboychenko on 15.02.2018.
 */
public class Copier {

    private String destinationPath;
    private String cachePath;

    public Copier(String cachePath, String destinationPath) {
        this.cachePath = cachePath;
        this.destinationPath = destinationPath;
    }

    private String generateRandom() {
        Random random = new Random();
        return String.valueOf(Math.abs(random.nextLong()));
    }

    public void copyFile(String sourceFileName, String destinationFileName, String fileExtension) {
        Path src = Paths.get(cachePath + sourceFileName);

        try {
            if (destinationFileName == null || checkIllegalChars(destinationFileName))
                destinationFileName = "UnknownName" + generateRandom() + fileExtension;

            Path dst = Paths.get(destinationPath + destinationFileName);

            Files.copy(src, dst, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean checkIllegalChars(String fileName) {
        Pattern p = Pattern.compile(".*([:\\\\/*?|<>]+).*");
        Matcher m = p.matcher(fileName);
        return m.find();
    }

    public void copyContent(String content, String destinationFileName, String fileExtension) {

        try {
            if (destinationFileName == null || "".equals(destinationFileName) || checkIllegalChars(destinationFileName))
                destinationFileName = "UnknownName" + generateRandom() + fileExtension;

            Path dst = Paths.get(destinationPath + destinationFileName);
            Files.write(dst, content.getBytes("KOI8-R"), StandardOpenOption.CREATE_NEW);
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
