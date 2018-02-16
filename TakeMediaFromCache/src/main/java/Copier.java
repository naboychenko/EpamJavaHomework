import java.io.IOException;
import java.nio.file.*;

/**
 * Created by Viktoria Naboychenko on 15.02.2018.
 */
public class Copier {

    String destinationPath;
    private String cachePath;

    public Copier(String cachePath, String destinationPath) {
        this.cachePath = cachePath;
        this.destinationPath = destinationPath;
    }

    public void copy(String sourceFileName, String destinationFileName){
        Path src = Paths.get(cachePath + sourceFileName);
        Path dst = Paths.get(destinationPath + destinationFileName);
        int i = 1;

        while(Files.exists(dst)){
            dst = Paths.get(destinationPath + destinationFileName.substring(0, destinationFileName.length() - 4) + i +
                    destinationFileName.substring(destinationFileName.length() - 4));
            i++;
        }

        try {
            Files.copy(src, dst, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e){
            System.out.println(e.getMessage());
        }

    }



}
