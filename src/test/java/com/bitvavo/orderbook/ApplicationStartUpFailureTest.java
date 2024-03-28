package com.bitvavo.orderbook;

import com.bitvavo.orderbook.config.DirectoryConfig;
import com.bitvavo.orderbook.config.ListenerInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class ApplicationStartUpFailureTest {
    DirectoryConfig directoryConfig;
    @BeforeEach
    public void init(){
        directoryConfig = new DirectoryConfig();
        directoryConfig.setInputDirectory("src/test/resources/inputDirectory");
        directoryConfig.setOutputDirectory("src/test/resources/inputDirectory");
    }

    @Test
    public void testFailureWhenSameDirectoriesConfigured(){
        ListenerInitializer listenerInitializer = new ListenerInitializer();
        listenerInitializer.setDirectoryConfig(directoryConfig);
        try {
        listenerInitializer.init();
        }
        catch (RuntimeException e) {
            assertEquals(e.getMessage(),"Please configure separate Input and Output directories.");
        }
    }
    @Test
    public void testFailureWhenInputDirectoryIsNotConfigured(){
        ListenerInitializer listenerInitializer = new ListenerInitializer();
        directoryConfig
                .setInputDirectory("");
        listenerInitializer.setDirectoryConfig(directoryConfig);
        try {
            listenerInitializer.init();
        }
        catch (RuntimeException e) {
            assertEquals(e.getMessage(),"Please configure an Input directory.");
        }
    }
    @Test
    public void testFailureWhenOutputDirectoryIsNotConfigured(){
        ListenerInitializer listenerInitializer = new ListenerInitializer();
        directoryConfig
                .setOutputDirectory("");
        listenerInitializer.setDirectoryConfig(directoryConfig);
        try {
            listenerInitializer.init();
        }
        catch (RuntimeException e) {
            assertEquals(e.getMessage(),"Please configure an Output directory.");
        }
    }

    @Test
    public void testFailureWhenOutputDirectoryIsFile(){
        ListenerInitializer listenerInitializer = new ListenerInitializer();
        directoryConfig
                .setOutputDirectory("src/test/resources/test_output.txt");
        listenerInitializer.setDirectoryConfig(directoryConfig);
        try {
            listenerInitializer.init();
        }
        catch (RuntimeException e) {
            assertEquals(e.getMessage(),"Incorrect configuration for Output directory, the path must point to an existing directory on file system.");
        }
    }
    @Test
    public void testFailureWhenInputDirectoryIsFile(){
        ListenerInitializer listenerInitializer = new ListenerInitializer();
        directoryConfig
                .setInputDirectory("src/test/resources/test_output.txt");
        listenerInitializer.setDirectoryConfig(directoryConfig);
        try {
            listenerInitializer.init();
        }
        catch (RuntimeException e) {
            assertEquals(e.getMessage(),"Incorrect configuration for Input directory, the path must point to an existing directory on file system.");
        }
    }
}
