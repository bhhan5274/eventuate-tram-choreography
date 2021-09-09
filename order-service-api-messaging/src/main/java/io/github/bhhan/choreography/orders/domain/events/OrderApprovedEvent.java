package io.github.bhhan.choreography.orders.domain.events;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderApprovedEvent implements OrderEvent{
    private OrderDetails orderDetails;

    public OrderApprovedEvent(OrderDetails orderDetails) {
        this.orderDetails = orderDetails;
    }
}
