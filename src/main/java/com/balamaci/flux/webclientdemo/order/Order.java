package com.balamaci.flux.webclientdemo.order;

import com.balamaci.flux.webclientdemo.product.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Order {

    public static final Order NOT_FOUND = new Order(-1, new ArrayList<>());

    @EqualsAndHashCode.Include
    private Integer id;

    private List<Product.ProductIdWithQuantity> products;

    @Override
    public String toString() {
        if(id.equals(NOT_FOUND.getId())) {
            return "Order_NOT_FOUND";
        }

        return "Order{" +
                "id=" + id +
                ", products=" + products +
                '}';
    }
}
