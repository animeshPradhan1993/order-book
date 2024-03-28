package com.bitvavo.orderbook.config;

import com.bitvavo.orderbook.event.listener.FileCreationListener;
import com.bitvavo.orderbook.observer.FileWatcher;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;

@Component
public class ListenerInitializer {

    @Autowired
    private FileCreationListener fileCreationListener;
    @Autowired
    private DirectoryConfig directoryConfig;
    private static Logger logger = LoggerFactory.getLogger(FileCreationListener.class);
    private static String INPUT = "Input";
    private static String OUTPUT = "Output";

    @PostConstruct
    void init() {
        checkEmptyDirectory(directoryConfig.getInputDirectory(), INPUT);
        checkEmptyDirectory(directoryConfig.getOutputDirectory(), OUTPUT);

        File inputDirectory = new File(directoryConfig.getInputDirectory());
        File outputDirectory = new File(directoryConfig.getOutputDirectory());
        checkIsDirectory(inputDirectory, INPUT);
        checkIsDirectory(outputDirectory, OUTPUT);

        if (outputDirectory.getAbsolutePath().equals(inputDirectory.getAbsolutePath())) {
            logger.error("Output directory  and input directory can not be same");
            throw new RuntimeException("Please configure separate input and output directories.");
        }
        FileWatcher watcher = new FileWatcher(inputDirectory);
        watcher.addListener(fileCreationListener).watch();
    }

    // this method checks whether config for directories is provided or not
    private void checkEmptyDirectory(String directory, String type) {
        if (StringUtils.isEmpty(directory)) {
            logger.error(type + " directory is not configured");
            throw new RuntimeException("Please configure an " + type + " directory.");
        }
    }

    // this method checks whether the given path points to a directory.
    private void checkIsDirectory(File file, String type) {
        if (!file.isDirectory()) {
            logger.error(type + " directory incorrectly configured.");
            throw new RuntimeException("Incorrect configuration for " + type + " directory, the path must point to an existing directory on file system.");
        }
    }
}

