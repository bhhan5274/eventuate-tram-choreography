package io.github.bhhan.choreography.orders.domain.events;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class OrderRejectedEvent implements OrderEvent{
    private OrderDetails orderDetails;

    public OrderRejectedEvent(OrderDetails orderDetails) {
        this.orderDetails = orderDetails;
    }
}
