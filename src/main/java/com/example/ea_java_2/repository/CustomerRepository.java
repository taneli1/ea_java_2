package com.example.ea_java_2.repository;

import com.example.ea_java_2.exception.CustomException;
import com.example.ea_java_2.models.Customer;
import com.example.ea_java_2.models.CustomerCountry;
import com.example.ea_java_2.models.CustomerGenre;
import com.example.ea_java_2.models.CustomerSpender;

import java.util.List;


/**
 * Customer repository that extends the functionality of the CRUD repository
 */
public interface CustomerRepository extends CRUDRepository<Customer, Integer> {


    /**
     * Query customers by name.
     *
     * @param name to query with
     * @return list of customers
     */
    List<Customer> queryByName(String name) throws CustomException;

    /**
     * Returns a list of n customers.
     *
     * @param limit  max number of customers
     * @param offset starting from
     * @return list of customers
     */
    List<Customer> getPaged(int limit, int offset) throws CustomException;

    /**
     * Get the country which contains the highest amount of customers.
     *
     * @return CustomerCountry containing the information.
     */
    CustomerCountry getCountryWithMostCustomers() throws CustomException;

    /**
     * Get information on who of the customers has spent the most money by counting the invoice totals.
     *
     * @return CustomerSpender containing the information.
     */
    CustomerSpender getHighestSpendingCustomer() throws CustomException;

    /**
     * Get a genre which the customer has bought most tracks in.
     *
     * @param forCustomer which to get the genre for
     * @return CustomerGenre containing the information.
     */
    CustomerGenre getMostPopularGenre(Customer forCustomer) throws CustomException;
}
