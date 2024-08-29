package com.algorian.springboot.customer.repository;

import com.algorian.springboot.customer.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Alejo0147
 */
public interface ICustomerRepository extends JpaRepository<Customer, Long> {
    
}
