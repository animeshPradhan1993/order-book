
package com.bitvavo.orderbook.model;

import lombok.Getter;
import java.util.Comparator;
import java.util.PriorityQueue;

@Getter
public class OrderBook {
    private final PriorityQueue<Order> sellQueue = new PriorityQueue<>(Comparator.comparingInt(Order::getPrice));

    private final PriorityQueue<Order> buyQueue = new PriorityQueue<>((Order o1, Order o2) -> o2.getPrice() - o1.getPrice());


}

