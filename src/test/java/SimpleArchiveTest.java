import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.max.simple.SimpleArchive;

import java.io.File;
import java.util.List;

/**
 * Created by maxxii on 15.03.2021.
 */
class SimpleArchiveTest {

    private final SimpleArchive simpleArchive = new SimpleArchive();


    @Test
    void listFiles() {
        String[] filePath = {"!zip","filePath.file","dirPath"};
        List<File> list = simpleArchive.listFiles(filePath);
        Assertions.assertEquals(2,list.size());
        Assertions.assertEquals("filePath.file",list.get(0).getName());
        Assertions.assertEquals("dirPath",list.get(1).getName());
    }

    @Test
    void zipFile() {
        //todo
    }

    @Test
    void unZipFile() {
        //todo
    }
}