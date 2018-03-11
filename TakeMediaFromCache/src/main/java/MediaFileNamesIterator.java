import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Viktoria Naboychenko on 15.02.2018.
 */
public class MediaFileNamesIterator implements Iterator {

    private String currentFileName;
    private String cachePath;

    public MediaFileNamesIterator(String cachePath) {
        this.cachePath = cachePath;
        findFirst();
    }

    private void findFirst() {
        List<Integer> filesNumbers = new ArrayList<>();
        Pattern p = Pattern.compile("f_[0]*([a-f0-9]+)");
        Matcher m;
        File[] files = new File(cachePath).listFiles();
        String fileName;

        for (File file : files) {
            if (file.isFile()) {
                fileName = file.getName();
                m = p.matcher(fileName);
                if (m.find()) {
                    filesNumbers.add(Integer.valueOf(m.group(1), 16));
                }
            }
        }

        Collections.sort(filesNumbers);
        currentFileName = makeFileName(Integer.toString(filesNumbers.get(0) - 1, 16));
    }

    private String makeFileName(String number) {
        String newName = "f_";

        int nulls = 6 - number.length();
        for (int i = 0; i < nulls; i++)
            newName += "0";

        return newName + number;
    }

    private String getNext() {
        int number;

        Pattern p = Pattern.compile("f_[0]*([a-f0-9]+)");
        Matcher m = p.matcher(currentFileName);

        if (m.find())
            number = Integer.valueOf(m.group(1), 16) + 1;
        else
            return null;

        return makeFileName(Integer.toString(number, 16));
    }

    public boolean hasNext() {
        String nextFileName = getNext();

        if (nextFileName == null)
            return false;

        File nextFile = new File(cachePath + nextFileName);
        return nextFile.exists();
    }

    public String next() {
        String nextFileName = getNext();
        currentFileName = nextFileName;
        return nextFileName;
    }
}

