package io.github.bhhan.choreography.customers;

import io.github.bhhan.choreography.common.domain.Money;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CustomerCreatedEvent implements CustomerEvent{
    private String name;
    private Money creditLimit;

    public CustomerCreatedEvent(String name, Money creditLimit) {
        this.name = name;
        this.creditLimit = creditLimit;
    }
}
