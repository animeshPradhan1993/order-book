package com.bitvavo.orderbook;

import com.bitvavo.orderbook.model.Order;
import com.bitvavo.orderbook.reader.OrderFileReader;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderFileReaderTest {

    @Test
    public void testOrderList() throws FileNotFoundException {
        List<Order> orderList = OrderFileReader.createOrderListFromInputFile(new File("src/test/resources/test1.txt"));
        assertEquals(orderList.size(),7);
        assertEquals(orderList.get(0).getQuantity(),25500);
        assertEquals(orderList.get(6).getQuantity(),16000);
    }
}
