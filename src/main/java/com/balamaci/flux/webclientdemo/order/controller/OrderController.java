package com.balamaci.flux.webclientdemo.order.controller;


import com.balamaci.flux.webclientdemo.order.Order;
import com.balamaci.flux.webclientdemo.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("{username}")
    public Flux<Order> retrieveOrdersForUser(@PathVariable String username) {
        return orderRepository.getOrderForUser(username);
    }

}
