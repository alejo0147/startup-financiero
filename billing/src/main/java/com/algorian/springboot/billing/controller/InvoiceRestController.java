package com.algorian.springboot.billing.controller;

import com.algorian.springboot.billing.entities.Invoice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import com.algorian.springboot.billing.respository.InvoiceRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 *
 * @author Alejo0147
 */

@RequiredArgsConstructor

@RestController
@RequestMapping("/billing")
public class InvoiceRestController {
    
    private final InvoiceRepository _billingRepository;
    
    @GetMapping()
    public List<Invoice> list() {
        return _billingRepository.findAll();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?>  get(@PathVariable long id) {
          Optional<Invoice> invoice = _billingRepository.findById(id);
        if (invoice.isPresent()) {
            return new ResponseEntity<>(invoice.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> put(@PathVariable String id, @RequestBody Invoice input) {
        return null;
    }
    
    @PostMapping
    public ResponseEntity<?> post(@RequestBody Invoice input) {
        Invoice save = _billingRepository.save(input);
        return ResponseEntity.ok(save);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
         Optional<Invoice> dto = _billingRepository.findById(Long.valueOf(id));
        if (!dto.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        _billingRepository.delete(dto.get());
        return ResponseEntity.ok().build();
    }
    
}
