package com.example.orderservice.config;

import events.PaymentEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class EventConsumerConfig {

    @Autowired
    private OrderStatusUpdateHandler orderStatusUpdateHandler;

    @Bean
    Consumer<PaymentEvent> paymentEventConsumer(){
        //listen to payment-event-topic
        //will check payment status
        // if payment status completed -> complet the order
        // else cancel the order
        return (payment) -> orderStatusUpdateHandler.updateOrder(payment.getPaymentRequestDto().getOrderId(),purchaseOrder -> {
            purchaseOrder.setPaymentStatus(payment.getPaymentStatus());
        });
    }
}
