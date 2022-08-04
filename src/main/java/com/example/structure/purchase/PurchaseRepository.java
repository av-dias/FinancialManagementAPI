package com.example.structure.purchase;

import com.example.structure.split.Split;
import com.example.structure.userclient.UserClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    @Query("SELECT p FROM Purchase p WHERE p.type = ?1")
    Optional<Purchase> findPurchaseByType(String type);

    @Query("SELECT p FROM Purchase p WHERE p.split = ?1")
    Optional<Purchase> findPurchaseBySplit(Split split);

    @Query("SELECT p FROM Purchase p WHERE p.split in ?1")
    Optional<Set<Purchase>> findPurchaseBySplits(Set<Split> split);

    @Query(value = "SELECT client_id FROM Purchase WHERE id = ?1", nativeQuery = true)
    String findUserbyPurchaseId(Long Id);
}
