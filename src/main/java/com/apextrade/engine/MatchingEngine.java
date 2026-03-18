package com.apextrade.engine;

import com.apextrade.model.Order;
import com.apextrade.model.Trade;
import com.apextrade.orderbook.OrderBook;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class MatchingEngine {

    private final ConcurrentHashMap<String, OrderBook> books =
            new ConcurrentHashMap<>();

    private final AtomicLong orderIdGenerator = new AtomicLong(1);

    public long nextOrderId() {
        return orderIdGenerator.getAndIncrement();
    }

    public List<Trade> submitOrder(Order order) {

        OrderValidator.validate(order);

        OrderBook book = books.computeIfAbsent(
                order.getSymbol(),
                OrderBook::new
        );

        return book.submitOrder(order);
    }

    public OrderBook getOrderBook(String symbol) {
        return books.get(symbol);
    }

    public boolean cancelOrder(String symbol, long orderId) {

        OrderBook book = books.get(symbol);

        if (book == null) {
            return false;
        }

        return book.cancelOrder(orderId);
    }
}