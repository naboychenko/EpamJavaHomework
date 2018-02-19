import org.apache.commons.cli.*;
import org.apache.commons.cli.CommandLine;

import java.io.File;

/**
 * Created by Viktoria Naboychenko on 19.02.2018.
 */
public class FilesList {
    public static void function(String currentDir, String[] args){

        if(args.length == 1){
            showFileList(new File(currentDir));
            return;
        }

        CommandLineParser parser = new DefaultParser();

        Options options = new Options();
        options.addOption( "r", "recursive", false, "Recursive" );
        options.addOption( "h", "help", false, "Help" );

        HelpFormatter formatter = new HelpFormatter();

        try {
            CommandLine line = parser.parse( options, args );

            if( line.hasOption( "r" ) ) {
                showDirTree(new File(currentDir));
            }
            if( line.hasOption( "h" ) ) {
                formatter.printHelp( args[0], options );
            }
        }
        catch( ParseException exp ) {
            System.out.println( "Unexpected exception:" + exp.getMessage() );
        }
    }

    private static void showFileList(File currentDir){
        for (File file : currentDir.listFiles()) {
            System.out.println(file.getName());
        }
    }

    private static void showDirTree(File currentDir){
        for (File file : currentDir.listFiles()) {
            if (file.isDirectory()) {
                showDirTree(file);
            }
            else {
                System.out.println(file.getName());
            }
        }
    }
}
