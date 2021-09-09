package io.github.bhhan.choreography.customers;

import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory;
import io.eventuate.tram.spring.optimisticlocking.OptimisticLockingDecoratorConfiguration;
import io.eventuate.tram.viewsupport.rebuild.*;
import io.github.bhhan.choreography.customers.domain.Customer;
import io.github.bhhan.choreography.customers.domain.CustomerRepository;
import io.github.bhhan.choreography.customers.service.CustomerService;
import io.github.bhhan.choreography.customers.service.OrderEventConsumer;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@Import({OptimisticLockingDecoratorConfiguration.class,
        SnapshotConfiguration.class})
@EnableJpaRepositories
@EnableAutoConfiguration
public class CustomerConfiguration {
    @Bean
    public OrderEventConsumer orderEventConsumer(CustomerService customerService){
        return new OrderEventConsumer(customerService);
    }

    @Bean
    public CustomerService customerService(CustomerRepository customerRepository, DomainEventPublisher domainEventPublisher){
        return new CustomerService(customerRepository, domainEventPublisher);
    }

    @Bean
    public DomainEventDispatcher domainEventDispatcher(OrderEventConsumer orderEventConsumer, DomainEventDispatcherFactory domainEventDispatcherFactory){
        return domainEventDispatcherFactory.make("orderServiceEvents", orderEventConsumer.domainEventHandlers());
    }

    @Bean
    public DomainSnapshotExportService<Customer> domainSnapshotExportService(CustomerRepository customerRepository,
                                                                             DomainSnapshotExportServiceFactory<Customer> domainSnapshotExportServiceFactory){
        return domainSnapshotExportServiceFactory.make(
                Customer.class,
                customerRepository,
                customer -> {
                    CustomerSnapshotEvent domainEvent = new CustomerSnapshotEvent(customer.getId(),
                            customer.getName(),
                            customer.getCreditLimit());

                    return new DomainEventWithEntityId(customer.getId(), domainEvent);
                },
                new DBLockService.TableSpec("customer", "customer0_"),
                "MySqlReader"
        );
    }
}
