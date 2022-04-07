package com.example.demo.user;

import com.example.demo.purchase.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT p FROM Purchase p WHERE p.email = ?1")
    Optional<Purchase> findPurchaseByType(String email);
}
