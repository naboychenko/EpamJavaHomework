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
    private String args[];

    public ChangeDirectory(String currentDirectory, String[] args) {
        this.currentDirectory = currentDirectory;
        this.args = args;
    }

    public String getCurrentDirectory() {
        return currentDirectory;
    }

    public void function(){

        CommandLineParser parser = new DefaultParser();

        Options options = new Options();
        options.addOption( "p", "parent", false, "Go to the directory above" );
        options.addOption( "h", "help", false, "Help" );

        HelpFormatter formatter = new HelpFormatter();
        boolean flag = true;

        try {
            CommandLine line = parser.parse( options, args );

            if( line.hasOption( "h" ) ) {
                formatter.printHelp( args[0], options );
                flag = false;
            }
            if( line.hasOption( "p" ) ) {
                File f = new File (currentDirectory);
                if(f.getParentFile() != null){
                    currentDirectory = FilenameUtils.getFullPathNoEndSeparator(f.getAbsolutePath());
                }
            }
            else if(flag){
                List<String>  unparsedArgList = line.getArgList();
                changeCurrentDirectory(unparsedArgList.get(1));

            }

        }
        catch( ParseException exp ) {
            System.out.println( "Unexpected exception:" + exp.getMessage() );
        }
    }

    private void changeCurrentDirectory(String path){
        File f = new File (path);
        if(f.isAbsolute() && f.exists() && f.isDirectory()){
            currentDirectory = path;
        }
        else{
            File f2 = new File (currentDirectory + "//" + path);
            if(f2.exists() && f2.isDirectory()){
                currentDirectory = f2.getAbsolutePath();
            }
        }
    }


}
