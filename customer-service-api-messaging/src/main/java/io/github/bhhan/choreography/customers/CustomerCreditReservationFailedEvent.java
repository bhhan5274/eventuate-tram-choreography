package io.github.bhhan.choreography.customers;

public class CustomerCreditReservationFailedEvent extends AbstractCustomerOrderEvent{
    public CustomerCreditReservationFailedEvent() {
    }

    public CustomerCreditReservationFailedEvent(Long orderId) {
        super(orderId);
    }
}
