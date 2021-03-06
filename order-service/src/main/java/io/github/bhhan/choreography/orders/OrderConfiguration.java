package io.github.bhhan.choreography.orders;

import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory;
import io.eventuate.tram.spring.optimisticlocking.OptimisticLockingDecoratorConfiguration;
import io.eventuate.tram.viewsupport.rebuild.*;
import io.github.bhhan.choreography.orders.domain.Order;
import io.github.bhhan.choreography.orders.domain.OrderRepository;
import io.github.bhhan.choreography.orders.domain.events.OrderSnapshotEvent;
import io.github.bhhan.choreography.orders.service.CustomerEventConsumer;
import io.github.bhhan.choreography.orders.service.OrderService;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories
@EnableAutoConfiguration
@Import({OptimisticLockingDecoratorConfiguration.class,
        SnapshotConfiguration.class})
public class OrderConfiguration {
    @Bean
    public OrderService orderService(DomainEventPublisher domainEventPublisher, OrderRepository orderRepository){
        return new OrderService(orderRepository, domainEventPublisher);
    }

    @Bean
    public CustomerEventConsumer customerEventConsumer(OrderService orderService){
        return new CustomerEventConsumer(orderService);
    }

    @Bean
    public DomainEventDispatcher domainEventDispatcher(CustomerEventConsumer customerEventConsumer, DomainEventDispatcherFactory domainEventDispatcherFactory){
        return domainEventDispatcherFactory.make("customerServiceEvents", customerEventConsumer.domainEventHandlers());
    }

    @Bean
    public DomainSnapshotExportService<Order> domainSnapshotExportService(OrderRepository orderRepository,
                                                                          DomainSnapshotExportServiceFactory<Order> domainSnapshotExportServiceFactory){
        return domainSnapshotExportServiceFactory.make(
                Order.class,
                orderRepository,
                order -> {
                    OrderSnapshotEvent domainEvent = new OrderSnapshotEvent(order.getId(),
                            order.getOrderDetails().getCustomerId(),
                            order.getOrderDetails().getOrderTotal(),
                            order.getState());

                    return new DomainEventWithEntityId(order.getId(), domainEvent);
                },
                new DBLockService.TableSpec("orders", "order0_"),
                "MySqlReader"
        );
    }
}
