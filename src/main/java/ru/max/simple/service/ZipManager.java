package ru.max.simple.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * This interface allocates methods to implement behavior
 * archiving and unpacking
 */
public interface ZipManager {

    void zipFile(List<File> files) throws IOException;

    void unZipFile(String zipFile) throws IOException;

}
