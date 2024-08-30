package com.algorian.springboot.customer.repository;

import com.algorian.springboot.customer.entities.Customer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Alejo0147
 */
public interface ICustomerRepository extends JpaRepository<Customer, Long> {
    
    Optional<Customer> findByCodeIgnoreCase(String code);
    
    Optional<Customer> findByIbanIgnoreCase(String iban);
    
}
