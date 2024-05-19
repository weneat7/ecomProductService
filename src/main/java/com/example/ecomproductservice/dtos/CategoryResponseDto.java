package com.example.ecomproductservice.dtos;

import com.example.ecomproductservice.models.Category;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CategoryResponseDto {
    private String name;

}
