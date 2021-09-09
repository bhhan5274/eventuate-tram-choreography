package io.github.bhhan.choreography.orders.domain.events;

import io.eventuate.tram.events.common.DomainEvent;
import io.github.bhhan.choreography.common.domain.Money;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderSnapshotEvent implements DomainEvent {
    private Long id;
    private Long customerId;
    private Money orderTotal;
    private OrderState orderState;

    public OrderSnapshotEvent(Long id, Long customerId, Money orderTotal, OrderState orderState) {
        this.id = id;
        this.customerId = customerId;
        this.orderTotal = orderTotal;
        this.orderState = orderState;
    }
}
