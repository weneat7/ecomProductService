package com.example.ecomproductservice.services;

import com.example.ecomproductservice.models.Category;
import com.example.ecomproductservice.models.Product;
import com.example.ecomproductservice.repositories.CategoryRepository;
import com.example.ecomproductservice.repositories.ProductRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service("SelfProductService")
public class SelfProductService implements ProductService {

    private ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private RedisTemplate<String, Object> redisTemplate;

    public SelfProductService(ProductRepository productRepository,
                              CategoryRepository categoryRepository,
                              RedisTemplate<String,Object> redisTemplate){
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Product getProductById(Long id) {
        Product p = (Product) redisTemplate.opsForHash().get("PRODUCTS","PRODUCT_"+id);
        if(p != null)
            return p;

        Optional<Product> optionalProduct = productRepository.findById(id);
        if(optionalProduct.isEmpty())
            return null;

        redisTemplate.opsForHash().put("PRODUCTS","PRODUCT"+id,optionalProduct.get());

        return optionalProduct.get();

    }


    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product deleteProduct(Long id) {
        Optional<Product> optionalProduct = productRepository.findByIdAndDeleted(id,false);
        if(optionalProduct.isEmpty())
            return null;

        optionalProduct.get().setDeleted(true);
        productRepository.save(optionalProduct.get());
        return optionalProduct.get();
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        Optional<Product> optionalProduct = productRepository.findByIdAndDeleted(id,false);
        if(optionalProduct.isEmpty())
            return null;

        Product product1 =optionalProduct.get();
        product1.setDeleted(product.isDeleted());
        product1.setId(product.getId());
        product1.setDescription(product.getDescription());
        product1.setName(product.getName());
        product1.setCategory(product.getCategory());
        product1.setImageUrl(product1.getImageUrl());

        return productRepository.save(product1);
    }

    @Override
    public Product replaceProduct(Long id, Product product) {
        Optional<Product> optionalProduct = productRepository.findByIdAndDeleted(id,false);
        if(optionalProduct.isEmpty())
            return null;

        Product product1 =optionalProduct.get();
        product1.setDeleted(product.isDeleted());
        product1.setId(product.getId());
        product1.setDescription(product.getDescription());
        product1.setName(product.getName());
        product1.setCategory(product.getCategory());
        product1.setImageUrl(product1.getImageUrl());

        return productRepository.save(product1);
    }

    @Override
    public Product addProduct(Product product) {
        Category category = product.getCategory();
        String categoryName = category.getName();
        Optional<Category> optionalCategory = categoryRepository.findByName(categoryName);
        if(optionalCategory.isEmpty()){
           category = categoryRepository.save(category);
           product.setCategory(category);
        }
        else product.setCategory(optionalCategory.get());
        return productRepository.save(product);
    }
}
