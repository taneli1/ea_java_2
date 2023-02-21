package com.example.ea_java_2.dao;

import com.example.ea_java_2.exception.CustomException;
import com.example.ea_java_2.models.Customer;
import com.example.ea_java_2.models.CustomerCountry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class ChinookDAO {
    private final String url;
    private final String username;
    private final String password;

    public ChinookDAO(
            @Value("${spring.datasource.url}") String url,
            @Value("${spring.datasource.username}") String username,
            @Value("${spring.datasource.password}") String password
    ) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * Return all customers from the database.
     *
     * @return list of customers.
     */
    public List<Customer> getAllCustomers() throws CustomException {
        String sql = "SELECT * FROM customer";
        return getCustomers(sql);
    }


    /**
     * Return a single customer via it's id.
     *
     * @param id of the customer
     * @return customer
     */
    public Customer getById(int id) throws CustomException {
        String sql = "SELECT * FROM customer WHERE customer_id = ?";
        try (Connection conn = openConnection()) {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            return extractCustomers(result).get(0);
        } catch (SQLException e) {
            throw new CustomException("Something went wrong");
        }
    }

    /**
     * Query customers by name.
     *
     * @param name to query with
     * @return list of customers
     */
    public List<Customer> queryByName(String name) throws CustomException {
        String sql = "SELECT * FROM customer WHERE first_name LIKE ?";
        try (Connection conn = openConnection()) {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, name + "%");
            ResultSet result = statement.executeQuery();
            return extractCustomers(result);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new CustomException("Something went wrong");
        }
    }

    /**
     * Returns a list of n customers.
     *
     * @param limit  max number of customers
     * @param offset starting from
     * @return list of customers
     */
    public List<Customer> getPaged(int limit, int offset) throws CustomException {
        String sql = "SELECT * FROM customer ORDER BY customer_id LIMIT ? OFFSET ?";
        try (Connection conn = openConnection()) {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, limit);
            statement.setInt(2, offset);
            ResultSet result = statement.executeQuery();
            return extractCustomers(result);
        } catch (SQLException e) {
            throw new CustomException("Something went wrong");
        }
    }

    /**
     * Create a customer with the parameter Customer's values. Id of the parameter object is ignored.
     *
     * @param customer to create
     * @return created customer
     */
    public Customer createCustomer(Customer customer) throws CustomException {
        String sql = "INSERT INTO customer(first_name, last_name, postal_code, country, phone, email) " +
                "VALUES (?,?,?,?,?,?)";
        return customerQuery(customer, sql);
    }

    /**
     * Update a customer record. Replaces all the values with the ones provided by the parameter object.
     *
     * @param customer to update
     * @return updated customer
     */
    public Customer updateCustomer(Customer customer) throws CustomException {
        String sql = "UPDATE customer " +
                "SET first_name=?, last_name=?, postal_code=?, country=?, phone=?, email=? " +
                "WHERE customer_id=?";
        return customerQuery(customer, sql);
    }

    private Customer customerQuery(Customer customer, String sql) throws CustomException {
        try (Connection conn = openConnection()) {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, customer.firstName());
            statement.setString(2, customer.lastName());
            statement.setString(3, customer.postalCode());
            statement.setString(4, customer.country().name());
            statement.setString(5, customer.phone());
            statement.setString(6, customer.email());
            ResultSet result = statement.executeQuery();
            return extractCustomers(result).get(0);
        } catch (SQLException e) {
            throw new CustomException("Something went wrong");
        }
    }

    public CustomerCountry getCountryWithMostCustomers() throws CustomException {
        String sql = "SELECT country " +
                "FROM customer GROUP BY customer.country " +
                "ORDER BY COUNT(country) DESC " +
                "LIMIT 1;";
        try (Connection conn = openConnection()) {
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return new CustomerCountry(result.getString("country"));
            }
            throw new CustomException("No result");
        } catch (SQLException e) {
            throw new CustomException("Something went wrong");
        }
    }

    public Customer getHighestSpendingCustomer() throws CustomException {
        String sql = "SELECT customer.customer_id, first_name, last_name, country, postal_code, phone, email, SUM(invoice.total) AS total " +
                "FROM customer " +
                "LEFT JOIN invoice ON customer.customer_id = invoice.customer_id " +
                "GROUP BY customer.customer_id " +
                "ORDER BY total DESC " +
                "LIMIT 1 ";

        try (Connection conn = openConnection()) {
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            return extractCustomers(result).get(0);
        } catch (SQLException e) {
            throw new CustomException("Something went wrong");
        }
    }

    public Pair<Customer, String> getMostPopularGenre(Customer forCustomer) throws CustomException {
        String sql = "SELECT customer.customer_id,first_name, last_name, country, postal_code, phone, email, track.genre_id, genre.name, COUNT(track.genre_id) AS genrecount FROM customer " +
                "LEFT JOIN invoice ON customer.customer_id=invoice.customer_id " +
                "LEFT JOIN invoice_line ON invoice_line.invoice_id=invoice.invoice_id " +
                "LEFT JOIN track ON track.track_id=invoice_line.track_id " +
                "LEFT JOIN genre ON genre.genre_id=track.genre_id " +
                "WHERE customer.customer_id=? " +
                "GROUP BY track.genre_id, customer.customer_id, genre.name " +
                "ORDER BY genrecount DESC " +
                "LIMIT 1";

        try (Connection conn = openConnection()) {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, forCustomer.id());
            ResultSet result = statement.executeQuery();
            result.next();
            Customer c = new Customer(
                    result.getInt("customer_id"),
                    result.getString("first_name"),
                    result.getString("last_name"),
                    result.getString("postal_code"),
                    new CustomerCountry(result.getString("country")),
                    result.getString("phone"),
                    result.getString("email")
            );
            return Pair.of(c, result.getString("name"));
        } catch (SQLException e) {
            throw new CustomException("Something went wrong");
        }
    }


    private List<Customer> getCustomers(String sql) throws CustomException {
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            return extractCustomers(result);
        } catch (SQLException e) {
            throw new CustomException("Something went wrong");
        }
    }

    private List<Customer> extractCustomers(ResultSet resultSet) throws SQLException {
        List<Customer> customers = new ArrayList<>();
        while (resultSet.next()) {
            customers.add(new Customer(
                    resultSet.getInt("customer_id"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"),
                    resultSet.getString("postal_code"),
                    new CustomerCountry(resultSet.getString("country")),
                    resultSet.getString("phone"),
                    resultSet.getString("email")
            ));
        }
        return customers;
    }

    private Connection openConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}
