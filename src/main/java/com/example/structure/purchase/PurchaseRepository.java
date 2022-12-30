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

    @Query(value = "SELECT TYPE, ROUND(AVG(VALUE)\\:\\:NUMERIC, 2)  FROM\n" +
            "\t(SELECT purchase.type AS type, SUM(purchase.value) AS VALUE, DATE_TRUNC('month', dop) AS month FROM purchase FULL JOIN split ON split.id = purchase.split_id WHERE client_id = 1 GROUP BY TYPE, month ORDER BY MONTH, TYPE) AS tmp\n" +
            "GROUP BY TYPE ORDER BY type", nativeQuery = true)
    Set<String> findPurchaseTypeByAverageRelative(Long Id);

    @Query(value = "SELECT purchase.type AS TYPE, ROUND(SUM(purchase.value*COALESCE((100-split.weight)*0.01,1))\\:\\:numeric,2) AS VALUE, DATE_TRUNC('month', dop) AS month FROM purchase FULL JOIN split ON split.id = purchase.split_id WHERE client_id = 1 GROUP BY TYPE, month ORDER BY MONTH, TYPE", nativeQuery = true)
    Set<String> findPurchaseTypeByMonthMine(Long Id);

    @Query(value = "SELECT TYPE, ROUND(AVG(VALUE)\\:\\:NUMERIC, 2) FROM\n" +
            "\t(SELECT purchase.type AS TYPE, ROUND(SUM(purchase.value*COALESCE((100-split.weight)*0.01,1))\\:\\:numeric,2) AS VALUE, DATE_TRUNC('month', dop) AS month FROM purchase FULL JOIN split ON split.id = purchase.split_id WHERE client_id = 1 GROUP BY TYPE, month ORDER BY MONTH, TYPE) AS tmp\n" +
            "\tGROUP BY TYPE", nativeQuery = true)
    Set<String> findPurchaseTypeByAverageMine(Long Id);

    @Query(value = "SELECT purchase.type AS TYPE, ROUND(SUM(purchase.value*COALESCE((split.weight)*0.01,0))\\:\\:numeric,2) AS VALUE, DATE_TRUNC('month', dop) AS month FROM purchase FULL JOIN split ON split.id = purchase.split_id WHERE client_id = 1 GROUP BY TYPE, month ORDER BY MONTH, TYPE", nativeQuery = true)
    Set<String> findPurchaseTypeByMonthYour(Long Id);

    @Query(value = "SELECT TYPE, ROUND(AVG(VALUE)\\:\\:NUMERIC, 2) FROM\n" +
            "\t(SELECT purchase.type AS TYPE, ROUND(SUM(purchase.value*COALESCE((split.weight)*0.01,0))\\:\\:numeric,2) AS VALUE, DATE_TRUNC('month', dop) AS month FROM purchase FULL JOIN split ON split.id = purchase.split_id WHERE client_id = 1 GROUP BY TYPE, month ORDER BY MONTH, TYPE) AS tmp\n" +
            "\tGROUP BY TYPE\n", nativeQuery = true)
    Set<String> findPurchaseTypeByAverageYour(Long Id);

    @Query(value = "SELECT TYPE, SUM(VALUE), MONTH FROM \n" +
            "\t(SELECT purchase.client_id, purchase.type AS TYPE, \n" +
            "\t\tCASE \n" +
            "\t\t\tWHEN purchase.client_id=1 THEN\n" +
            "\t\t\t\tROUND(SUM(purchase.value*COALESCE((100-split.weight)*0.01,1))\\:\\:numeric,2) \n" +
            "\t\t\tWHEN purchase.client_id=2 THEN\n" +
            "\t\t\t\tROUND(SUM(purchase.value*COALESCE((split.weight)*0.01,0))\\:\\:numeric,2) \n" +
            "\t\tEND AS VALUE, \n" +
            "\t\tDATE_TRUNC('month', dop) AS MONTH FROM purchase FULL JOIN split ON split.id = purchase.split_id WHERE client_id = 1 OR \t\t\tclient_id=2 GROUP BY TYPE, MONTH, purchase.client_id ORDER BY MONTH, TYPE) AS tmp\n" +
            "GROUP BY TYPE, MONTH ORDER BY MONTH, TYPE;", nativeQuery = true)
    Set<String> findPurchaseTypeByMonthReal(Long Id);

    @Query(value = "SELECT TYPE, ROUND(AVG(VALUE)\\:\\:NUMERIC, 2) FROM\n" +
            "\t(SELECT TYPE, SUM(VALUE) AS VALUE, MONTH FROM \n" +
            "\t(SELECT purchase.client_id, purchase.type AS TYPE, \n" +
            "\t\tCASE \n" +
            "\t\t\tWHEN purchase.client_id=1 THEN\n" +
            "\t\t\t\tROUND(SUM(purchase.value*COALESCE((100-split.weight)*0.01,1))\\:\\:numeric,2) \n" +
            "\t\t\tWHEN purchase.client_id=2 THEN\n" +
            "\t\t\t\tROUND(SUM(purchase.value*COALESCE((split.weight)*0.01,0))\\:\\:numeric,2) \n" +
            "\t\tEND AS VALUE, \n" +
            "\t\tDATE_TRUNC('month', dop) AS MONTH FROM purchase FULL JOIN split ON split.id = purchase.split_id WHERE client_id = 1 OR \t\t\tclient_id=2 GROUP BY TYPE, MONTH, purchase.client_id ORDER BY MONTH, TYPE) AS tmp\n" +
            "GROUP BY TYPE, MONTH ORDER BY MONTH, TYPE) AS tmp\n" +
            "\tGROUP BY TYPE", nativeQuery = true)
    Set<String> findPurchaseTypeByAverageReal(Long Id);

    @Query(value = "SELECT purchase.type AS TYPE, SUM(purchase.value) AS VALUE, DATE_TRUNC('month', dop) AS MONTH FROM purchase FULL JOIN split ON split.id = purchase.split_id WHERE client_id = 1 OR client_id=2 GROUP BY TYPE, month ORDER BY  MONTH, TYPE", nativeQuery = true)
    Set<String> findPurchaseTypeByMonthCouple(Long Id);

    @Query(value = "SELECT TYPE, ROUND(AVG(VALUE)\\:\\:NUMERIC, 2) FROM\n" +
            "\t(SELECT purchase.type AS TYPE, SUM(purchase.value) AS VALUE, DATE_TRUNC('month', dop) AS MONTH FROM purchase FULL JOIN split ON split.id = purchase.split_id WHERE client_id = 1 OR client_id=2 GROUP BY TYPE, month ORDER BY  MONTH, TYPE) AS tmp\n" +
            "\tGROUP BY TYPE", nativeQuery = true)
    Set<String> findPurchaseTypeByAverageCouple(Long Id);

}
