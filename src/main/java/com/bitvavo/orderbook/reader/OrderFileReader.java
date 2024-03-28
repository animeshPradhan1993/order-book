package com.bitvavo.orderbook.reader;

import com.bitvavo.orderbook.model.Order;
import com.bitvavo.orderbook.model.OrderType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public  class OrderFileReader {
    private static Logger logger = LoggerFactory.getLogger(OrderFileReader.class);
    public static List<Order> createOrderListFromInputFile(File file) {

        logger.info("Starting to read file: {}", file.getAbsolutePath());
        Scanner input = null;
        try {
            input = new Scanner(file);
        } catch (FileNotFoundException e) {
            logger.error(" File not found at: {} {}", file.getAbsolutePath(),e);
            throw new RuntimeException(e);
        }
        input.useDelimiter("-|\n");
        List<Order> orderList = new ArrayList<>();
        while (input.hasNext()) {
            String line = input.next();
            String[] arr = line.split(",");
            String id = arr[0];
            OrderType side = OrderType.valueOf(arr[1]);
            Integer price = Integer.parseInt(arr[2]);
            Integer quantity = Integer.parseInt(arr[3]);
            Order order = new Order(id, side, price, quantity);
            orderList.add(order);
        }
        logger.info("Successfully Read: {}", file.getAbsolutePath());
        return orderList;
    }
}
