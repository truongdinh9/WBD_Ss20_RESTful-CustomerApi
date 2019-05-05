package com.codegym.cms.controller;

import java.util.ArrayList;
import java.util.List;

import com.codegym.cms.model.Customer;
import com.codegym.cms.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    //-------------------Retrieve All Customers--------------------------------------------------------

//    @RequestMapping(value = "/customers", method = RequestMethod.GET)
    @GetMapping(value = "/customers")
    public ResponseEntity<Iterable<Customer>> listAllCustomers() {
        Iterable<Customer> customers = customerService.findAll();
        if (((ArrayList)customers).isEmpty()) {
            return new ResponseEntity<Iterable<Customer>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
            return new ResponseEntity<Iterable<Customer>>(customers, HttpStatus.OK);


    }

    //-------------------Retrieve Single Customer--------------------------------------------------------

    @GetMapping(value = "/customers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<Customer> getCustomer(@PathVariable("id") Long id){
        Customer customer=customerService.findById(id);
        if(customer==null){
            System.out.println("Not found customer");
            return new ResponseEntity<Customer>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Customer>(customer,HttpStatus.OK);
    }

    //-------------------Create a Customer--------------------------------------------------------

    @PostMapping("/customers")
    public ResponseEntity<Void> createCustomer(@RequestBody Customer customer) {
        System.out.println("Creating Customer " + customer.getLastName());
        customerService.save(customer);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setLocation(ucBuilder.path("/customers/{id}").buildAndExpand(customer.getId()).toUri());
        return new ResponseEntity<Void>( HttpStatus.CREATED);
    }

    //------------------- Update a Customer --------------------------------------------------------

    @PutMapping(value = "/customers/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable("id") long id, @RequestBody Customer customer) {
        System.out.println("Updating Customer " + id);

        Customer currentCustomer = customerService.findById(id);

        if (currentCustomer == null) {
            System.out.println("Customer with id " + id + " not found");
            return new ResponseEntity<Customer>(HttpStatus.NOT_FOUND);
        }

        currentCustomer.setFirstName(customer.getFirstName());
        currentCustomer.setLastName(customer.getLastName());
//        currentCustomer.setId(customer.getId());

        customerService.save(currentCustomer);
        return new ResponseEntity<Customer>(currentCustomer, HttpStatus.OK);
    }

    //------------------- Delete a Customer --------------------------------------------------------

    @DeleteMapping(value = "/customers/{id}")
    public ResponseEntity<Customer> deleteCustomer(@PathVariable("id") long id) {
        System.out.println("Fetching & Deleting Customer with id " + id);

        Customer customer = customerService.findById(id);
        if (customer == null) {
            System.out.println("Unable to delete. Customer with id " + id + " not found");
            return new ResponseEntity<Customer>(HttpStatus.NOT_FOUND);
        }

        customerService.remove(id);
        return new ResponseEntity<Customer>(HttpStatus.OK);
    }
}