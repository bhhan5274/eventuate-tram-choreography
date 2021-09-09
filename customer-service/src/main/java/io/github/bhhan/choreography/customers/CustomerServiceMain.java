package io.github.bhhan.choreography.customers;

import io.github.bhhan.choreography.customers.web.CustomerWebConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({CustomerConfiguration.class, CustomerWebConfiguration.class})
public class CustomerServiceMain {
    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceMain.class, args);
    }
}
