package io.github.bhhan.choreography.customers.service;

import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import io.github.bhhan.choreography.orders.domain.events.OrderCanceledEvent;
import io.github.bhhan.choreography.orders.domain.events.OrderCreatedEvent;

public class OrderEventConsumer {
    private final CustomerService customerService;

    public OrderEventConsumer(CustomerService customerService) {
        this.customerService = customerService;
    }

    public DomainEventHandlers domainEventHandlers(){
        return DomainEventHandlersBuilder
                .forAggregateType("io.github.bhhan.choreography.orders.domain.Order")
                .onEvent(OrderCreatedEvent.class, this::handleOrderCreatedEvent)
                .onEvent(OrderCanceledEvent.class, this::handleOrderCanceledEvent)
                .build();
    }

    private void handleOrderCreatedEvent(DomainEventEnvelope<OrderCreatedEvent> domainEventEnvelope){
        OrderCreatedEvent event = domainEventEnvelope.getEvent();
        customerService.reserveCredit(Long.parseLong(domainEventEnvelope.getAggregateId()),
                event.getOrderDetails().getCustomerId(),
                event.getOrderDetails().getOrderTotal());
    }

    private void handleOrderCanceledEvent(DomainEventEnvelope<OrderCanceledEvent> domainEventEnvelope){
        OrderCanceledEvent event = domainEventEnvelope.getEvent();
        customerService.releaseCredit(Long.parseLong(domainEventEnvelope.getAggregateId()),
                event.getOrderDetails().getCustomerId());
    }
}
