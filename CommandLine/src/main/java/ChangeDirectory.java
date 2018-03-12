import org.apache.commons.cli.*;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.List;

/**
 * Created by Viktoria Naboychenko on 19.02.2018.
 */
public class ChangeDirectory {
    private String currentDirectory;
    private Options options;

    public ChangeDirectory(String currentDirectory) {
        this.currentDirectory = currentDirectory;
        options = new Options();
        options.addOption("p", "parent", false, "Go to the directory above");
        options.addOption("h", "help", false, "Help");
    }

    public String getCurrentDirectory() {
        return currentDirectory;
    }

    public void function(String args[]) {

        CommandLineParser parser = new DefaultParser();

        HelpFormatter formatter = new HelpFormatter();
        boolean flag = true;

        try {
            CommandLine line = parser.parse(options, args);

            if (line.hasOption("h")) {
                formatter.printHelp(args[0], options);
                flag = false;
            }
            if (line.hasOption("p")) {
                File f = new File(currentDirectory);
                if (f.getParentFile() != null) {
                    currentDirectory = FilenameUtils.getFullPathNoEndSeparator(f.getAbsolutePath());
                }
            } else if (flag) {
                List<String> unparsedArgList = line.getArgList();
                changeCurrentDirectory(unparsedArgList.get(1));
            }

        } catch (ParseException exp) {
            System.out.println(exp.getMessage());
        }
    }

    private void changeCurrentDirectory(String path) {
        File f = new File(path);
        if (f.isAbsolute() && f.exists() && f.isDirectory()) {
            currentDirectory = path;
        } else {
            File f2 = new File(currentDirectory + "\\" + path);
            if (f2.exists() && f2.isDirectory()) {
                currentDirectory = f2.getAbsolutePath();
            }
        }
    }
}
