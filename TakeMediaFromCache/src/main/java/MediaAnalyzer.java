import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Viktoria Naboychenko on 15.02.2018.
 */
public class MediaAnalyzer {

    private String cachePath;
    private String destPath;
    String finalName;
    private int count;

    public int getCount() {
        return count;
    }

    public MediaAnalyzer(String destPath) {
        count = 0;
        this.cachePath = "C:\\Users\\" + System.getProperty("user.name") + "\\AppData\\Local\\Google\\Chrome\\User Data\\Default\\Media Cache\\";
        this.destPath = destPath;
    }

    void findMedia(){
        List<Path> filesInFolder;
        Copier cp = new Copier(cachePath, destPath);
        String currentFileName;

        try {
            filesInFolder =  Files.walk(Paths.get(cachePath))
                    .filter(Files::isRegularFile)
                    .collect(Collectors.toList());

            for(Path p : filesInFolder){
                currentFileName = p.getFileName().toString();

                if(isMedia(currentFileName)){
                    cp.copy(currentFileName, finalName);
                    count++;
                }
            }
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    private boolean isMedia(String currentFile){

        Mp3Analyzer mp3A = new Mp3Analyzer(cachePath + currentFile);
        if(mp3A.isMp3()){
            finalName = mp3A.getFileName();
            return true;
        }
        return false;
    }

}
