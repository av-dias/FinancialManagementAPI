package com.example.structure.transactions;

import com.example.structure.purchase.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TransactionsRepository extends JpaRepository<Transactions, Long> {
    @Query("SELECT t FROM Transactions t WHERE t.user_origin_id = ?1")
    Optional<Set<Transactions>> findTransactionsSentByUser(Long id);

    @Query("SELECT t FROM Transactions t WHERE t.user_destination_id = ?1")
    Optional<Set<Transactions>> findTransactionsReceivedByUser(Long id);
}
