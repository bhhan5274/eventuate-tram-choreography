package io.github.bhhan.choreography.customers;

public class CustomerCreditReservedEvent extends AbstractCustomerOrderEvent{
    public CustomerCreditReservedEvent() {
    }

    public CustomerCreditReservedEvent(Long orderId) {
        super(orderId);
    }
}
