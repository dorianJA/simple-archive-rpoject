package ru.max.simple;

import ru.max.simple.service.ZipManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.List;

/**
 *
 */
public class Main {

    /**
     * @param args For the program to work, you must specify the launch parameters
     *             !zip paths to files or directories that need to be archived separated by a space
     *             !unzip for unpacking, the path to the zipped file separated by a space
     */
    public static void main(String[] args) {
        ZipManager zipManager = new SimpleArchive();

        if (args.length > 1) {
            try {
                if (args[0].equals("!zip")) {
                    List<File> list = ((SimpleArchive) zipManager).listFiles(args);
                    zipManager.zipFile(list);
                    System.out.println("Archiving completed successfully!");
                } else if (args[0].equals("!unzip")) {
                    try {
                        zipManager.unZipFile(args[1]);
                        System.out.println("The files are unpacked!");
                    } catch (NoSuchFileException e) {
                        System.out.println("File: " + e.getMessage() + " not found");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Parameters not set!");
        }


    }


}
