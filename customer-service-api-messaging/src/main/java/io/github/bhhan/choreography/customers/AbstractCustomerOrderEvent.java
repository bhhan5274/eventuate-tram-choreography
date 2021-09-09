package io.github.bhhan.choreography.customers;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public abstract class AbstractCustomerOrderEvent implements CustomerEvent{
    protected Long orderId;

    protected AbstractCustomerOrderEvent(Long orderId) {
        this.orderId = orderId;
    }
}
