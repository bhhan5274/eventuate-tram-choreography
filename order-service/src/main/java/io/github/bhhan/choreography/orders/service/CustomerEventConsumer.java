package io.github.bhhan.choreography.orders.service;

import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import io.github.bhhan.choreography.customers.CustomerCreditReservationFailedEvent;
import io.github.bhhan.choreography.customers.CustomerCreditReservedEvent;
import io.github.bhhan.choreography.customers.CustomerValidationFailedEvent;

public class CustomerEventConsumer {
    private final OrderService orderService;

    public CustomerEventConsumer(OrderService orderService) {
        this.orderService = orderService;
    }

    public DomainEventHandlers domainEventHandlers(){
        return DomainEventHandlersBuilder
                .forAggregateType("io.github.bhhan.choreography.customers.domain.Customer")
                .onEvent(CustomerCreditReservedEvent.class, this::handleCustomerCreditReservedEvent)
                .onEvent(CustomerCreditReservationFailedEvent.class, this::handleCustomerCreditReservationFailedEvent)
                .onEvent(CustomerValidationFailedEvent.class, this::handleCustomerValidationFailedEvent)
                .build();
    }

    private void handleCustomerCreditReservedEvent(DomainEventEnvelope<CustomerCreditReservedEvent> domainEventEnvelope){
        orderService.approveOrder(domainEventEnvelope.getEvent().getOrderId());
    }

    private void handleCustomerCreditReservationFailedEvent(DomainEventEnvelope<CustomerCreditReservationFailedEvent> domainEventEnvelope){
        orderService.rejectOrder(domainEventEnvelope.getEvent().getOrderId());
    }

    private void handleCustomerValidationFailedEvent(DomainEventEnvelope<CustomerValidationFailedEvent> domainEventEnvelope){
        orderService.rejectOrder(domainEventEnvelope.getEvent().getOrderId());
    }
}
