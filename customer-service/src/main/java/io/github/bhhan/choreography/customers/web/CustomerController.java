package io.github.bhhan.choreography.customers.web;

import io.eventuate.common.json.mapper.JSonMapper;
import io.eventuate.tram.viewsupport.rebuild.DomainSnapshotExportService;
import io.github.bhhan.choreography.customers.domain.Customer;
import io.github.bhhan.choreography.customers.service.CustomerService;
import io.github.bhhan.choreography.customers.web.api.CreateCustomerRequest;
import io.github.bhhan.choreography.customers.web.api.CreateCustomerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    private final DomainSnapshotExportService<Customer> domainSnapshotExportService;

    @PostMapping
    public CreateCustomerResponse createCustomer(@RequestBody CreateCustomerRequest createCustomerRequest){
        Customer customer = customerService.createCustomer(createCustomerRequest.getName(), createCustomerRequest.getCreditLimit());
        return new CreateCustomerResponse(customer.getId());
    }

    @PostMapping("/make-snapshot")
    public String makeSnapshot(){
        return JSonMapper.toJson(domainSnapshotExportService.exportSnapshots());
    }
}
