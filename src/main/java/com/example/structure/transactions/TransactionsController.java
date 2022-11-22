package com.example.structure.transactions;

import com.example.structure.userclient.UserClient;
import com.example.structure.userclient.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin("*")
@Slf4j
@RequestMapping(path = "api/v1/transactions")
public class TransactionsController {
    private final TransactionsService transactionsService;

    @Autowired
    public TransactionsController(TransactionsService transactionsService){ this.transactionsService = transactionsService;}

    @GetMapping(path = "user/{userId}")
    public Set<Transactions> getUserClients(@PathVariable("userId") Long userId) {
        return transactionsService.getTransactionsSent(userId);
    }

    @PostMapping(path = "user/{userId}")
    public void registerNewTransaction(@RequestBody Transactions transactions,
                                       @RequestParam String destination_email,
                                       @PathVariable("userId") Long userId) {
        transactionsService.addNewTransactions(userId, transactions, destination_email);
    }
}
