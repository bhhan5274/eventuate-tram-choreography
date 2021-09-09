package io.github.bhhan.choreography.customers.domain;

import io.eventuate.tram.events.publisher.ResultWithEvents;
import io.github.bhhan.choreography.common.domain.Money;
import io.github.bhhan.choreography.customers.CustomerCreatedEvent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collections;
import java.util.Map;

@Entity
@Table(name = "CUSTOMERS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CUSTOMER_ID")
    private Long id;

    private String name;

    @Embedded
    private Money creditLimit;

    @ElementCollection
    private Map<Long, Money> creditReservations;

    private Long creationTime;

    @Version
    private Long version;

    public static ResultWithEvents<Customer> create(String name, Money creditLimit){
        Customer customer = new Customer(name, creditLimit);
        return new ResultWithEvents<>(customer,
                Collections.singletonList(new CustomerCreatedEvent(customer.getName(), customer.getCreditLimit())));
    }

    public Customer(String name, Money creditLimit) {
        this.name = name;
        this.creditLimit = creditLimit;
        this.creditReservations = Collections.emptyMap();
        this.creationTime = System.currentTimeMillis();
    }

    public Money availableCredit(){
        return creditLimit.subtract(creditReservations.values()
                .stream()
                .reduce(Money.ZERO, Money::add));
    }

    public void reserveCredit(Long orderId, Money orderTotal){
        if(availableCredit().isGreaterThanOrEqual(orderTotal)){
            creditReservations.put(orderId, orderTotal);
        }else {
            throw new CustomerCreditLimitExceededException();
        }
    }

    public void unReserveCredit(Long orderId){
        creditReservations.remove(orderId);
    }
}
