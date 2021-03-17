import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.max.simple.SimpleArchive;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by maxxii on 15.03.2021.
 */
class SimpleArchiveTest {

    private final SimpleArchive simpleArchive = new SimpleArchive();
    private final String pathForZipFiles = "." + File.separator + "src" + File.separator + "test" + File.separator + "resources"
            + File.separator + "actualdata" + File.separator + "testing2";
    private final String unzippingFilesPath = "." + File.separator + "unzipFiles";


    //проверяем действительно ли метод пропускает параметр !zip и !unzip, т.к этот метод должен возвращать файлы с указанными путями
    @Test
    void listFiles() {
        String[] filePath = {"!zip", "filePath.file", "dirPath", "!unzip"};
        List<File> list = simpleArchive.listFiles(filePath);
        Assertions.assertEquals(2, list.size());
        Assertions.assertEquals("filePath.file", list.get(0).getName());
        Assertions.assertEquals("dirPath", list.get(1).getName());
    }

    //архивируем заранее подготовленные файлы и сравниваем размер заархивированного архива с архивом подготовленным для тестирования
    @Test
    void zippingTest() throws IOException {
        simpleArchive.zipFile(simpleArchive.listFiles(pathForZipFiles));
        long actualSize = Files.size(Paths.get("default.zip"));
        long expectedSize = Files.size(Paths.get("zip_for_testing.zip"));
        Assertions.assertEquals(expectedSize, actualSize);
    }

    //распаковываем заархивированный архив, и сравниваем результаты с файлами, которые необходимо было заархивировать
    @Test
    void unzippingTest() throws IOException {
        simpleArchive.unZipFile("default.zip");

        List<Path> actualData = Files.walk(Paths.get(unzippingFilesPath))
                .filter(path -> Files.isRegularFile(path))
                .map(Path::getFileName)
                .collect(Collectors.toList());

        List<Path> expectedData = Files.walk(Paths.get(pathForZipFiles))
                .filter(path -> Files.isRegularFile(path))
                .map(Path::getFileName)
                .collect(Collectors.toList());

        System.out.println("Actual: " + actualData);
        System.out.println("Expected: " + expectedData);

        Assertions.assertEquals(expectedData, actualData);
    }


}