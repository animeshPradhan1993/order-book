package com.bitvavo.orderbook.event.listener;

import com.bitvavo.orderbook.config.DirectoryConfig;
import com.bitvavo.orderbook.event.model.FileEvent;
import com.bitvavo.orderbook.model.Order;
import com.bitvavo.orderbook.reader.OrderFileReader;
import com.bitvavo.orderbook.executor.OrderExecutor;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.List;

@Component
public class FileCreationListener implements FileListener {
    @Autowired
    private DirectoryConfig directoryConfig;
    private String directoryAbsolutePath;

    private static Logger logger = LoggerFactory.getLogger(FileCreationListener.class);


    @PostConstruct
    void init() {
        File outputDirectory = new File(directoryConfig.getOutputDirectory());
        directoryAbsolutePath = outputDirectory.getAbsolutePath();
    }

   /*This method handles the file creation
   event and processes the input file.*/
    @Override
    public void onCreated(FileEvent event) {
        logger.info("Received event to read file at: {}", event.getFile().getAbsolutePath());
        File file = event.getFile();
        List<Order> orderList = OrderFileReader.createOrderListFromInputFile(file);
        if (!CollectionUtils.isEmpty(orderList)) {
        String outputFileName = "/output_" + file.getName();
        OrderExecutor.writeToDestination(orderList, directoryAbsolutePath + outputFileName);}
    }

}
