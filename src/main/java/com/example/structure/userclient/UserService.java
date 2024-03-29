package com.example.structure.userclient;

import com.example.structure.income.Income;
import com.example.structure.purchase.Purchase;
import com.example.structure.transactions.Transactions;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@Service
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

    public Optional<UserClient> getUser(String username) {
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

    @Transactional
    public void saveTransactionFromUser(Transactions transactions, Long userId){
        //CHECK IF USER ALREADY EXISTS
        UserClient user = this.findUser(userId).orElseThrow(() -> new IllegalStateException("User does not exist."));
        //ADDS Income
        user.addTransactions(transactions);
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

    public JSONObject getPurchaseStatsFromUser(Long userId, int todayMonth) {
        //CHECK IF USER ALREADY EXISTS
        UserClient user = this.findUser(userId).orElseThrow(() -> new IllegalStateException("User does not exist."));
        Calendar calendar = Calendar.getInstance();
        //int todayMonth = calendar.get(Calendar.MONTH) + 1;

        float totalSpendings = user.getTotalPurchases();
        float totalSavings = user.getTotalSavings();
        float monthSpendings = user.getTotalMonthPurchases(todayMonth);
        float monthSavings = user.getMonthSavings(todayMonth);
        JSONObject purchasesByType = user.getMonthPurchasesbyType(todayMonth);
        JSONObject averagePurchasesbyType = user.getAveragePurchasesbyType();
        JSONObject purchasesByMonth = user.getMonthsPurchases();
        JSONObject savingsByMonth = user.getMonthsSavings();

        JSONObject stats = new JSONObject();
        stats.put("total_spendings", totalSpendings);
        stats.put("total_savings", totalSavings);
        stats.put("month_spendings", monthSpendings);
        stats.put("month_savings", monthSavings);
        stats.put("purchases_by_type", purchasesByType);
        stats.put("av_purchases_by_type", averagePurchasesbyType);
        stats.put("purchases_by_month", purchasesByMonth);
        stats.put("savings_by_month", savingsByMonth);

        return stats;
    }

    public JSONObject purchasesByType(Long userId,  int todayMonth, Set<Purchase> p){
        //CHECK IF USER ALREADY EXISTS
        UserClient user = this.findUser(userId).orElseThrow(() -> new IllegalStateException("User does not exist."));

        JSONObject purchaseByType = new JSONObject();
        purchaseByType.put("current", user.getMonthPurchasesbyType(todayMonth, p));
        purchaseByType.put("average",user.getAveragePurchasesbyType(p));

        return purchaseByType;
    }

    public void addNewUser(UserClient userClient) {
        //CHECK IF USER ALREADY EXISTS
        Optional<UserClient> checkUser = userRepository.findUserClientByEmail(userClient.getEmail());
        if (checkUser.isPresent() && checkUser.get() != null) {
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