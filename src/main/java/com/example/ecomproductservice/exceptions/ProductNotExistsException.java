package com.example.ecomproductservice.exceptions;

public class ProductNotExistsException extends Exception {

    public ProductNotExistsException(String message) {
        super(message);
    }
}
