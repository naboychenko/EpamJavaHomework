import java.util.Scanner;

/**
 * Created by Viktoria Naboychenko on 17.02.2018.
 */
public class CommandLine {
    private String currentDirectory;

    public CommandLine() {
        this.currentDirectory = "C:\\Users\\" + System.getProperty("user.name");
    }

    private void printHelp(){
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
        System.out.println("zip|tar -x file.zip");
        System.out.println("zip|tar file1 file2 file3");
    }

    public void function(){

        Scanner scanner = new Scanner(System.in);
        String [] arguments;
        printHelp();

        while(true){

            System.out.print(currentDirectory + ">");

            arguments = scanner.nextLine().split(" ");
            switch (arguments[0]){
                case "ls":
                    FilesList.function(currentDirectory, arguments);
                    break;
                case "cd"://не работает для файлов с пробелами в имени :(
                    ChangeDirectory cd = new ChangeDirectory(currentDirectory, arguments);
                    cd.function();
                    currentDirectory = cd.getCurrentDirectory();
                    break;
                case "zip":
                case "tar":
                    Archiver.function(currentDirectory, arguments);
                    break;
                default:
                    System.out.println("Command \""+ arguments[0] +"\" is not supported");
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
