package com.example.paymentservice.repositories;

import com.example.paymentservice.Entities.UserTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTransactionRepository extends JpaRepository<UserTransaction,Integer> {
}
