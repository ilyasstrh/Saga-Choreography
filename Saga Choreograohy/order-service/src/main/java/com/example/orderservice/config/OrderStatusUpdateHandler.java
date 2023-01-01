package com.example.orderservice.config;

import com.example.orderservice.entities.PurchaseOrder;
import com.example.orderservice.repositories.OrderRepository;
import com.example.orderservice.service.OrderStatusPublisher;
import dto.OrderRequestDto;
import enums.OrderStatus;
import enums.PaymentStatus;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class OrderStatusUpdateHandler {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderStatusPublisher orderStatusPublisher;

    @Transactional
    public void updateOrder(Integer orderId, Consumer<PurchaseOrder> consumer) {
        orderRepository.findById(orderId).ifPresent(consumer.andThen(this::updateOrder));
    }

    private void updateOrder(PurchaseOrder purchaseOrder) {
        Boolean isComplete = PaymentStatus.PAYMENT_COMPLETED.equals(purchaseOrder.getPaymentStatus());
        OrderStatus orderStatus = isComplete ? OrderStatus.ORDER_COMPLETED : OrderStatus.ORDER_CANCELLED;
        purchaseOrder.setOrderStatus(orderStatus);
        if (!isComplete) {
            orderStatusPublisher.publishOrderEvent(convertEntityToDto(purchaseOrder),orderStatus);
        }
    }

    public OrderRequestDto convertEntityToDto(PurchaseOrder po) {
        OrderRequestDto orderRequestDto = new OrderRequestDto();
        orderRequestDto.setOrderId(po.getId());
        orderRequestDto.setUserId(po.getUserId());
        orderRequestDto.setAmount(po.getPrice());
        orderRequestDto.setProductId(po.getProductId());
        return orderRequestDto;
    }
}
