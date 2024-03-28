package com.bitvavo.orderbook.writer;

import com.bitvavo.orderbook.model.Order;
import com.bitvavo.orderbook.model.OrderBook;
import com.bitvavo.orderbook.model.OrderType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.PriorityQueue;

public class OrderBookFileWriter {
    private static Logger logger = LoggerFactory.getLogger(OrderBookFileWriter.class);

    public static void writeToDestination(List<Order> orderList, String destination) {
        logger.info("Starting to write  to file: {}", destination);
        FileWriter myWriter = null;
        try {
            myWriter = new FileWriter(destination);
        } catch (IOException e) {
            logger.error("Error in creating a file writer for file: {} {}", destination, e);
            throw new RuntimeException(e);
        }
        OrderBook orderBook = queueAndProcessOrders(orderList, myWriter, destination);
        writeOrderBookToFile(myWriter, orderBook.getBuyQueue(), orderBook.getSellQueue(), destination);
        try {
            myWriter.close();
        } catch (IOException e) {
            logger.error("Error in closing file writer for file: {} {}", destination, e);
            throw new RuntimeException(e);
        }
        logger.info("Successfully written file: {}", destination);
    }

    private static void writeOrderBookToFile(FileWriter writer, PriorityQueue<Order> buyQueue, PriorityQueue<Order> sellQueue, String destination) {
        int reqLength = Math.max(buyQueue.size(), sellQueue.size());
        for (int i = 0; i < reqLength; i++) {
            Order buyOrder = buyQueue.poll();
            Order sellOrder = sellQueue.poll();
            String buyQuantity = buyOrder == null ? "" : formatQuantity(buyOrder.getQuantity());
            String buyPrice = buyOrder == null ? "" : buyOrder.getPrice().toString();
            String sellQuantity = sellOrder == null ? "" : formatQuantity(sellOrder.getQuantity());
            String sellPrice = sellOrder == null ? "" : sellOrder.getPrice().toString();
            logger.info("Writing pending orders in the Book to file: {}", destination);
            try {
                writer.write(fixedLengthString(buyQuantity, 11) + " " +
                        fixedLengthString(buyPrice, 6) + " | " +
                        fixedLengthString(sellPrice, 6) + " " + fixedLengthString(sellQuantity, 11));
                writer.write(System.lineSeparator());
                logger.info("Successfully Written Order Book to file: {}", destination);
            } catch (IOException e) {
                logger.error("Error in writing pending orders  to file: {} {}", destination, e);
                throw new RuntimeException(e);
            }
        }
    }

    private static OrderBook queueAndProcessOrders(List<Order> orderList, FileWriter writer, String destination)  {
        OrderBook orderBook = new OrderBook();
        for (Order order : orderList) {
            OrderType type = order.getSide();
            if (type == OrderType.S) {
                handleSellOrder(order, orderBook.getBuyQueue(), orderBook.getSellQueue(), writer, destination);
            } else {
                handleBuyOrder(order, orderBook.getBuyQueue(), orderBook.getSellQueue(), writer, destination);
            }
        }
        return orderBook;
    }

    private static void handleSellOrder(Order sellOrder, PriorityQueue<Order> buyQueue, PriorityQueue<Order> sellQueue, FileWriter writer, String destination) {
        Order buyOrder = buyQueue.peek();
        while (buyOrder != null && buyOrder.getPrice() >= sellOrder.getPrice() && sellOrder.getQuantity() > 0) {
            int buyQuantity = buyOrder.getQuantity();
            int sellQuantity = sellOrder.getQuantity();
            if (buyQuantity > sellQuantity) {
                buyOrder.setQuantity(buyQuantity - sellQuantity);
                logger.info("Executing trade for orderId: {}", sellOrder.getOrderId());
                writeTradesToFile(writer, sellOrder, buyOrder, buyOrder.getPrice(), sellQuantity, destination);
                return;
            } else {
                sellOrder.setQuantity(sellQuantity - buyQuantity);
                logger.info("Executing trade for orderId: {}", sellOrder.getOrderId());
                writeTradesToFile(writer, sellOrder, buyOrder, buyOrder.getPrice(), buyQuantity, destination);
                buyQueue.poll();
                buyOrder = buyQueue.peek();
            }
        }
        if (sellOrder.getQuantity() > 0) {
            sellQueue.offer(sellOrder);
        }
    }

    private static void handleBuyOrder(Order buyOrder, PriorityQueue<Order> buyQueue, PriorityQueue<Order> sellQueue, FileWriter writer, String destination) {
        Order sellOrder = sellQueue.peek();
        while (sellOrder != null && buyOrder.getPrice() >= sellOrder.getPrice() && buyOrder.getQuantity() > 0) {
            int buyQuantity = buyOrder.getQuantity();
            int sellQuantity = sellOrder.getQuantity();
            if (sellQuantity > buyQuantity) {
                sellOrder.setQuantity(sellQuantity - buyQuantity);
                logger.info("Executing trade for orderId: {}", buyOrder.getOrderId());
                writeTradesToFile(writer, buyOrder, sellOrder, sellOrder.getPrice(), buyQuantity, destination);
                return;
            } else {
                buyOrder.setQuantity(buyQuantity - sellQuantity);
                logger.info("Executing trade for orderId: {}", buyOrder.getOrderId());
                writeTradesToFile(writer, buyOrder, sellOrder, sellOrder.getPrice(), sellQuantity, destination);
                sellQueue.poll();
                sellOrder = sellQueue.peek();
            }
        }
        if (buyOrder.getQuantity() > 0) {
            buyQueue.offer(buyOrder);
        }
    }

    private static void writeTradesToFile(FileWriter writer, Order aggressiveOrder, Order restingOrder, Integer price, Integer quantity, String destination) {
        try {
            writer.write("trade " + aggressiveOrder.getOrderId() + "," + restingOrder.getOrderId() + "," + price + "," + quantity);
            writer.write(System.lineSeparator());
        } catch (IOException e) {
            logger.error("Error in writing trade  to file: {}  orderId: {} {}", destination, aggressiveOrder.getOrderId(), e);
            throw new RuntimeException(e);
        }

    }

    private static String formatQuantity(Integer quantity) {
        return NumberFormat.getNumberInstance(Locale.US).format(quantity);
    }

    private static String fixedLengthString(String string, int length) {
        return String.format("%1$" + length + "s", string);
    }
}