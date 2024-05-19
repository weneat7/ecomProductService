package com.example.ecomproductservice.services;

import com.example.ecomproductservice.dtos.FakeStoreProductDto;
import com.example.ecomproductservice.dtos.ProductRequestDto;
import com.example.ecomproductservice.exceptions.ProductNotExistsException;
import com.example.ecomproductservice.models.Category;
import com.example.ecomproductservice.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


@Service("fakeStoreProductService")
public class FakeStoreProductService implements ProductService {
    private RestTemplate restTemplate;

    @Autowired
    public FakeStoreProductService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private Product convertFakeStoreProductToProduct(FakeStoreProductDto fakeStoreProduct) {
        Product product = new Product();
        product.setName(fakeStoreProduct.getTitle());
        product.setId(fakeStoreProduct.getId());
        product.setPrice(fakeStoreProduct.getPrice());
        product.setDescription(fakeStoreProduct.getDescription());
        product.setImageUrl(fakeStoreProduct.getImage());
        product.setCategory(new Category());
        product.getCategory().setName(fakeStoreProduct.getCategory());

        return product;
    }

    @Override
    public Product getProductById(Long id) throws ProductNotExistsException {
//        int a = 1 / 0;
        ResponseEntity<FakeStoreProductDto> productDto = restTemplate.getForEntity(
                "https://fakestoreapi.com/products/" + id,
                FakeStoreProductDto.class
        );

        if (productDto.getStatusCode() != HttpStatusCode.valueOf(200)) {
            // log this to command line
        }

        if (productDto == null) {
            throw new ProductNotExistsException(
                    "Product with id: " + id + " doesn't exist."
            );
        }

        return convertFakeStoreProductToProduct(productDto.getBody());
    }

    @Override
    public List<Product> getAllProducts() {



//        List<FakeStoreProductDto> response = restTemplate.getForObject(
//                "https://fakestoreapi.com/products",
//                List<FakeStoreProductDto>.class
//        );

        // runtime
        FakeStoreProductDto[] response = restTemplate.getForObject(
                "https://fakestoreapi.com/products",
                FakeStoreProductDto[].class
        );


        List<Product> answer = new ArrayList<>();


        for (FakeStoreProductDto dto: response) {
            answer.add(convertFakeStoreProductToProduct(dto));
        }

        return answer;
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        return null;
    }

    @Override
    public Product replaceProduct(Long id, Product product) {
        FakeStoreProductDto fakeStoreProductDto = new FakeStoreProductDto();
        fakeStoreProductDto.setTitle(product.getName());
        fakeStoreProductDto.setPrice(product.getPrice());
        fakeStoreProductDto.setImage(product.getDescription());
        fakeStoreProductDto.setImage(product.getImageUrl());

        RequestCallback requestCallback = restTemplate.httpEntityCallback(fakeStoreProductDto, FakeStoreProductDto.class);
        HttpMessageConverterExtractor<FakeStoreProductDto> responseExtractor =
                new HttpMessageConverterExtractor<>(FakeStoreProductDto.class, restTemplate.getMessageConverters());
        FakeStoreProductDto response = restTemplate.execute("https://fakestoreapi.com/products/" + id, HttpMethod.PUT, requestCallback, responseExtractor);

        return convertFakeStoreProductToProduct(response);
    }

    @Override
    public Product addProduct(Product product) {
        FakeStoreProductDto fakeStoreProductDto = convertProductIntoFakeStoreProductDto(product);
        RequestCallback requestCallback = restTemplate.httpEntityCallback(fakeStoreProductDto, FakeStoreProductDto.class);
        HttpMessageConverterExtractor<FakeStoreProductDto> responseExtractor = new HttpMessageConverterExtractor(FakeStoreProductDto.class, restTemplate.getMessageConverters());
        FakeStoreProductDto fakeStoreProductDto1 = restTemplate.execute("https://fakestoreapi.com/products", HttpMethod.POST, requestCallback, responseExtractor);
        return convertFakeStoreProductIntoProduct(fakeStoreProductDto1);
    }

    @Override
    public Product deleteProduct(Long id) {
        try {
            Product product = getProductById(id);
            restTemplate.delete("https://fakestoreapi.com/products/"+id);
            return product;
        }catch (Exception e){
            return null;
        }
    }


    private FakeStoreProductDto convertProductIntoFakeStoreProductDto(Product product){return null;}

    private Product convertFakeStoreProductIntoProduct(FakeStoreProductDto fakeStoreProductDto){return null;}

}

// Break till 10:35
