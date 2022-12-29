package com.example.structure.purchase;

import com.example.structure.split.Split;
import org.json.JSONObject;
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

    @Query(value = "SELECT purchase.type AS type, SUM(purchase.value) AS VALUE, DATE_TRUNC('month', dop) AS month FROM purchase FULL JOIN split ON split.id = purchase.split_id WHERE client_id = 1 GROUP BY TYPE, month ORDER BY MONTH, type", nativeQuery = true)
    Set<String> findPurchaseTypeByMonthRelative(Long Id);

    @Query(value = "SELECT purchase.type AS TYPE, ROUND(SUM(purchase.value*COALESCE((100-split.weight)*0.01,1))::numeric,2) AS VALUE, DATE_TRUNC('month', dop) AS month FROM purchase FULL JOIN split ON split.id = purchase.split_id WHERE client_id = 1 GROUP BY TYPE, month ORDER BY MONTH, TYPE", nativeQuery = true)
    Set<String> findPurchaseTypeByMonthMine(Long Id);
}
