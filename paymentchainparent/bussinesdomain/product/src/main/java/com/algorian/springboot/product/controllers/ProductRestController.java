package com.algorian.springboot.product.controllers;

import com.algorian.springboot.product.entities.Product;
import com.algorian.springboot.product.repository.IProductRepository;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor

@RestController
@RequestMapping("/product")
public class ProductRestController {
    
    private final IProductRepository _productRepository;
    
    @GetMapping()
    public ResponseEntity<List<Product>> findAll() {
        return ResponseEntity.ok(_productRepository.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        Optional<Product> find = _productRepository.findById(id);
        if (find.isPresent()) {
             return new ResponseEntity<>(find.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> put(@PathVariable Long id, @RequestBody Product product) {
        Optional<Product> find = _productRepository.findById(id);
        if (find.isPresent()) {
            Product newProduct = find.get();
            
            newProduct.setCode(product.getCode());
            newProduct.setName(product.getName());
            
            Product save = _productRepository.save(newProduct);
            return ResponseEntity.status(HttpStatus.OK).body(save);
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping
    public ResponseEntity<?> post(@RequestBody Product product) {
        Product save = _productRepository.save(product);
        return ResponseEntity.ok(save);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Product> find = _productRepository.findById(id);
        if (find.isPresent()) {
            _productRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
}
