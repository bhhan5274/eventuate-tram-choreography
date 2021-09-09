package io.github.bhhan.choreography.orders.domain;

import io.eventuate.tram.events.publisher.ResultWithEvents;
import io.github.bhhan.choreography.orders.domain.events.OrderCreatedEvent;
import io.github.bhhan.choreography.orders.domain.events.OrderDetails;
import io.github.bhhan.choreography.orders.domain.events.OrderState;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collections;

@Entity
@Table(name = "ORDERS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_ID")
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderState state;

    @Embedded
    private OrderDetails orderDetails;

    @Version
    private Long version;

    public static ResultWithEvents<Order> createOrder(OrderDetails orderDetails){
        Order order = new Order(orderDetails);
        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent(orderDetails);
        return new ResultWithEvents<>(order, Collections.singletonList(orderCreatedEvent));
    }

    public Order(OrderDetails orderDetails) {
        this.orderDetails = orderDetails;
        this.state = OrderState.PENDING;
    }

    public void noteCreditReserved(){
        this.state = OrderState.APPROVED;
    }

    public void noteCreditReservationFailed(){
        this.state = OrderState.REJECTED;
    }

    public void cancel(){
        switch (state){
            case PENDING:
                throw new PendingOrderCantBeCancelledException();
            case APPROVED:
                this.state = OrderState.CANCELED;
                return;
            default:
                throw new UnsupportedOperationException("Can't cancel in this state: " + state);
        }
    }
}
