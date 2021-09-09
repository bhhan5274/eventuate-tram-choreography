package io.github.bhhan.choreography.customers;

import io.github.bhhan.choreography.common.domain.Money;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CustomerSnapshotEvent implements CustomerEvent{
    private Long id;
    private String name;
    private Money creditLimit;

    public CustomerSnapshotEvent(Long id, String name, Money creditLimit) {
        this.id = id;
        this.name = name;
        this.creditLimit = creditLimit;
    }
}
