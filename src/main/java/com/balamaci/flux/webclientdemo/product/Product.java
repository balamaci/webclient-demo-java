package com.balamaci.flux.webclientdemo.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    private String id;
    private String name;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductIdWithQuantity {
        private String productId;

        @Min(value = 1)
        private Integer quantity;
    }

}
