package io.github.bhhan.choreography.orders.web.api;

import io.github.bhhan.choreography.orders.domain.events.OrderState;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class GetOrderResponse {
    private Long orderId;
    private OrderState orderState;

    public GetOrderResponse(Long orderId, OrderState orderState) {
        this.orderId = orderId;
        this.orderState = orderState;
    }
}
