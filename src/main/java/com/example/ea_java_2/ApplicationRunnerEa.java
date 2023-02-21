package com.example.ea_java_2;

import com.example.ea_java_2.models.Customer;
import com.example.ea_java_2.models.CustomerCountry;
import com.example.ea_java_2.repository.CustomerRepository;
import com.example.ea_java_2.repository.CustomerRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Class that runs the application
 * The class implements ApplicationRunner
 * To show the functionality of the application
 */
@SpringBootApplication
public class ApplicationRunnerEa implements ApplicationRunner {
    private final CustomerRepository repo;

    public ApplicationRunnerEa(@Autowired CustomerRepositoryImpl repo) {
        this.repo = repo;
    }

    public static void main(String[] args) {
        SpringApplication.run(ApplicationRunnerEa.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // First requirement test
        System.out.println("Find all customers");
        System.out.println(repo.findAll());
        System.out.println();

        // Second requirement test
        System.out.println("Find customer by id");
        System.out.println(repo.findById(2));
        System.out.println();

        // Third requirement test
        System.out.println("Add customer response");
        System.out.println(repo.add(new Customer(999,"test","test","test",new CustomerCountry("test"),"test","test")));
        System.out.println();

        // Fourth requirement test
        System.out.println("Update customer response");
        System.out.println(repo.update(new Customer(1,"UPDATE","test","test",new CustomerCountry("test"),"test","test")));
        System.out.println();

        // Fifth requirement test
        System.out.println("Find customer name");
        System.out.println(repo.queryByName("UPDATE"));
        System.out.println();

        // Sixth requirement test
        System.out.println("Paged customers");
        System.out.println(repo.getPaged(2,2));
        System.out.println();

        // Seventh requirement test
        System.out.println("Country with the most customers");
        System.out.println(repo.getCountryWithMostCustomers());
        System.out.println();

        // Eighth requirement test
        System.out.println("Highest spending customer");
        System.out.println(repo.getHighestSpendingCustomer());
        System.out.println();

        // Ninth requirement test
        System.out.println("Most popular genre for a customer");
        System.out.println(repo.getMostPopularGenre(repo.findById(1)));
        System.out.println();
    }
}
