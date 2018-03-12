import org.apache.commons.cli.*;
import org.apache.commons.cli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Viktoria Naboychenko on 19.02.2018.
 */
public class FilesList {
    private static boolean sort = false;
    private static List<File> recursiveList;
    private Options options;

    public FilesList() {
        options = new Options();
        options.addOption("r", "recursive", false, "Recursive file listing");
        options.addOption("s", "sort", false, "Sort by date modified");
        options.addOption("h", "help", false, "Help");
    }

    public void function(String currentDir, String[] args) {

        if (args.length == 1) {
            showFileList(new File(currentDir));
            return;
        }

        CommandLineParser parser = new DefaultParser();

        HelpFormatter formatter = new HelpFormatter();

        try {
            CommandLine line = parser.parse(options, args);

            if (line.hasOption("s")) {
                sort = true;
                if (args.length == 2) {
                    showFileList(new File(currentDir));
                    return;
                }
            }

            if (line.hasOption("r")) {
                showDirTree(new File(currentDir));
            }
            if (line.hasOption("h")) {
                formatter.printHelp(args[0], options);
            }
        } catch (ParseException exp) {
            System.out.println(exp.getMessage());
        }
    }

    private static File[] sort(File[] files) {
        Arrays.sort(files, new Comparator<File>() {
            public int compare(File f1, File f2) {
                return Long.compare(f1.lastModified(), f2.lastModified());
            }
        });
        return files;
    }

    private void showFileList(File currentDir) {
        File[] files = currentDir.listFiles();
        if (sort) {
            sort(files);
            sort = false;
        }
        for (File file : files) {
            System.out.println(file.getName());
        }
    }

    class MyFileVisitor extends SimpleFileVisitor<Path> {
        public FileVisitResult visitFile(Path path, BasicFileAttributes fileAttributes) {

            recursiveList.add(path.toFile());
            return FileVisitResult.CONTINUE;
        }
    }

    private void showDirTree(File currentDir) {
        Path pathSource = Paths.get(currentDir.getAbsolutePath());
        recursiveList = new ArrayList<>();
        try {
            Files.walkFileTree(pathSource, new MyFileVisitor());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        if (sort) {
            recursiveList.sort((File f1, File f2) -> Long.compare(f1.lastModified(), f2.lastModified()));
            sort = false;
        }
        recursiveList.forEach((File) -> System.out.println(File.getName()));
    }
}
