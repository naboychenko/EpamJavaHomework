import javafx.scene.shape.Arc;
import org.apache.commons.cli.*;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.utils.IOUtils;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by Viktoria Naboychenko on 19.02.2018.
 */
public class Archiver {
    private Options options;

    public Archiver() {
        options = new Options();
        Option extract = Option.builder("x")
                .longOpt("extract")
                .desc("Extract files from archive")
                .hasArg()
                .argName("fileName")
                .build();
        options.addOption(extract);

        options.addOption("h", "help", false, "Help");
    }

    public void function(String currentDirectory, String[] args) {
        CommandLineParser parser = new DefaultParser();

        HelpFormatter formatter = new HelpFormatter();
        String destination = currentDirectory;

        try {
            CommandLine line = parser.parse(options, args);
            List<String> unparsedArgList = line.getArgList();
            if (line.hasOption("x")) {

                if (unparsedArgList.size() > 2) {
                    throw new ParseException("Too many arguments.");
                }

                String fileName = line.getOptionValue("x");
                fileName = getExistingFileName(fileName, currentDirectory);

                if (unparsedArgList.size() == 2)
                    destination = unparsedArgList.get(1);

                switch (unparsedArgList.get(0)) {
                    case "zip":
                        unpackZipArchive(fileName, destination);
                        break;
                    case "tar":
                        unpackTarArchive(fileName, destination);
                        break;
                }
            } else {
                String archiveType = unparsedArgList.get(0);
                unparsedArgList.remove(0);

                if (unparsedArgList.size() < 2)
                    throw new ParseException("Too few arguments.");
                destination = unparsedArgList.get(0);
                unparsedArgList.remove(0);
                destination = getNonExistentFileName(destination, currentDirectory);

                createArchive(createFileList(unparsedArgList, currentDirectory), new File(destination), archiveType);
            }
            if (line.hasOption("h")) {
                formatter.printHelp(args[0], options);
            }
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp(args[0], options);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (FileAlreadyExistsException e) {
            System.out.println(e.getMessage());
        }
    }

    private String getNonExistentFileName(String name, String currentDirectory) throws FileAlreadyExistsException {

        File f = new File(name);
        if (!f.isAbsolute())
            f = new File(currentDirectory + "\\" + name);
        if (!f.exists())
            return f.getAbsolutePath();
        throw new FileAlreadyExistsException("File " + name + " already exists.");
    }

    private String getExistingFileName(String name, String currentDirectory) throws FileNotFoundException {
        if (new File(name).exists())
            return name;
        if (new File(currentDirectory + "\\" + name).exists())
            return currentDirectory + "\\" + name;
        throw new FileNotFoundException("File " + name + " does not exist.");
    }

    private ArrayList<File> createFileList(List<String> fileNamesList, String currentDirectory) throws FileNotFoundException {
        ArrayList<File> fileList = new ArrayList<>();
        for (String fileName : fileNamesList) {
            fileList.add(new File(getExistingFileName(fileName, currentDirectory)));
        }
        return fileList;
    }

    private ArchiveEntry getArchiveEntry(String fileName, String archiveType, long size) {
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

    private ArchiveOutputStream getArchiveOutputStream(String archiveType, BufferedOutputStream bos) throws ArchiveException {

        switch (archiveType) {
            case "zip":
                return new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, bos);
            case "tar":
                return new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, bos);
        }
        throw new IllegalArgumentException("Unsupported archive type: '" + archiveType + "'");
    }

    private void createArchive(ArrayList<File> fileList, File destination, String archiveType) {
        try {

            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destination));
            ArchiveOutputStream output = getArchiveOutputStream(archiveType, bos);

            BufferedInputStream input;
            ArchiveEntry entry;

            for (File file : fileList) {

                entry = getArchiveEntry(file.getName(), archiveType, file.length());
                output.putArchiveEntry(entry);
                input = new BufferedInputStream(new FileInputStream(file));
                IOUtils.copy(input, output);
                input.close();
                output.closeArchiveEntry();
            }

            output.finish();
            bos.close();

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (ArchiveException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void unpackTarArchive(String archive, String targetDirectory) {
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(archive));
            ByteArrayInputStream bais;
            TarArchiveInputStream input = new TarArchiveInputStream(bis);
            TarArchiveEntry entry;
            OutputStream outputStream;
            long size;
            byte[] content;
            int offset = 0;

            while (true) {
                entry = input.getNextTarEntry();
                if (entry == null)
                    break;
                size = entry.getSize();
                content = new byte[(int) size];
                input.read(content, offset, content.length - offset);
                offset += (int) entry.getRealSize();
                bais = new ByteArrayInputStream(content);
                outputStream = new BufferedOutputStream(new FileOutputStream(new File(targetDirectory, entry.getName())));
                IOUtils.copy(bais, outputStream);
                outputStream.flush();
                outputStream.close();
            }
            input.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void unpackZipArchive(String archive, String targetDirectory) {

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

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
