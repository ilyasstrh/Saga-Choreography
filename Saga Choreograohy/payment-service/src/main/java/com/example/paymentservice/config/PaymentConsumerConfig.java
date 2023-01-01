package com.example.paymentservice.config;


import java.util.function.Function;

import com.example.paymentservice.services.PaymentService;
import enums.OrderStatus;
import events.OrderEvent;
import events.PaymentEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Configuration
public class PaymentConsumerConfig {

    @Autowired
    private PaymentService paymentService;
    @Bean
    public Function<Flux<OrderEvent>, Flux<PaymentEvent>> paymentProcessor() {
        return orderEventFlux -> orderEventFlux.flatMap(this::processPayment);
    }

    private Mono<PaymentEvent> processPayment(OrderEvent orderEvent){

        if(OrderStatus.ORDER_CREATED.equals(orderEvent.getOrderStatus())){
            return Mono.fromSupplier(() -> paymentService.newOrderEvent(orderEvent));
        }else{
            return Mono.fromRunnable(() -> paymentService.cancelOrderEvent(orderEvent));
        }
    }
}
