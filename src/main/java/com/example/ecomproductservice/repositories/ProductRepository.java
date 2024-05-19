package com.example.ecomproductservice.repositories;

import com.example.ecomproductservice.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findById(Long id);

    List<Product> findAll();

    Optional<Product> findByName(String title);

    Optional<Product> findByIdAndDeleted(Long id, boolean b);
}