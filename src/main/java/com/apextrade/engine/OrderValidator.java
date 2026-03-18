package com.apextrade.engine;

import com.apextrade.model.Order;
import com.apextrade.model.OrderType;

import java.math.BigDecimal;

public class OrderValidator {

    public static void validate(Order order) {

        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }

        if (order.getSymbol() == null || order.getSymbol().isBlank()) {
            throw new IllegalArgumentException("Symbol is required");
        }

        if (order.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be > 0");
        }

        if (order.getType() == OrderType.LIMIT) {
            if (order.getPrice() == null ||
                order.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Limit price must be > 0");
            }
        }
    }
}