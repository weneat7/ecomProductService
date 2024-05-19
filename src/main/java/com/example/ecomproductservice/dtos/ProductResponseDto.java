package com.example.ecomproductservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductResponseDto {
    private long id;
    private String name;
    private double price;
    private String description;
    private String imageUrl;
    private CategoryResponseDto category;
}
