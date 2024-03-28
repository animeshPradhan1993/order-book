package com.bitvavo.orderbook;

import com.bitvavo.orderbook.model.Order;
import com.bitvavo.orderbook.reader.OrderFileReader;
import com.bitvavo.orderbook.writer.OrderBookFileWriter;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderFileWriterTest {
    @Test
    public void testOrderList() throws IOException, NoSuchAlgorithmException {
        List<Order> orderList = OrderFileReader.createOrderListFromInputFile(new File("src/test/resources/test1.txt"));
        OrderBookFileWriter.writeToDestination(orderList,"src/test/resources/test_output.txt");
        assertEquals(TestUtils.getmD5("src/test/resources/test_output.txt"),"ce8e7e5ab26ab5a7db6b7d30759cf02e");
    }
}
