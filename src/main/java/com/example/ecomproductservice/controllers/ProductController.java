package com.example.ecomproductservice.controllers;


import com.example.ecomproductservice.dtos.CategoryResponseDto;
import com.example.ecomproductservice.dtos.ProductRequestDto;
import com.example.ecomproductservice.dtos.ProductResponseDto;
import com.example.ecomproductservice.exceptions.ProductNotExistsException;
import com.example.ecomproductservice.models.Category;
import com.example.ecomproductservice.models.Product;
import com.example.ecomproductservice.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private ProductService productService;

    @Autowired
    public ProductController(@Qualifier("SelfProductService") ProductService productService){
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ProductResponseDto getProduct(@PathVariable Long id) throws ProductNotExistsException {
        Product product = productService.getProductById(id);
        return convertProductIntoResponseDto(product);
    }


    @GetMapping("")
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductResponseDto> productResponseDtos = new ArrayList<>();

        for(Product product : products){
            productResponseDtos.add(convertProductIntoResponseDto(product));
        }

        return new ResponseEntity<>(productResponseDtos, HttpStatus.OK);
    }



    @PostMapping("/add")
    public ResponseEntity<ProductResponseDto> addProduct(@RequestBody ProductRequestDto productRequestDto) {
        Product product = convertRequestDtoIntoProduct(productRequestDto);
        product = productService.addProduct(product);
        return new ResponseEntity<>(convertProductIntoResponseDto(product),HttpStatus.OK);
    }



    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ProductResponseDto> deleteProduct(@PathVariable Long id) {
        Product product = productService.deleteProduct(id);
        return new ResponseEntity<>(convertProductIntoResponseDto(product),HttpStatus.OK);
    }



    @PutMapping("/replace/{id}")
    public ResponseEntity<ProductResponseDto> replaceProduct(@PathVariable Long id, ProductRequestDto productRequestDto){
        Product product = convertRequestDtoIntoProduct(productRequestDto);
        product = productService.replaceProduct(id,product);
        return new ResponseEntity<>(convertProductIntoResponseDto(product),HttpStatus.OK);
    }



    @PatchMapping("/update/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable Long id, ProductRequestDto productRequestDto){
        Product product = convertRequestDtoIntoProduct(productRequestDto);
        product = productService.updateProduct(id,product);
        return new ResponseEntity<>(convertProductIntoResponseDto(product),HttpStatus.OK);
    }


    private ProductResponseDto convertProductIntoResponseDto(Product product){
        ProductResponseDto productResponseDto = new ProductResponseDto();
        productResponseDto.setId(product.getId());
        productResponseDto.setName(product.getName());
        productResponseDto.setDescription(product.getDescription());
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        categoryResponseDto.setName(product.getCategory().getName());
        productResponseDto.setCategory(categoryResponseDto);
        productResponseDto.setPrice(product.getPrice());
        productResponseDto.setImageUrl(product.getImageUrl());
        return productResponseDto;
    }

    private Product convertRequestDtoIntoProduct(ProductRequestDto productRequestDto){
        Product product = new Product();
        product.setName(productRequestDto.getName());
        product.setPrice(productRequestDto.getPrice());
        product.setDescription(productRequestDto.getDescription());
        product.setDeleted(false);
        Category category = new Category();
        category.setName(productRequestDto.getCategory());
        product.setCategory(category);
        product.setImageUrl(productRequestDto.getImageUrl());
        return product;
    }
}