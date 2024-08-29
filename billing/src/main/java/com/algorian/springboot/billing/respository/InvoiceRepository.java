package com.algorian.springboot.billing.respository;

import com.algorian.springboot.billing.entities.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Alejo0147
 */


public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    
}
