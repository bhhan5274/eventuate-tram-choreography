package io.github.bhhan.choreography.orders.web;

import io.eventuate.common.json.mapper.JSonMapper;
import io.eventuate.tram.viewsupport.rebuild.DomainSnapshotExportService;
import io.github.bhhan.choreography.orders.domain.Order;
import io.github.bhhan.choreography.orders.domain.OrderRepository;
import io.github.bhhan.choreography.orders.domain.events.OrderDetails;
import io.github.bhhan.choreography.orders.service.OrderService;
import io.github.bhhan.choreography.orders.web.api.CreateOrderRequest;
import io.github.bhhan.choreography.orders.web.api.CreateOrderResponse;
import io.github.bhhan.choreography.orders.web.api.GetOrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final DomainSnapshotExportService<Order> domainSnapshotExportService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateOrderResponse createOrder(@RequestBody CreateOrderRequest createOrderRequest){
        Order order = orderService.createOrder(new OrderDetails(createOrderRequest.getCustomerId(),
                createOrderRequest.getOrderTotal()));
        return new CreateOrderResponse(order.getId());
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<GetOrderResponse> getOrder(@PathVariable Long orderId){
        return orderRepository
                .findById(orderId)
                .map(this::makeSuccessfulResponse)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<GetOrderResponse> cancelOrder(@PathVariable Long orderId){
        Order order = orderService.cancelOrder(orderId);
        return makeSuccessfulResponse(order);
    }

    @PostMapping("/make-snapshot")
    public String makeSnapshot(){
        return JSonMapper.toJson(domainSnapshotExportService.exportSnapshots());
    }

    private ResponseEntity<GetOrderResponse> makeSuccessfulResponse(Order order){
        return new ResponseEntity<>(new GetOrderResponse(order.getId(), order.getState()), HttpStatus.OK);
    }
}
