package com.algorian.springboot.customer.controllers;

import com.algorian.springboot.customer.entities.Customer;
import com.algorian.springboot.customer.repository.ICustomerRepository;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

/**
 *
 * @author Alejo0147
 */
@RestController
@RequestMapping("/customer")
public class CustomerRestController {

    private final ICustomerRepository _customerRepository;

    public CustomerRestController(ICustomerRepository _CustomerRepository) {
        this._customerRepository = _CustomerRepository;
    }

    @GetMapping()
    public ResponseEntity<List<Customer>> findAll() {
        return ResponseEntity.ok(_customerRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        Optional<Customer> find = _customerRepository.findById(id);
        if (find.isPresent()) {
            return new ResponseEntity<>(find.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> put(@PathVariable Long id, @RequestBody Customer customer) {
        Optional<Customer> find = _customerRepository.findById(id);
        if (find.isPresent()) {
            Customer newCustomer = find.get();
            
            newCustomer.setCode(customer.getCode());
            newCustomer.setIban(customer.getIban());
            newCustomer.setName(customer.getName());
            newCustomer.setSurname(customer.getSurname());
            newCustomer.setPhone(customer.getPhone());
            newCustomer.setAddress(customer.getAddress());
            
            Customer save = _customerRepository.save(newCustomer);
            return ResponseEntity.status(HttpStatus.OK).body(save);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> post(@RequestBody Customer customer) {
        Customer save = _customerRepository.save(customer);
        return ResponseEntity.ok(save);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Customer> find = _customerRepository.findById(id);
        if (find.isPresent()) {
            _customerRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}
