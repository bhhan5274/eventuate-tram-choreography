package io.github.bhhan.choreography.customers.web.api;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CreateCustomerResponse {
    private Long customerId;

    public CreateCustomerResponse(Long customerId) {
        this.customerId = customerId;
    }
}
