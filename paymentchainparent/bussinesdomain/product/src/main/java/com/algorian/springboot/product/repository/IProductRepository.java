package com.algorian.springboot.product.repository;

import com.algorian.springboot.product.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Alejo0147
 */
public interface IProductRepository extends JpaRepository<Product, Long> {
    
}
