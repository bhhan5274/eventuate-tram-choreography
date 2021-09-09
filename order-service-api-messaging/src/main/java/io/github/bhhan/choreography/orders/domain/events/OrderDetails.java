package io.github.bhhan.choreography.orders.domain.events;

import io.github.bhhan.choreography.common.domain.Money;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Getter
@NoArgsConstructor
@Embeddable
public class OrderDetails {
    private Long customerId;

    @Embedded
    private Money orderTotal;

    public OrderDetails(Long customerId, Money orderTotal) {
        this.customerId = customerId;
        this.orderTotal = orderTotal;
    }
}
