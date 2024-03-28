package com.bitvavo.orderbook;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT, properties = "spring.config.location=src/test/resources/")
public class EndToEndTest {
    @BeforeEach
    @AfterEach
    public void clean() throws IOException, InterruptedException {
        TestUtils.deleteFromFolder("src/test/resources/inputDirectory");
        TestUtils.deleteFromFolder("src/test/resources/outputDirectory");
        Thread.sleep(2000);
    }


    @ParameterizedTest
    @CsvFileSource(resources = "/csvSources/testSource.csv", numLinesToSkip = 0)
    public void E2ETests(String fileName,String outputFileName, String expectedHash) throws IOException, NoSuchAlgorithmException, InterruptedException {
        moveFileToFolder(fileName);
        Thread.sleep(9500);
        assertEquals(expectedHash, TestUtils.getmD5("src/test/resources/outputDirectory/"+outputFileName));

    }

    private void moveFileToFolder(String filename) throws IOException {
        File file = new File("src/test/resources/" + filename);
        Path copied = Paths.get("src/test/resources/inputDirectory/" + filename);
        Path originalPath = file.toPath();
        Files.copy(originalPath, copied, StandardCopyOption.REPLACE_EXISTING);
    }


}
