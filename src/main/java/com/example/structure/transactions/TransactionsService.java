package com.example.structure.transactions;

import com.example.structure.userclient.UserClient;
import com.example.structure.userclient.UserService;
import org.json.JSONObject;
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

    public JSONObject getTransactionsSent(Long userId){
        JSONObject transactions = new JSONObject();
        JSONObject transactionByMonth = new JSONObject();

        Set<Transactions> sentTransactions = getTransactionsSentFromUser(userId);
        Set<Transactions> receivedTransactions = getTransactionsReceivedByUser(userId);
        float totalTransactionAmount=0;

        for(Transactions t : sentTransactions)
        {
            int month = t.getDot().getMonthValue();
            if(transactionByMonth.has(String.valueOf(month)))
                transactionByMonth.put(String.valueOf(month), (float) -t.getAmount() + (float) transactionByMonth.get(String.valueOf(month)));
            else
                transactionByMonth.put(String.valueOf(month), (float) -t.getAmount());
            totalTransactionAmount-=(float) t.getAmount();
        };

        for(Transactions t : receivedTransactions){
            int month = t.getDot().getMonthValue();
            if(transactionByMonth.has(String.valueOf(month)))
                transactionByMonth.put(String.valueOf(month), (float) t.getAmount() + (float) transactionByMonth.get(String.valueOf(month)));
            else
                transactionByMonth.put(String.valueOf(month), (float) t.getAmount());
            totalTransactionAmount+=(float) t.getAmount();
        };
        transactions.put("per_month", transactionByMonth);
        transactions.put("total", totalTransactionAmount);
        return transactions;
    }

    @Transactional
    private Set<Transactions> getTransactionsSentFromUser(Long userId) {
        //CHECK IF USER ALREADY EXISTS
        userService.findUser(userId).orElseThrow(() -> new IllegalStateException("User does not exist."));

        //CHECK IF USER HAS TRANSACTIONS
        Set<Transactions> sentTransactions = transactionsRepository.findTransactionsSentByUser(userId).orElseThrow(() -> new IllegalStateException("User with id " + userId + " does not have transactions."));

        return sentTransactions;
    }

    @Transactional
    private Set<Transactions> getTransactionsReceivedByUser(Long userId) {
        //CHECK IF USER ALREADY EXISTS
        userService.findUser(userId).orElseThrow(() -> new IllegalStateException("User does not exist."));

        //CHECK IF USER HAS TRANSACTIONS
        Set<Transactions> receivedTransactions = transactionsRepository.findTransactionsReceivedByUser(userId).orElseThrow(() -> new IllegalStateException("User with id " + userId + " does not have transactions."));

        return receivedTransactions;
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
