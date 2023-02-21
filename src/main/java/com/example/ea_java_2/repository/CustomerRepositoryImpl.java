package com.example.ea_java_2.repository;

import com.example.ea_java_2.dao.ChinookDAO;
import com.example.ea_java_2.exception.CustomException;
import com.example.ea_java_2.models.Customer;
import com.example.ea_java_2.models.CustomerCountry;
import com.example.ea_java_2.models.CustomerGenre;
import com.example.ea_java_2.models.CustomerSpender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomerRepositoryImpl implements CustomerRepository {
    private final ChinookDAO dao;

    @Autowired
    public CustomerRepositoryImpl(ChinookDAO dao) {
        this.dao = dao;
    }

    @Override
    public List<Customer> findAll() throws CustomException {
        return dao.getAllCustomers();
    }

    @Override
    public Customer findById(Integer integer) throws CustomException {
        return dao.getById(1);
    }

    @Override
    public int add(Customer object) throws CustomException {
        return dao.createCustomer(object);
    }

    @Override
    public int update(Customer object) throws CustomException {
        return dao.updateCustomer(object);
    }

    @Override
    public List<Customer> queryByName(String name) throws CustomException {
        return dao.queryByName(name);
    }

    @Override
    public List<Customer> getPaged(int limit, int offset) throws CustomException {
        return dao.getPaged(limit, offset);
    }

    @Override
    public CustomerCountry getCountryWithMostCustomers() throws CustomException {
        return dao.getCountryWithMostCustomers();
    }

    @Override
    public CustomerSpender getHighestSpendingCustomer() throws CustomException {
        return dao.getHighestSpendingCustomer();
    }

    @Override
    public CustomerGenre getMostPopularGenre(Customer forCustomer) throws CustomException {
        return dao.getMostPopularGenre(forCustomer);
    }
}
