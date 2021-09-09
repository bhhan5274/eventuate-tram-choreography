package io.github.bhhan.choreography.orders.domain.events;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class OrderCanceledEvent implements OrderEvent{
    private OrderDetails orderDetails;

    public OrderCanceledEvent(OrderDetails orderDetails) {
        this.orderDetails = orderDetails;
    }
}
