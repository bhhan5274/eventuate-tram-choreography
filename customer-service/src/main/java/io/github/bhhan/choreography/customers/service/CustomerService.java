package io.github.bhhan.choreography.customers.service;

import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.events.publisher.ResultWithEvents;
import io.github.bhhan.choreography.common.domain.Money;
import io.github.bhhan.choreography.customers.CustomerCreditReservationFailedEvent;
import io.github.bhhan.choreography.customers.CustomerCreditReservedEvent;
import io.github.bhhan.choreography.customers.CustomerValidationFailedEvent;
import io.github.bhhan.choreography.customers.domain.Customer;
import io.github.bhhan.choreography.customers.domain.CustomerCreditLimitExceededException;
import io.github.bhhan.choreography.customers.domain.CustomerRepository;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Optional;

public class CustomerService {
    private final CustomerRepository customerRepository;
    private final DomainEventPublisher domainEventPublisher;

    public CustomerService(CustomerRepository customerRepository, DomainEventPublisher domainEventPublisher) {
        this.customerRepository = customerRepository;
        this.domainEventPublisher = domainEventPublisher;
    }

    @Transactional
    public Customer createCustomer(String name, Money creditLimit){
        ResultWithEvents<Customer> customerWithEvents = Customer.create(name, creditLimit);
        Customer customer = customerRepository.save(customerWithEvents.result);
        domainEventPublisher.publish(Customer.class, customer.getId(), customerWithEvents.events);
        return customer;
    }

    public void reserveCredit(Long orderId, Long customerId, Money orderTotal) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);

        if(!optionalCustomer.isPresent()){
            domainEventPublisher.publish(Customer.class,
                    customerId,
                    Collections.singletonList(new CustomerValidationFailedEvent(orderId)));
            return;
        }

        Customer customer = optionalCustomer.get();

        try {
            customer.reserveCredit(orderId, orderTotal);

            CustomerCreditReservedEvent customerCreditReservedEvent = new CustomerCreditReservedEvent(orderId);
            domainEventPublisher.publish(Customer.class,
                    customer.getId(),
                    Collections.singletonList(customerCreditReservedEvent));
        }catch(CustomerCreditLimitExceededException e){
            CustomerCreditReservationFailedEvent customerCreditReservationFailedEvent = new CustomerCreditReservationFailedEvent(orderId);
            domainEventPublisher.publish(Customer.class,
                    customer.getId(),
                    Collections.singletonList(customerCreditReservationFailedEvent));
        }
    }

    public void releaseCredit(Long orderId, Long customerId){
        Customer customer = customerRepository.findById(customerId).get();
        customer.unReserveCredit(orderId);
    }
}
