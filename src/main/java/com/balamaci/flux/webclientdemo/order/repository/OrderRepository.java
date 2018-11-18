package com.balamaci.flux.webclientdemo.order.repository;

import com.balamaci.flux.webclientdemo.order.Order;
import com.balamaci.flux.webclientdemo.product.Product.ProductIdWithQuantity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@Slf4j
public class OrderRepository {

    private Map<String, List<Integer>> usernameOrders = new HashMap<>();
    private Map<Integer, List<ProductIdWithQuantity>> orders = new HashMap<>();

    @PostConstruct
    public void init() {
        usernameOrders.put("john", Arrays.asList(1, 3));
        orders.put(1, Arrays.asList(
                new ProductIdWithQuantity("sword", 1),
                new ProductIdWithQuantity("cloak", 1))
        );
        orders.put(3, Arrays.asList(
                new ProductIdWithQuantity("wine", 2))
        );

        usernameOrders.put("tyrion", Arrays.asList(2));
        orders.put(2, Arrays.asList(
                new ProductIdWithQuantity("book", 1),
                new ProductIdWithQuantity("wine", 10),
                new ProductIdWithQuantity("ring", 2))
        );

        usernameOrders.put("cersei", Arrays.asList(4, 5));
        orders.put(4, Arrays.asList(
                new ProductIdWithQuantity("wine", 2),
                new ProductIdWithQuantity("book", 2))
        );
        orders.put(5, Arrays.asList(
                new ProductIdWithQuantity("wine", 1),
                new ProductIdWithQuantity("ring", 1),
                new ProductIdWithQuantity("earings", 2))
        );
    }

    public Flux<Order> getOrderForUser(String username) {
        List<Integer> orderIds = usernameOrders.get(username);
        if(orderIds == null) {
            return Flux.empty();
        }

        return Flux.fromIterable(orderIds)
                .flatMap(orderId -> Mono.justOrEmpty(new Order(orderId, orders.get(orderId))))
                .doOnNext(order -> log.info("Sending {}", order));
    }

}
