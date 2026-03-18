package com.apextrade;

import com.apextrade.infrastructure.DatabaseConfig;

import java.sql.Connection;

public class TestConnection {

    public static void main(String[] args) throws Exception {

        try (Connection conn =
                     DatabaseConfig.getDataSource().getConnection()) {

            System.out.println("✅ Connected to MySQL successfully!");
        }
    }
}