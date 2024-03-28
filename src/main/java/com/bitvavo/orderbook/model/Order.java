
package com.bitvavo.orderbook.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Getter
public class Order {
    private String orderId;
    private OrderType side;
    private Integer price;
    @Setter
    private Integer quantity;

}
