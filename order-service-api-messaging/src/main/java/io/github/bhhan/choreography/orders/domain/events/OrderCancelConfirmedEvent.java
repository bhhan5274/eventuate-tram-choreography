package io.github.bhhan.choreography.orders.domain.events;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderCancelConfirmedEvent implements OrderEvent{
    private OrderDetails orderDetails;

    public OrderCancelConfirmedEvent(OrderDetails orderDetails) {
        this.orderDetails = orderDetails;
    }
}
