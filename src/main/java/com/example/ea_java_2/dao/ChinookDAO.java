package com.example.ea_java_2.dao;

import com.example.ea_java_2.models.Customer;
import com.example.ea_java_2.models.CustomerCountry;
import com.example.ea_java_2.models.CustomerGenre;
import org.springframework.beans.factory.annotation.Value;
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

    public void test() {
        try (Connection conn = DriverManager.getConnection(url, username, password);) {
            System.out.println("Connected to Postgres...");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Return all customers from the database.
     *
     * @return list of customers.
     */
    public List<Customer> getAllCustomers() {
        String sql = "SELECT * FROM customer";
        return getCustomers(sql);
    }

    /**
     * Return a single customer via it's id.
     *
     * @param id of the customer
     * @return customer
     */
    public Customer getById(int id) {
        String sql = "SELECT * FROM customer WHERE customer_id = ?";
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            return extractCustomers(result).get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Customer> queryByName(String name) {
        String sql = "SELECT * FROM customer WHERE first_name LIKE ?";
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, name + "%");
            ResultSet result = statement.executeQuery();
            return extractCustomers(result);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Customer> getPaged(int limit, int offset) {
        String sql = "SELECT * FROM customer ORDER BY customer_id LIMIT ? OFFSET ?";
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, limit);
            statement.setInt(2, offset);
            ResultSet result = statement.executeQuery();
            return extractCustomers(result);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Create a customer with the parameter Customer's values. Id of the parameter object is ignored.
     *
     * @param customer to create
     * @return created customer
     */
    public Customer createCustomer(Customer customer) {
        String sql = "INSERT INTO customer(first_name, last_name, postal_code, country, phone, email) values (?,?,?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
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
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Update a customer record. Replaces all the values with the ones provided by the parameter object.
     *
     * @param customer to update
     * @return updated customer
     */
    public Customer updateCustomer(Customer customer) {
        String sql = "UPDATE customer " +
                "SET first_name=?, last_name=?, postal_code=?, country=?, phone=?, email=? " +
                "WHERE customer_id=?";

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
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
            e.printStackTrace();
        }
        return null;
    }

    public CustomerCountry getCountryWithMostCustomers() {
        String sql = "SELECT country\n" +
                "FROM customer GROUP BY customer.country \n" +
                "ORDER BY COUNT(country) DESC\n" +
                "LIMIT 1;";

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return new CustomerCountry(result.getString("country"));
            }
            throw new RuntimeException("No result");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Customer getHighestSpendingCustomer() {
        String sql = "SELECT customer.customer_id, first_name, last_name, country, postal_code, phone, email, SUM(invoice.total) AS total " +
                "FROM customer " +
                "LEFT JOIN invoice ON customer.customer_id = invoice.customer_id " +
                "GROUP BY customer.customer_id " +
                "ORDER BY total DESC " +
                "LIMIT 1 ";

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            return extractCustomers(result).get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

//    public CustomerGenre getMostPopularGenre(Customer forCustomer) {
//        String sql = "SELECT name " +
//                "FROM genre " +
//                "LEFT JOIN customer ON customer_id="
//
//        try (Connection conn = DriverManager.getConnection(url, username, password)) {
//            PreparedStatement statement = conn.prepareStatement(sql);
//            ResultSet result = statement.executeQuery();
//            return extractCustomers(result).get(0);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }


    private List<Customer> getCustomers(String sql) {
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            return extractCustomers(result);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
}
