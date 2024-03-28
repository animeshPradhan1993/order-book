package com.bitvavo.orderbook.event.listener;
import com.bitvavo.orderbook.event.model.FileEvent;
import com.bitvavo.orderbook.model.Order;
import com.bitvavo.orderbook.reader.OrderFileReader;
import com.bitvavo.orderbook.writer.OrderBookFileWriter;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.List;

@Component
public class FileCreationListener implements FileListener {
    @Value("${orderBook.outputDirectory}")
    private String outputDirectoryLocation;
    private String directoryAbsolutePath;

    private static Logger logger = LoggerFactory.getLogger(FileCreationListener.class);


    @PostConstruct
    void init() {
        if (!StringUtils.isEmpty(outputDirectoryLocation)) {
            File outputDirectory = new File(outputDirectoryLocation);
            if (!outputDirectory.isDirectory()) {
                logger.error("Output directory incorrectly configured");
                throw new RuntimeException("Incorrect configuration for output directory the path must point to an existing directory on file system.");
            }
            directoryAbsolutePath = outputDirectory.getAbsolutePath();
        } else {
            logger.error("Output directory is not configured");
            throw new RuntimeException("Please configure an output directory.");
        }
    }

    @Override
    public void onCreated(FileEvent event)  {
        logger.info("Received event to read file at: {}", event.getFile().getAbsolutePath());
        File file = event.getFile();
        List<Order> orderList = OrderFileReader.createOrderListFromInputFile(file);
        String outputFileName = "/output_" + file.getName();
        OrderBookFileWriter.writeToDestination(orderList,directoryAbsolutePath + outputFileName);
    }

}
