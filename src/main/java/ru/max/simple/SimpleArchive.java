package ru.max.simple;

import ru.max.simple.service.ZipManager;

import java.io.*;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Class for archiving and unpacking files
 */
public class SimpleArchive implements ZipManager {
    /**
     * The field that stores the path where the files will be unpacked
     */
    private static final String DEST_UNZIP_PATH = "." + File.separator + "unzipFiles";
    /**
     * Archive name
     */
    private static final String ZIP_NAME = "default.zip";
    /**
     * Buffer size for reading and writing.
     */
    private static final int BUFFER_SIZE = 1024;

    /**
     * Method from file paths, returns a list of files
     *
     * @param paths Accepts parameters that are passed when the application starts
     * @return list of files, excluding parameters !zip and !unzip
     */
    public List<File> listFiles(String... paths) {
        return Stream.of(paths).filter(s -> !s.startsWith("!zip") && !s.startsWith("!unzip"))
                .map(File::new)
                .collect(Collectors.toList());
    }

    /**
     * The method writes all files to a zip archive
     *
     * @param files Accepts a list of files to archive
     * @throws IOException if it cannot find or create a file or directory
     */
    @Override
    public void zipFile(List<File> files) throws IOException {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(ZIP_NAME))) {
            for (File f : files) {
                zip(f, f.getName(), zos);
            }
        }
    }


    private void zip(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (!fileToZip.exists()) {
            throw new FileNotFoundException("File with name: " + fileToZip.getName() + " not found");
        }
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {//если директория, создаем директорию
            if (fileName.endsWith(File.separator)) {
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName + File.separator));
                zipOut.closeEntry();
            }
            File[] children = fileToZip.listFiles();//рекурсивно пробегаемся по директории смотрим что там есть
            for (File childFile : children) {
                zip(childFile, fileName + File.separator + childFile.getName(), zipOut);
            }
            return;
        }
        try (FileInputStream fis = new FileInputStream(fileToZip)) {//иначе создаем файл и записываем содержимое исходного файла
            ZipEntry zipEntry = new ZipEntry(fileName);
            zipOut.putNextEntry(zipEntry);
            byte[] bytes = new byte[BUFFER_SIZE];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
        }

    }

    /**
     * The method creates files and directories in the same hierarchy
     * as in the zip archive, and writes the read data to them,
     * files are unpacked to the path specified in the field DEST_UNZIP_PATH
     *
     * @param fileZip accepts path to .zip archive
     * @throws IOException if the file is not found, or the directory or file cannot be created
     */
    @Override
    public void unZipFile(String fileZip) throws IOException {
        ZipFile zipFile = new ZipFile(fileZip);
        Enumeration<?> enu = zipFile.entries();
        while (enu.hasMoreElements()) {
            ZipEntry zipEntry = (ZipEntry) enu.nextElement();

            String name = zipEntry.getName();

            // создаем директорию если это директория
            File file = new File(DEST_UNZIP_PATH, name);
            if (name.endsWith(File.separator)) {
                file.mkdirs();
                continue;
            }

            File parent = file.getParentFile();
            if (parent != null) {
                parent.mkdirs();
            }

            // считываем и записываем в созданный файл данные
            try (InputStream is = zipFile.getInputStream(zipEntry);
                 FileOutputStream fos = new FileOutputStream(file)) {
                byte[] bytes = new byte[BUFFER_SIZE];
                int length;
                while ((length = is.read(bytes)) >= 0) {
                    fos.write(bytes, 0, length);
                }
            }

        }
    }
}
