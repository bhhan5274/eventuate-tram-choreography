package io.github.bhhan.choreography.orders.web.api;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CreateOrderResponse {
    private Long orderId;

    public CreateOrderResponse(Long orderId) {
        this.orderId = orderId;
    }
}
