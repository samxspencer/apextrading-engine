package com.apextrade.model;

import java.math.BigDecimal;

public class Order {

    private final long id;
    private final long userId;

    private final String symbol;
    private final Market market;
    private final OrderSide side;
    private final OrderType type;
    private final TimeInForce timeInForce;

    private final BigDecimal price;
    private final long quantity;

    private long filledQuantity;
    private OrderStatus status = OrderStatus.NEW;

    private final long timestamp; // nanoTime for price-time priority

    public Order(long id,
                 long userId,
                 String symbol,
                 Market market,
                 OrderSide side,
                 OrderType type,
                 TimeInForce timeInForce,
                 BigDecimal price,
                 long quantity) {

        this.id = id;
        this.userId = userId;
        this.symbol = symbol;
        this.market = market;
        this.side = side;
        this.type = type;
        this.timeInForce = timeInForce;
        this.price = price;
        this.quantity = quantity;

        this.filledQuantity = 0;
        this.status = OrderStatus.NEW;
        this.timestamp = System.nanoTime();
    }

    public long getId() { return id; }
    public long getUserId() { return userId; }
    public String getSymbol() { return symbol; }
    public Market getMarket() { return market; }
    public OrderSide getSide() { return side; }
    public OrderType getType() { return type; }
    public TimeInForce getTimeInForce() { return timeInForce; }
    public BigDecimal getPrice() { return price; }
    public long getQuantity() { return quantity; }
    public long getFilledQuantity() { return filledQuantity; }
    public OrderStatus getStatus() { return status; }
    public long getTimestamp() { return timestamp; }

    public long getRemainingQuantity() {
        return quantity - filledQuantity;
    }

    public void fill(long quantity) {

        if (status == OrderStatus.CANCELLED ||
            status == OrderStatus.REJECTED ||
            status == OrderStatus.FILLED) {
            throw new IllegalStateException("Cannot fill order in status: " + status);
        }

        this.filledQuantity += quantity;

        if (this.filledQuantity == this.quantity) {
            this.status = OrderStatus.FILLED;
        } else if (this.filledQuantity > 0) {
            this.status = OrderStatus.PARTIALLY_FILLED;
        }
    }

    public void cancel() {

        if (status == OrderStatus.FILLED) {
            throw new IllegalStateException("Cannot cancel FILLED order");
        }

        if (status == OrderStatus.CANCELLED) {
            return;
        }

        this.status = OrderStatus.CANCELLED;
    }

    public void reject() {
        status = OrderStatus.REJECTED;
    }
}