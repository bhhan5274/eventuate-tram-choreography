package io.github.bhhan.choreography.customers;

public class CustomerCreditReleasedEvent extends AbstractCustomerOrderEvent{
    public CustomerCreditReleasedEvent() {
    }

    public CustomerCreditReleasedEvent(Long orderId) {
        super(orderId);
    }
}
