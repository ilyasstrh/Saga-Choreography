package com.example.paymentservice.Entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTransaction {
    @Id
    private Integer orderId;
    private Integer userId;
    private double amount;
}
