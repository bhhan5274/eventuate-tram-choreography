package io.github.bhhan.choreography.customers;

public class CustomerValidationFailedEvent extends AbstractCustomerOrderEvent{
    public CustomerValidationFailedEvent() {
    }

    public CustomerValidationFailedEvent(Long orderId) {
        super(orderId);
    }
}
