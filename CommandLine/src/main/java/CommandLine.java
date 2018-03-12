import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Viktoria Naboychenko on 17.02.2018.
 *
 *
 * Программа предоставляет следующие возможномти:
 * 1. Перемещение по директориям с помощью команды cd.
 * 2. Получение списка файлов текущей директории ls.
 * 3. Архивация и извлечение из архивов командами tar и zip.
 * Для просмотра возможностей каждой команды введите ее с ключом "-h"
 */
public class CommandLine {
    private String currentDirectory;

    public CommandLine() {
        this.currentDirectory = "C:\\Users\\" + System.getProperty("user.name");
    }

    private void printHelp() {
        System.out.println("Usage: CommandLine");
        System.out.println("ls [options]");
        System.out.println("cd [filename] | [options]");
        System.out.println("zip [options] files");
        System.out.println("tar [options] files");
        System.out.println("Examples:");
        System.out.println("cd -p");
        System.out.println("cd C:\\Logs");
        System.out.println("ls ");
        System.out.println("ls -r");
        System.out.println("ls -s");
        System.out.println("ls -r -s");
        System.out.println("zip|tar -x file.zip");
        System.out.println("zip|tar file1 file2 file3");
    }

    private String[] splitArgs(String arguments) {
        List<String> argsList = new ArrayList<>();
        Pattern pattern = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
        Matcher matcher = pattern.matcher(arguments);
        while (matcher.find()) {
            if (matcher.group(1) != null) {
                argsList.add(matcher.group(1));
            } else if (matcher.group(2) != null) {
                argsList.add(matcher.group(2));
            } else {
                argsList.add(matcher.group());
            }
        }
        return argsList.toArray(new String[0]);
    }

    public void function() {

        Scanner scanner = new Scanner(System.in);
        String[] arguments;
        printHelp();
        FilesList ls = new FilesList();
        ChangeDirectory cd = new ChangeDirectory(currentDirectory);
        Archiver archiver = new Archiver();

        while (true) {

            System.out.print(currentDirectory + ">");

            arguments = splitArgs(scanner.nextLine());
            switch (arguments[0]) {
                case "ls":
                    ls.function(currentDirectory, arguments);
                    break;
                case "cd":
                    cd.function(arguments);
                    currentDirectory = cd.getCurrentDirectory();
                    break;
                case "zip":
                case "tar":
                    archiver.function(currentDirectory, arguments);
                    break;
                default:
                    System.out.println("Command \"" + arguments[0] + "\" is not supported");
                    printHelp();
                    break;
            }
        }
    }

    public static void main(String[] args) {

        CommandLine cl = new CommandLine();
        cl.function();
    }
}
