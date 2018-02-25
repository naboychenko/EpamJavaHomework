import org.apache.commons.cli.*;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Viktoria Naboychenko on 19.02.2018.
 */
public class Archiver {
    public static void function(String currentDirectory, String[] args){
        CommandLineParser parser = new DefaultParser();

        Options options = new Options();
        Option extract = Option.builder("x")
                .longOpt( "extract" )
                .desc( "Extract files from archive"  )
                .hasArg()
                .argName( "fileName" )
                .build();
        options.addOption(extract);

        options.addOption("h", "help", false, "Help");

        HelpFormatter formatter = new HelpFormatter();
        String destination = currentDirectory;

        try {
            org.apache.commons.cli.CommandLine line = parser.parse( options, args );
            List<String>  unparsedArgList = line.getArgList();
            if(line.hasOption("x")) {

                if(unparsedArgList.size() > 2){
                    throw new ParseException("Too many arguments.");
                }

                String fileName = line.getOptionValue("x");
                fileName = getExistingFileName (fileName, currentDirectory);

                if(unparsedArgList.size() == 2)
                    destination = unparsedArgList.get(1);

                switch(unparsedArgList.get(0)){
                    case "zip":
                        unpackZipArchive(fileName, destination);
                        break;
                    case "tar":
                        unpackTarArchive(fileName, destination);
                        break;
                }
            }
            else
            {
                String archiveType = unparsedArgList.get(0);
                unparsedArgList.remove(0);

                if(unparsedArgList.size() < 2)
                    throw new ParseException("Too few arguments.");
                destination = unparsedArgList.get(0);
                unparsedArgList.remove(0);
                destination = getNotExistingFileName(destination, currentDirectory);

                createArchive(createFileList(unparsedArgList, currentDirectory), new File(destination), archiveType);
            }
            if( line.hasOption( "h" ) ) {
                formatter.printHelp( args[0], options );
            }
        }
        catch(ParseException e) {
            System.out.println( "Unexpected exception:" + e.getMessage() );
            formatter.printHelp( args[0], options );
        }
        catch (FileNotFoundException e){
            System.out.println(e.getMessage());
        }
        catch (FileAlreadyExistsException e){
            System.out.println(e.getMessage());
        }
    }

    private static String getNotExistingFileName (String name, String currentDirectory)throws FileAlreadyExistsException{

        File f = new File(name);
        if(!f.isAbsolute());
            f = new File(currentDirectory + "\\" + name);
        //String path = currentDirectory + "\\" + name;

        // NTFS standard path
        //Pattern p = Pattern.compile("^[a-zA-Z]:\\(((?![<>:\"/\\|?*]).)+((?<![ .])\\)?)*$");
        //Matcher m = p.matcher(name);
        //if(m.matches())
        //    path = name;

        if(!f.exists())
            return f.getAbsolutePath();
        throw new FileAlreadyExistsException("File " + name + " already exists.");

    }
    private static String getExistingFileName (String name, String currentDirectory)throws FileNotFoundException{
        if(new File(name).exists())
            return name;
        if(new File(currentDirectory + "\\" + name).exists())
            return currentDirectory + "\\" + name;
        throw new FileNotFoundException("File " + name + " does not exist.");

    }

    private static ArrayList<File> createFileList(List<String> fileNamesList, String currentDirectory) throws FileNotFoundException{
        ArrayList<File> fileList = new ArrayList<>();
        for(String fileName : fileNamesList){
            fileList.add(new File(getExistingFileName(fileName, currentDirectory)));
        }
        return fileList;
    }

    private static ArchiveEntry getArchiveEntry(String fileName, String archiveType, long size) {
        switch (archiveType) {
            case "zip":
                ZipArchiveEntry zipEntry = new ZipArchiveEntry(fileName);
                zipEntry.setSize(size);
                return zipEntry;
            case "tar":
                TarArchiveEntry tarEntry = new TarArchiveEntry(fileName);
                tarEntry.setSize(size);
                return tarEntry;
        }
        throw new IllegalArgumentException("Unsupported archive type: '" + archiveType + "'");
    }

    private static ArchiveOutputStream getArchiveOutputStream(String archiveType, BufferedOutputStream bos) throws ArchiveException{

        switch (archiveType) {
            case "zip":
                return new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, bos);
            case "tar":
                return new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, bos);
        }
        throw new IllegalArgumentException("Unsupported archive type: '" + archiveType + "'");
    }

    private static void createArchive(ArrayList<File> fileList, File destination, String archiveType){
        try {

            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destination));
            ArchiveOutputStream output = getArchiveOutputStream(archiveType, bos);

            BufferedInputStream input;
            ArchiveEntry entry;

            for(File file : fileList){

                entry = getArchiveEntry(file.getName(), archiveType, file.length());
                output.putArchiveEntry(entry);
                input = new BufferedInputStream(new FileInputStream(file));
                IOUtils.copy(input, output);
                input.close();
                output.closeArchiveEntry();
            }

            output.finish();
            bos.close();

        }catch(FileNotFoundException e){
            System.out.println(e.getMessage());
        }catch (ArchiveException e){
            System.out.println(e.getMessage());
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    private static void unpackTarArchive(String archive, String targetDirectory){
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(archive));
            ByteArrayInputStream bais;
            //ArchiveInputStream input = new ArchiveStreamFactory().createArchiveInputStream( bis);
            TarArchiveInputStream input = new TarArchiveInputStream(bis);
            TarArchiveEntry entry;
            OutputStream outputStream;
            long size;
            byte[] content;
            int offset = 0;

            while(true){
                entry = input.getNextTarEntry();
                if(entry == null)
                    break;
                size = entry.getSize();
                //System.out.println(size);
                content = new byte[(int)size];
                input.read(content, offset, content.length - offset);
                offset += (int)entry.getRealSize();
                bais = new ByteArrayInputStream(content);
                outputStream = new BufferedOutputStream(new FileOutputStream(new File(targetDirectory, entry.getName())));
                IOUtils.copy(bais, outputStream);
                outputStream.flush();
                outputStream.close();
            }
            input.close();
        }catch(FileNotFoundException e){
            System.out.println(e.getMessage());
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    private static void unpackZipArchive(String archive, String targetDirectory){

        try {

            ZipFile zipFile = new ZipFile(new File(archive));
            Enumeration<ZipArchiveEntry> entries = zipFile.getEntries();
            ZipArchiveEntry entry;
            File file;

            while (entries.hasMoreElements()) {
                entry = entries.nextElement();
                file = new File(targetDirectory, entry.getName());
                if (entry.isDirectory()) {
                    file.mkdir();
                    continue;
                }
                try (OutputStream outputStream =
                             new BufferedOutputStream(new FileOutputStream(file))) {
                    IOUtils.copy(zipFile.getInputStream(entry), outputStream);
                }
            }

        }catch(FileNotFoundException e){
            System.out.println(e.getMessage());
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

/*    public static void main(String[] args) {
        /*String f1 = "E:\\TestDir\\file1.txt";
        String f2 = "E:\\TestDir\\file2.txt";
        String f3 = "E:\\TestDir\\ar.tar";
        ArrayList<File> list = new ArrayList<>();
        list.add(new File(f1));
        list.add(new File(f2));
        createArchive(list, new File(f3), "tar");
        unpackTarArchive("E:\\TestDir\\ar.tar","E:\\TestDir\\Dir2");
        //zip files.zip file1.txt file2.txt
    }*/

}
