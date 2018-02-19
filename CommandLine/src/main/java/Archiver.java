import org.apache.commons.cli.*;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Viktoria Naboychenko on 19.02.2018.
 */
public class Archiver {
    public static void function(String currentDirectory, String[] args){
        CommandLineParser parser = new DefaultParser();

        Options options = new Options();
        Option format = Option.builder("f")
                .longOpt( "format" )
                .desc( "archive format"  )
                .hasArg()
                .argName( "format" )
                .build();
        options.addOption(format);
        options.addOption( "h", "help", false, "Help" );

        HelpFormatter formatter = new HelpFormatter();

        try {
            org.apache.commons.cli.CommandLine line = parser.parse( options, args );

            if( line.hasOption( "f" ) ) {
                String formatValue = line.getOptionValue( "f" );
                List<String>  unparsedArgList = line.getArgList();
                toZip();

            }
            else{

            }
            if( line.hasOption( "h" ) ) {
                formatter.printHelp( args[0], options );
            }
        }
        catch( ParseException exp ) {
            System.out.println( "Unexpected exception:" + exp.getMessage() );
            formatter.printHelp( args[0], options );
        }
    }

    private static String checkPath(String name, String currentDirectory){
        File f = new File(name);

        if(f.isAbsolute() && f.exists()){
            return name;
        }
        else{
            File f2 = new File (currentDirectory + "//" + name);
            if(f2.exists()){
                return f2.getAbsolutePath();
            }
        }
        return null;
    }


    private static void toZip() {


        /*OutputStream archiveStream = new FileOutputStream(dst);
        ArchiveOutputStream archive = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, archiveStream);


            //String entryName = getEntryName(source, file);
        ZipArchiveEntry entry = new ZipArchiveEntry(destination);
        archive.putArchiveEntry(entry);

        BufferedInputStream input = new BufferedInputStream(new FileInputStream(src));

        archive.closeArchiveEntry();

        archive.finish();
        archiveStream.close();*/
    }

}
