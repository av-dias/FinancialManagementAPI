package com.example.structure.transactions;

import com.example.structure.userclient.UserClient;
import com.example.structure.userclient.UserService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin("*")
@Slf4j
@RequestMapping(path = "api/v1/transactions/")
public class TransactionsController {
    private final TransactionsService transactionsService;

    @Autowired
    public TransactionsController(TransactionsService transactionsService){ this.transactionsService = transactionsService;}

    @GetMapping(path = "user/{userId}")
    public String getUserClients(@PathVariable("userId") Long userId) {
        return transactionsService.getTransactionsSent(userId).toString();
    }

    @GetMapping(path = "list/user/{userId}")
    public String getTransactionList(@PathVariable("userId") Long userId) {
        return transactionsService.getTransactionsList(userId).toString();
    }


    @PostMapping(path = "user/{userId}")
    public void registerNewTransaction(@RequestBody Transactions transactions,
                                       @RequestParam String destination_email,
                                       @PathVariable("userId") Long userId) {
        transactionsService.addNewTransactions(userId, transactions, destination_email);
    }

    @PostMapping(path = "mobile/user/{userId}/update/transactions")
    public String registerMobilePurchases(@RequestBody String details, @PathVariable("userId") Long userId) {
        transactionsService.addMobileTransactions(details, userId);

        return "{'rowCount': 'success'}";
    }
}
