package com.bitvavo.orderbook;

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
    @Value("${orderBook.inputDirectory}")
    private String inputDirectoryLocation;
    private static Logger logger = LoggerFactory.getLogger(FileCreationListener.class);
    @PostConstruct
    void init() {
        if (!StringUtils.isEmpty(inputDirectoryLocation)) {
            File inputDirectory = new File(inputDirectoryLocation);

            if (!inputDirectory.isDirectory()) {
                logger.error("Input directory incorrectly configured");

                throw new RuntimeException("Incorrect configuration for input directory the path must point to an existing directory on file system");
            }
            FileWatcher watcher = new FileWatcher(inputDirectory);
            watcher.addListener(fileCreationListener).watch();
        } else {
            logger.error("Input directory is not configured");
            throw new RuntimeException("Please configure an output directory.");
        }
    }

}
