package com.demo.multiplesqldatasources;

import com.demo.multiplesqldatasources.service.PersonService;
import com.demo.multiplesqldatasources.service.ProductService;
import com.demo.multiplesqldatasources.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;


@SpringBootApplication
public class MultipleSqlDatasourcesApplication implements ApplicationRunner {

    @Autowired
    private PersonService personService;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private ProductService productService;

    @Autowired
    @Qualifier("entityManagerFactory")
    private LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean;

    public static void main(String[] args) {
        SpringApplication.run(MultipleSqlDatasourcesApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("Person table:");
        personService.getPeople().forEach(System.out::println);
        System.out.println();

        System.out.println("Vehicle table:");
        vehicleService.getVehicles().forEach(System.out::println);
        System.out.println();

        System.out.println("Product table:");
        productService.getProducts().forEach(System.out::println);
        System.out.println();
    }
}
