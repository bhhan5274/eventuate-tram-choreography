package io.github.bhhan.choreography.endtoendtests;

import io.eventuate.util.test.async.Eventually;
import io.github.bhhan.choreography.common.domain.Money;
import io.github.bhhan.choreography.customers.web.api.CreateCustomerRequest;
import io.github.bhhan.choreography.customers.web.api.CreateCustomerResponse;
import io.github.bhhan.choreography.orders.domain.events.OrderState;
import io.github.bhhan.choreography.orders.web.api.CreateOrderRequest;
import io.github.bhhan.choreography.orders.web.api.CreateOrderResponse;
import io.github.bhhan.choreography.orders.web.api.GetOrderResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = CustomersAndOrdersEndToEndTestConfiguration.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class CustomersAndOrdersEndToEndTest {
    @Value("${host.name}")
    private String hostName;

    @Autowired
    private RestTemplate restTemplate;

    private String baseUrlOrders(String... path){
        StringBuilder sb = new StringBuilder();
        sb.append("http://")
                .append(hostName)
                .append(":8081");
        Arrays.stream(path)
                .forEach(p -> {
                    sb.append('/').append(p);
                });
        return sb.toString();
    }

    private String baseUrlCustomers(String path){
        return "http://" + hostName + ":8082/" + path;
    }

    @Test
    public void shouldApprove(){
        Long customerId = createCustomer("Fred", new Money("15.00"));
        Long orderId = createOrder(customerId, new Money("12.34"));
        assertOrderState(orderId, OrderState.APPROVED);
    }

    @Test
    public void shouldReject(){
        Long customerId = createCustomer("Fred", new Money("15.00"));
        Long orderId = createOrder(customerId, new Money("123.34"));
        assertOrderState(orderId, OrderState.REJECTED);
    }

    @Test
    public void shouldRejectForNonExistentCustomerId(){
        long customerId = System.nanoTime();
        Long orderId = createOrder(customerId, new Money("123.34"));
        assertOrderState(orderId, OrderState.REJECTED);
    }

    @Test
    public void shouldCancel(){
        Long customerId = createCustomer("Fred", new Money("15.00"));
        Long orderId = createOrder(customerId, new Money("12.34"));
        assertOrderState(orderId, OrderState.APPROVED);
        cancelOrder(orderId);
        assertOrderState(orderId, OrderState.CANCELED);
    }

    private Long createCustomer(String name, Money credit){
        return restTemplate
                .postForObject(baseUrlCustomers("v1/customers"),
                        new CreateCustomerRequest(name, credit), CreateCustomerResponse.class)
                .getCustomerId();
    }

    private Long createOrder(Long customerId, Money orderTotal){
        return restTemplate
                .postForObject(baseUrlOrders("v1/orders"),
                        new CreateOrderRequest(orderTotal, customerId), CreateOrderResponse.class)
                .getOrderId();
    }

    private void cancelOrder(Long orderId){
        restTemplate.postForObject(baseUrlOrders("v1/orders", Long.toString(orderId), "cancel"),
                null, GetOrderResponse.class);
    }

    private void assertOrderState(Long id, OrderState expectedState){
        Eventually.eventually(10, 500, TimeUnit.MILLISECONDS, () ->{
            ResponseEntity<GetOrderResponse> response = restTemplate.getForEntity(baseUrlOrders("v1/orders/" + id), GetOrderResponse.class);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            GetOrderResponse order = response.getBody();
            assertEquals(expectedState, order.getOrderState());
        });
    }
}
