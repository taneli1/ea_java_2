package com.example.ea_java_2;

import com.example.ea_java_2.dao.ChinookDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EaJava2Application implements ApplicationRunner {

    @Autowired
    ChinookDAO chinookDAO;

    public static void main(String[] args) {
        SpringApplication.run(EaJava2Application.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println(chinookDAO.getMostPopularGenre(chinookDAO.getById(1)));
        System.out.println(chinookDAO.getById(1));
        System.out.println(chinookDAO.getAllCustomers());
        System.out.println(chinookDAO.queryByName("Luís"));
        System.out.println(chinookDAO.getHighestSpendingCustomer());
        System.out.println(chinookDAO.getCountryWithMostCustomers());
    }
}
