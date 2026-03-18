package com.apextrade.model;

import java.math.BigDecimal;

public class Trade {

    private final long buyOrderId;
    private final long sellOrderId;
    private final String symbol;
    private final BigDecimal price;
    private final long quantity;
    private final long timestamp;

    public Trade(long buyOrderId,
                 long sellOrderId,
                 String symbol,
                 BigDecimal price,
                 long quantity,
                 long timestamp) {

        this.buyOrderId = buyOrderId;
        this.sellOrderId = sellOrderId;
        this.symbol = symbol;
        this.price = price;
        this.quantity = quantity;
        this.timestamp = timestamp;
    }

    public long getBuyOrderId() {
        return buyOrderId;
    }

    public long getSellOrderId() {
        return sellOrderId;
    }

    public String getSymbol() {
        return symbol;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public long getQuantity() {
        return quantity;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "TRADE: " + symbol +
                " Price=" + price +
                " Qty=" + quantity +
                " BuyOrder=" + buyOrderId +
                " SellOrder=" + sellOrderId;
    }
}