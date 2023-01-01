package com.example.paymentservice.repositories;

import com.example.paymentservice.Entities.UserBalance;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBalanceRepository extends JpaRepository<UserBalance, Integer> {
}
