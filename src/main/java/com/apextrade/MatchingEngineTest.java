package com.apextrade;

import com.apextrade.engine.MatchingEngine;
import com.apextrade.model.*;

import java.math.BigDecimal;
import java.util.List;

public class MatchingEngineTest {

    public static void main(String[] args) {

        MatchingEngine engine = new MatchingEngine();

        long sellId = engine.nextOrderId();
        long buyId = engine.nextOrderId();

        Order sell1 = new Order(
                sellId,
                1001L,
                "AAPL",
                Market.US,
                OrderSide.SELL,
                OrderType.LIMIT,
                TimeInForce.DAY,
                new BigDecimal("150.00"),
                100L
        );

        Order buy1 = new Order(
                buyId,
                1002L,
                "AAPL",
                Market.US,
                OrderSide.BUY,
                OrderType.LIMIT,
                TimeInForce.DAY,
                new BigDecimal("151.00"),
                50L
        );

        // Submit SELL first (no match yet)
        List<Trade> trades1 = engine.submitOrder(sell1);
        trades1.forEach(System.out::println);

        // Submit BUY (should match 50)
        List<Trade> trades2 = engine.submitOrder(buy1);
        trades2.forEach(System.out::println);

        // Print remaining book
        engine.getOrderBook("AAPL").printBook();

        System.out.println("\nCancelling remaining sell order...");

        boolean cancelled = engine.cancelOrder("AAPL", sell1.getId());

        System.out.println("Cancelled: " + cancelled);
        System.out.println("Sell Order Status After Cancel: " + sell1.getStatus());

        engine.getOrderBook("AAPL").printBook();
    }
}