package io.github.bhhan.choreography.customers.web.api;

import io.github.bhhan.choreography.common.domain.Money;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CreateCustomerRequest {
    private String name;
    private Money creditLimit;

    public CreateCustomerRequest(String name, Money creditLimit) {
        this.name = name;
        this.creditLimit = creditLimit;
    }
}
