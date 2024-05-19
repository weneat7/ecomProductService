package com.example.ecomproductservice.services;

import com.example.ecomproductservice.exceptions.ProductNotExistsException;
import com.example.ecomproductservice.models.Product;

import java.util.List;

public interface ProductService {
    public Product getProductById(Long id)  throws ProductNotExistsException;
    public List<Product> getAllProducts();
    public Product deleteProduct(Long id);
    public Product updateProduct(Long id, Product product);
    public Product replaceProduct(Long id, Product product);
    public Product addProduct(Product product);


}
