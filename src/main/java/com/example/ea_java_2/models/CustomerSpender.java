package com.example.ea_java_2.models;

/**
 * Contains information about the customers spending.
 */
public record CustomerSpender(
        int customerId,
        String name,
        float totalSpent
) {

}
