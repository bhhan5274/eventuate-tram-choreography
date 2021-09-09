package io.github.bhhan.choreography.orders.domain.events;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class OrderCreatedEvent implements OrderEvent{
    private OrderDetails orderDetails;

    public OrderCreatedEvent(OrderDetails orderDetails) {
        this.orderDetails = orderDetails;
    }
}
