package com.example.structure.transactions;

import com.example.structure.userclient.UserClient;
import com.example.structure.userclient.UserService;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Set;

@Component
public class TransactionsService {

    private final TransactionsRepository transactionsRepository;

    private final UserService userService;

    @Autowired
    public TransactionsService(TransactionsRepository transactionsRepository, UserService userService){
        this.transactionsRepository = transactionsRepository;
        this.userService = userService;
    }

    public Set<Transactions> getTransactionsSent(Long userId){
        return getTransactionsSentFromUser(userId);
    }

    @Transactional
    private Set<Transactions> getTransactionsSentFromUser(Long userId) {
        //CHECK IF USER ALREADY EXISTS
        UserClient user = userService.findUser(userId).orElseThrow(() -> new IllegalStateException("User does not exist."));

        //CHECK IF USER HAS TRANSACTIONS
        Set<Transactions> sentTransactions = transactionsRepository.findTransactionsSentByUser(userId).orElseThrow(() -> new IllegalStateException("User with id " + userId + " does not have transactions."));
        return sentTransactions;
    }
}
