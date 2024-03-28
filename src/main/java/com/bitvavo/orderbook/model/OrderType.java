package com.bitvavo.orderbook.model;

public enum OrderType {
    B("Buy"),
    S("Sell");
    public final String label;
     OrderType(String label) {
        this.label= label;
    }
}
