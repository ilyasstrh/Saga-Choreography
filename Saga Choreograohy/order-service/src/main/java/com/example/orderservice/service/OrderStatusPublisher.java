package com.example.orderservice.service;

import dto.OrderRequestDto;
import enums.OrderStatus;
import events.Event;
import events.OrderEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

@Service
public class OrderStatusPublisher {
    @Autowired
    private Sinks.Many<OrderEvent> orderSinks;

    public  void publishOrderEvent(OrderRequestDto  orderRequestDto, OrderStatus orderStatus)
    {
        OrderEvent orderEvent = new OrderEvent(orderRequestDto,orderStatus);
        orderSinks.tryEmitNext(orderEvent);
    }
}
