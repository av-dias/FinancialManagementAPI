package com.example.structure.transactions;

import com.example.structure.purchase.Purchase;
import com.example.structure.userclient.UserClient;
import com.example.structure.userclient.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utility.protection.UserProtection;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Optional;
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
            if(transactionByMonth.containsKey(String.valueOf(month)))
                transactionByMonth.put(String.valueOf(month), (float) -t.getAmount() + (float) transactionByMonth.get(String.valueOf(month)));
            else
                transactionByMonth.put(String.valueOf(month), (float) -t.getAmount());
            totalTransactionAmount-=(float) t.getAmount();
        };

        for(Transactions t : receivedTransactions){
            int month = t.getDot().getMonthValue();
            if(transactionByMonth.containsKey(String.valueOf(month)))
                transactionByMonth.put(String.valueOf(month), (float) t.getAmount() + (float) transactionByMonth.get(String.valueOf(month)));
            else
                transactionByMonth.put(String.valueOf(month), (float) t.getAmount());
            totalTransactionAmount+=(float) t.getAmount();
        };
        transactions.put("per_month", transactionByMonth);
        transactions.put("total", totalTransactionAmount);
        return transactions;
    }

    public JSONObject getTransactionsList(Long userId){
        JSONObject transactions = new JSONObject();
        JSONObject transactionReceived = new JSONObject();

        Set<Transactions> sentTransactions = getTransactionsSentFromUser(userId);
        Set<Transactions> receivedTransactions = getTransactionsReceivedByUser(userId);

        transactions.put("sent", sentTransactions);
        transactions.put("received", receivedTransactions);
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

    public void addMobileTransactions(String details, Long userId) {
        JSONArray result = new JSONArray(details);
        final ObjectMapper mapper = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());

        result.forEach( jsonT -> {
            try{
                JSONParser jsonParser = new JSONParser();
                JSONObject jsonValue = (JSONObject) jsonParser.parse(jsonT.toString());

                String destinationEmail = (String) jsonValue.remove("user_destination_id");
                Transactions t = mapper.readValue(jsonValue.toString(), Transactions.class);
                addNewTransactions( userId, t, destinationEmail);
                System.out.println(t);
            } catch(ParseException e){
                throw new RuntimeException(e);
            } catch (JsonMappingException e) {
                throw new RuntimeException(e);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
