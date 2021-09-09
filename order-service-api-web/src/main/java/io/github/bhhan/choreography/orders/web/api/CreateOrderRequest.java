package io.github.bhhan.choreography.orders.web.api;

import io.github.bhhan.choreography.common.domain.Money;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateOrderRequest {
    private Money orderTotal;
    private Long customerId;

    public CreateOrderRequest(Money orderTotal, Long customerId) {
        this.orderTotal = orderTotal;
        this.customerId = customerId;
    }
}
