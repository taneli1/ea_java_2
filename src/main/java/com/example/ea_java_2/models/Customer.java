package com.example.ea_java_2.models;

/**
 * Represents information about the customer
 */
public record Customer(
        int id,
        String firstName,
        String lastName,
        String postalCode,
        CustomerCountry country,
        String phone,
        String email
) {
}
