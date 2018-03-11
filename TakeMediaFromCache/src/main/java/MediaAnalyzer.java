import java.io.File;
import java.io.FileNotFoundException;
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
    private String finalName;
    private int filesNumber;
    private String fileExtention;
    private boolean fileBeginning;

    public MediaAnalyzer(String destPath, String cachePath) throws FileNotFoundException {
        filesNumber = 0;
        if (cachePath.isEmpty())
            this.cachePath = "C:\\Users\\" + System.getProperty("user.name") + "\\AppData\\Local\\Google\\Chrome\\User Data\\Default\\Media Cache\\";
        else
            this.cachePath = appendSlash(cachePath);

        this.destPath = appendSlash(destPath);

        if (!isExistingDirectory(this.cachePath) || !isExistingDirectory(this.destPath))
            throw new FileNotFoundException("Directory " + this.cachePath + " does not exist.");
        if (!isExistingDirectory(this.destPath))
            throw new FileNotFoundException("Directory " + this.destPath + " does not exist.");
    }

    private boolean isExistingDirectory(String path) {
        return new File(path).isDirectory();
    }

    public int getFilesNumber() {
        return filesNumber;
    }

    private String appendSlash(String path) {
        return path.charAt(path.length() - 1) == '\\' ? path : path + "\\";
    }

    public void findMedia() {
        List<Path> filesInFolder;
        Copier cp = new Copier(cachePath, destPath);
        String currentFileName;

        try {
            filesInFolder = Files.walk(Paths.get(cachePath))
                    .filter(Files::isRegularFile)
                    .collect(Collectors.toList());

            for (Path p : filesInFolder) {
                currentFileName = p.getFileName().toString();

                if (isMedia(currentFileName, "")) {
                    cp.copyFile(currentFileName, finalName, fileExtention);
                    filesNumber++;
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void findMediaWithConcat() {

        MediaFileNamesIterator fileIterator = new MediaFileNamesIterator(cachePath);
        String currentFileName;
        String currentFinalName = new String();
        String currentFileContent = "";
        Copier cp = new Copier(cachePath, destPath);
        byte[] content;
        boolean first = true;
        fileBeginning = false;

        while (fileIterator.hasNext()) {
            currentFileName = fileIterator.next();
            try {
                if (isMedia(currentFileName, currentFinalName)) {
                    if (!first && fileBeginning) {
                        cp.copyContent(currentFileContent, currentFinalName, fileExtention);
                        currentFileContent = "";
                        filesNumber++;
                        currentFinalName = finalName;
                    }

                    content = Files.readAllBytes(Paths.get(cachePath + currentFileName));
                    currentFileContent += new String(content, "KOI8-R");

                    if (first) {
                        currentFinalName = finalName;
                        first = false;
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        if (!first) {
            cp.copyContent(currentFileContent, currentFinalName, fileExtention);
            filesNumber++;
        }
    }

    private boolean isMedia(String currentFile, String currentFinalName) {

        Mp3Analyzer mp3A = new Mp3Analyzer(cachePath + currentFile);
        if (mp3A.isMp3()) {
            finalName = mp3A.getFileName();
            fileBeginning = mp3A.hasNonCustomTag() && (currentFinalName == null || !currentFinalName.equals(finalName));
            fileExtention = ".mp3";
            return true;
        }
        return false;
    }
}
