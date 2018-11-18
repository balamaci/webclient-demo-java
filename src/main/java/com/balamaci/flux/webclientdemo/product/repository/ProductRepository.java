package com.balamaci.flux.webclientdemo.product.repository;

import com.balamaci.flux.webclientdemo.product.Product;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Configuration
public class ProductRepository {

    private static List<Product> products = Arrays.asList(
            new Product("sword", "LongSword"),
            new Product("cloak", "Black Cloak"),
            new Product("book", "The Adventures"),
            new Product("wine", "Wine Bottle"),
            new Product("earrings", "Pearl Earrings"),
            new Product("ring", "Jewel Ring"));

    public Mono<Product> findProductById(String id) {
        return Flux.fromIterable(products).filter(product -> product.getId().equals(id)).single();
    }

}
