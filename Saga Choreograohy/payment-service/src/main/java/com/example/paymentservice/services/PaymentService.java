package com.example.paymentservice.services;

import com.example.paymentservice.Entities.UserBalance;
import com.example.paymentservice.Entities.UserTransaction;
import com.example.paymentservice.repositories.UserBalanceRepository;
import com.example.paymentservice.repositories.UserTransactionRepository;
import dto.OrderRequestDto;
import dto.PaymentRequestDto;
import enums.PaymentStatus;
import events.OrderEvent;
import events.PaymentEvent;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PaymentService {

    @Autowired
    private UserBalanceRepository userBalanceRepository;
    @Autowired
    private UserTransactionRepository userTransactionRepository;

    @PostConstruct
    public void initUserBalanceInDb() {
        userBalanceRepository.saveAll(Stream.of(new UserBalance(101, 4000.0), new UserBalance(102, 5000.0), new UserBalance(103, 5000.0)).collect(Collectors.toList()));
    }

    /**
     * get the user id
     * then check the balance availability
     * if the balance is sufficient -> payment completed and deduct amount from bd
     * if payment not sufficient  -> cancel the order event and update the amount in db
     */
    @Transactional
    public PaymentEvent newOrderEvent(OrderEvent orderEvent) {
        OrderRequestDto orderRequestDto = orderEvent.getOrderRequestDto();
        PaymentRequestDto paymentRequestDto = new PaymentRequestDto(orderRequestDto.getOrderId(), orderRequestDto.getUserId(), orderRequestDto.getAmount());
        return userBalanceRepository.findById(orderRequestDto.getUserId())
                .filter(ub -> ub.getPrice() > orderRequestDto.getAmount())
                .map(ub -> {
                    ub.setPrice(ub.getPrice() - orderRequestDto.getAmount());
                    userTransactionRepository.save(new UserTransaction(orderRequestDto.getOrderId()
                            , orderRequestDto.getOrderId(), orderRequestDto.getAmount()));
                    return new PaymentEvent(paymentRequestDto, PaymentStatus.PAYMENT_COMPLETED);
                })
                .orElse(new PaymentEvent(paymentRequestDto, PaymentStatus.PAYMENT_FAILED));
    }

    @Transactional
    public void cancelOrderEvent(OrderEvent orderEvent) {
        userTransactionRepository.findById(orderEvent.getOrderRequestDto().getOrderId())
                .ifPresent(ut -> {
                    userTransactionRepository.delete(ut);
                    userTransactionRepository.findById(ut.getUserId())
                            .ifPresent(u -> u.setAmount(u.getAmount() + ut.getAmount()));
                });
    }
}
