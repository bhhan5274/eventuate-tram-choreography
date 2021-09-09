package io.github.bhhan.choreography.orders.service;

import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.events.publisher.ResultWithEvents;
import io.github.bhhan.choreography.orders.domain.Order;
import io.github.bhhan.choreography.orders.domain.OrderRepository;
import io.github.bhhan.choreography.orders.domain.events.OrderApprovedEvent;
import io.github.bhhan.choreography.orders.domain.events.OrderCanceledEvent;
import io.github.bhhan.choreography.orders.domain.events.OrderDetails;
import io.github.bhhan.choreography.orders.domain.events.OrderRejectedEvent;

import javax.transaction.Transactional;
import java.util.Collections;

public class OrderService {
    private final OrderRepository orderRepository;
    private final DomainEventPublisher domainEventPublisher;

    public OrderService(OrderRepository orderRepository, DomainEventPublisher domainEventPublisher) {
        this.orderRepository = orderRepository;
        this.domainEventPublisher = domainEventPublisher;
    }

    @Transactional
    public Order createOrder(OrderDetails orderDetails){
        ResultWithEvents<Order> orderWithEvent = Order.createOrder(orderDetails);
        Order order = orderWithEvent.result;
        orderRepository.save(order);
        domainEventPublisher.publish(Order.class, order.getId(), orderWithEvent.events);
        return order;
    }

    public void approveOrder(Long orderId){
        Order order = orderRepository
                .findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("order with id %s not found", orderId)));

        order.noteCreditReserved();
        domainEventPublisher.publish(Order.class, orderId, Collections.singletonList(new OrderApprovedEvent(order.getOrderDetails())));
    }

    public void rejectOrder(Long orderId){
        Order order = orderRepository
                .findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("order with id %s not found", orderId)));

        order.noteCreditReservationFailed();
        domainEventPublisher.publish(Order.class, orderId, Collections.singletonList(new OrderRejectedEvent(order.getOrderDetails())));
    }

    @Transactional
    public Order cancelOrder(Long orderId){
        Order order = orderRepository
                .findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("order with id %s not found", orderId)));

        order.cancel();
        domainEventPublisher.publish(Order.class,
                orderId,
                Collections.singletonList(new OrderCanceledEvent(order.getOrderDetails())));
        return order;
    }
}
