package com.example.ea_java_2.models;

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
