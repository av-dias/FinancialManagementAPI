package com.example.structure.userclient;

import com.example.structure.income.Income;
import com.example.structure.purchase.Purchase;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.apache.catalina.User;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserClient> getUserClients() {
        return userRepository.findAll();
    }

    public Optional<UserClient> findUser(Long userId) {
        return userRepository.findUserClientById(userId);
    }

    public UserClient getUser(String username) {
        return userRepository.findUserClientByEmail(username);
    }

    @Transactional
    public void savePurchaseFromUser(Purchase purchase, Long userId) {
        //CHECK IF USER ALREADY EXISTS
        UserClient user = this.findUser(userId).orElseThrow(() -> new IllegalStateException("User does not exist."));
        //ADDS PURCHASE
        user.addPurchase(purchase);
    }

    @Transactional
    public void saveIncomeFromUser(Income income, Long userId) {
        //CHECK IF USER ALREADY EXISTS
        UserClient user = this.findUser(userId).orElseThrow(() -> new IllegalStateException("User does not exist."));
        //ADDS Income
        user.addIncome(income);
    }
    public Set<Purchase> getPurchaseFromUser(Long userId) {
        //CHECK IF USER ALREADY EXISTS
        UserClient user = this.findUser(userId).orElseThrow(() -> new IllegalStateException("User does not exist."));
        //get PURCHASE
        return user.getPurchases();
    }

    public Set<Income> getIncomeFromUser(Long userId) {
        //CHECK IF USER ALREADY EXISTS
        UserClient user = this.findUser(userId).orElseThrow(() -> new IllegalStateException("User does not exist."));
        //get Income
        return user.getIncome();
    }

    public JSONObject getPurchaseStatsFromUser(Long userId) {
        //CHECK IF USER ALREADY EXISTS
        UserClient user = this.findUser(userId).orElseThrow(() -> new IllegalStateException("User does not exist."));

        int totalSpendings = user.getTotalPurchases();
        int monthSpendings = user.getTotalMonthPurchases();
        JSONObject stats = new JSONObject();
        stats.put("total_spendings", totalSpendings);
        stats.put("month_spendings", monthSpendings);

        return stats;
    }

    public void addNewUser(UserClient userClient) {
        //CHECK IF USER ALREADY EXISTS
        UserClient checkUser = userRepository.findUserClientByEmail(userClient.getEmail());
        if (checkUser != null) {
            throw new IllegalStateException("Email Taken.");
        }
        //CHECK IF DATE OF CREATION EXISTS
        if (userClient.getDoc() == null) {
            userClient.setDoc(LocalDateTime.now());
            userClient.setDou(userClient.getDoc());
        }
        userRepository.save(userClient);
    }

    public void deleteUser(Long userId) {
        boolean exists = userRepository.existsById(userId);
        if (!exists) {
            throw new IllegalStateException("user with id " + userId + " does not exists");
        }
        userRepository.deleteById(userId);
    }

    @Transactional
    public void updateUser(Long userId, String name, String email, String password, LocalDateTime dou) {
        UserClient userClient = userRepository.findById(userId).orElseThrow(() -> new IllegalStateException("user with id " + userId + " does not exists"));
        if (name != null && name.length() > 0) {
            userClient.setName(name);
        }

        if (email != null && email.length() > 0) {
            userClient.setEmail(email);
        }

        if (password != null && password.length() > 0) {
            userClient.setPassword(password);
        }

        if (dou != null) {
            userClient.setDou(dou);
        }
    }
}