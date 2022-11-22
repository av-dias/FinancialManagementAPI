package com.example.structure.transactions;

import com.example.structure.userclient.UserClient;
import com.example.structure.userclient.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.transaction.Transactional;
import java.time.LocalDate;
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
        userService.findUser(userId).orElseThrow(() -> new IllegalStateException("User does not exist."));

        //CHECK IF USER HAS TRANSACTIONS
        return transactionsRepository.findTransactionsSentByUser(userId).orElseThrow(() -> new IllegalStateException("User with id " + userId + " does not have transactions."));
    }

    @Transactional
    public void addNewTransactions(Long userId, Transactions transactions, String destination_email){
        //CHECK IF USER IS DEFINED
        if (userId == null)
            throw new IllegalStateException("No user defined.");
        //CHECK IF USER ALREADY EXISTS
        userService.findUser(userId).orElseThrow(() -> new IllegalStateException("User does not exist."));
        //CHECK IF DESTINATION EMAIL IS DEFINED
        if(destination_email==null)
            throw new IllegalStateException("No destination email defined.");
        //CHECK IF DESTINATION EMAIL EXISTS
        UserClient destination_user_id = userService.getUser(destination_email).orElseThrow(() -> new IllegalStateException("Email does not exist."));
        //CHECK IF TRANSACTION IS DEFINED
        if(transactions==null)
            throw new IllegalStateException("No transaction defined.");
        transactions.setUser_destination_id(destination_user_id.getId());
        //CHECK IG DATE OF PURCHASE EXISTS
        if(transactions.getDot() == null)
            transactions.setDot(LocalDate.now());

        try{
            transactionsRepository.save(transactions);
        }
        catch(Exception e){
            System.out.println(e);
        }
        userService.saveTransactionFromUser(transactions, userId);
    }
}
