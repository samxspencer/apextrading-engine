package com.apextrade.orderbook;

import com.apextrade.model.*;

import java.math.BigDecimal;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import com.apextrade.model.OrderStatus;

public class OrderBook {

    private final String symbol;

    private final PriorityQueue<Order> bids;
    private final PriorityQueue<Order> asks;

    public OrderBook(String symbol) {
        this.symbol = symbol;

        this.bids = new PriorityQueue<>(
                Comparator.comparing(Order::getPrice).reversed()
                        .thenComparing(Order::getTimestamp)
        );

        this.asks = new PriorityQueue<>(
                Comparator.comparing(Order::getPrice)
                        .thenComparing(Order::getTimestamp)
        );
    }

    public synchronized List<Trade> submitOrder(Order order) {

        List<Trade> trades = new ArrayList<>();

        if (order.getSide() == OrderSide.BUY) {
            matchBuyOrder(order, trades);
            if (order.getRemainingQuantity() > 0 &&
                order.getType() == OrderType.LIMIT) {
                bids.add(order);
            }
        } else {
            matchSellOrder(order, trades);
            if (order.getRemainingQuantity() > 0 &&
                order.getType() == OrderType.LIMIT) {
                asks.add(order);
            }
        }

        return trades;
    }

    private void matchBuyOrder(Order buyOrder, List<Trade> trades){

        while (!asks.isEmpty()
                && buyOrder.getRemainingQuantity() > 0) {

            Order bestAsk = asks.peek();

            if (buyOrder.getType() == OrderType.LIMIT &&
                buyOrder.getPrice().compareTo(bestAsk.getPrice()) < 0) {
                break;
            }

            Trade trade = executeTrade(buyOrder, bestAsk);
            trades.add(trade);

            if (bestAsk.getRemainingQuantity() == 0) {
                asks.poll();
            }
        }
    }

    private void matchSellOrder(Order sellOrder, List<Trade> trades){

        while (!bids.isEmpty()
                && sellOrder.getRemainingQuantity() > 0) {

            Order bestBid = bids.peek();

            if (sellOrder.getType() == OrderType.LIMIT &&
                sellOrder.getPrice().compareTo(bestBid.getPrice()) > 0) {
                break;
            }

            Trade trade = executeTrade(bestBid, sellOrder);
            trades.add(trade);

            if (bestBid.getRemainingQuantity() == 0) {
                bids.poll();
            }
        }
    }

    private Trade executeTrade(Order buy, Order sell) {

        long executedQty = Math.min(
                buy.getRemainingQuantity(),
                sell.getRemainingQuantity()
        );

        buy.fill(executedQty);
        sell.fill(executedQty);

        return new Trade(
                buy.getId(),
                sell.getId(),
                symbol,
                sell.getPrice(),
                executedQty,
                System.currentTimeMillis()
        );
    }

    public void printBook() {

    System.out.println("----- ORDER BOOK " + symbol + " -----");

    System.out.println("ASKS:");
    asks.stream()
        .sorted(Comparator.comparing(Order::getPrice))
        .forEach(o ->
            System.out.println(
                o.getPrice() + " x " + o.getRemainingQuantity()
            )
        );

    System.out.println("BIDS:");
    bids.stream()
        .sorted(Comparator.comparing(Order::getPrice).reversed())
        .forEach(o ->
            System.out.println(
                o.getPrice() + " x " + o.getRemainingQuantity()
            )
        );
    } 
    
    public synchronized boolean cancelOrder(long orderId) {

        // Try remove from bids
        for (Order order : bids) {
            if (order.getId() == orderId &&
                order.getStatus() != OrderStatus.FILLED) {

                bids.remove(order);
                order.cancel();
                return true;
            }
        }

        // Try remove from asks
        for (Order order : asks) {
            if (order.getId() == orderId &&
                order.getStatus() != OrderStatus.FILLED) {

                asks.remove(order);
                order.cancel();
                return true;
            }
        }

        return false;
    }
}