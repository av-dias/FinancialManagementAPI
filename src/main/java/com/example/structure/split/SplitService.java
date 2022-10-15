package com.example.structure.split;

import com.example.structure.purchase.Purchase;
import com.example.structure.purchase.PurchaseService;
import com.example.structure.userclient.UserClient;
import com.example.structure.userclient.UserService;
import com.fasterxml.jackson.annotation.JsonBackReference;
import org.apache.catalina.User;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import utility.protection.UserProtection;
import utility.protection.PurchaseProtection;

import javax.transaction.Transactional;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.*;

@Component
public class SplitService {

    private final SplitRepository splitRepository;
    private final UserProtection userProtection;
    private final PurchaseProtection purchaseProtection;

    @Autowired
    public SplitService(SplitRepository splitRepository, UserService userService, PurchaseService purchaseService) {
        this.splitRepository = splitRepository;
        this.userProtection = new UserProtection(userService);
        this.purchaseProtection = new PurchaseProtection(purchaseService);
    }

    public Set<Split> getUsersSplits(Long userId) {
        Optional<UserClient> _userClient = userProtection.hasUser(userId);
        if (_userClient.isPresent()) {
            Set<Purchase> purchases = _userClient.get().getPurchases();
            Set<Split> split = new HashSet<Split>();
            purchases.forEach(purchase -> {
                if (purchase.getSplit() != null) {
                    split.add(purchase.getSplit());
                }
            });
            return split;
        }
        return null;
    }

    public Set<Split> getSplit(Long userId) {
        Optional<Set<Split>> _split = splitRepository.findSplitbyUser(userId);
        if (_split.isPresent()) {
            return _split.get();
        }
        return null;
    }

    public Set<Purchase> getPurchasesFromSplit(Long userId) {
        Set<Split> split = getSplit(userId);
        Set<Purchase> listOfPurchases = new HashSet<Purchase>();
        if (!split.isEmpty()) {
            split.forEach(s -> {
                Optional<Purchase> _purchase = purchaseProtection.getPurchaseFromSplit(s);
                if (_purchase.isPresent()) {
                    listOfPurchases.add(_purchase.get());
                }
            });
        }
        return listOfPurchases;
    }

    // Get purchases and splits by type and per month
    public JSONObject getPurchasesSplitbyType(Long userId, int month_backtrack){
        Optional<UserClient> _userClient = userProtection.hasUser(userId);

        Set<Purchase> p = getPurchasesFromSplit(userId);
        JSONObject purchaseByType = userProtection.purchasesByType(userId, month_backtrack, p);
        return purchaseByType;
    }

    public void saveNewSplit(Long userId, Long purchaseId, int weight, String userEmail) {
        Optional<UserClient> _userClient = userProtection.hasUser(userId);
        Optional<Purchase> _purchase = purchaseProtection.hasPurchase(purchaseId);
        //VERIFY IF USER AND PURCHASE EXIST AND USER HAS PURCHASE
        if (_userClient.isPresent() && _purchase.isPresent() && _userClient.get().hasPurchase(_purchase.get())) {
            // VERIFY SPLIT VALUES
            Optional<UserClient> _splitUserClient = userProtection.hasUser(userEmail);
            if (_splitUserClient.isPresent() && weight >= 0) {
                // CREATE NEW SPLIT and UPDATE PURCHASE WITH SPLIT
                Split split = new Split(weight, _splitUserClient.get().getId());
                splitRepository.save(split);
                _purchase.get().setSplit(split);
                purchaseProtection.updatePurchase(_purchase.get());
            }
        }
    }

    public JSONObject getSplitStats(Long userId) {
        Optional<UserClient> _userClient = userProtection.hasUser(userId);
        JSONObject stats;
        HashMap<Long, String> uNames = new HashMap<Long, String>();
        // If user exists
        if (_userClient.isPresent()) {
            UserClient userClient = _userClient.get();

            //Get split information that I own
            stats = userClient.getSplitInformation();
            JSONArray array = (JSONArray) stats.get("Self");

            // Usernames index list
            for (int i = 0; i < array.length(); i++) {
                int charIndex = array.get(i).toString().indexOf("=");
                int id = Integer.parseInt(array.get(i).toString().substring(0, charIndex));
                UserClient user = userProtection.hasUser((long) id).get();
                uNames.put((long) id, user.getName());
            }


            // Get split information that I was given
            Optional<Set<Split>> mySplits = splitRepository.findSplitbyUser(userId);
            Set<Purchase> purchaseGiven = purchaseProtection.getPurchaseBySplits(mySplits.get()).get();

            Map<String, JSONObject> mapUsers = new HashMap<>();
            purchaseGiven.forEach(pG -> {
                Long sUser = Long.parseLong(purchaseProtection.getUserbyPurchase(pG));
                String splitUser = sUser.toString();
                if (mapUsers.containsKey(splitUser)) {
                    JSONObject json = mapUsers.get(splitUser);
                    float total = (float) json.get("total") + pG.getValue();
                    float iShare = (float) json.get("iShare") + (pG.getValue() * (pG.getSplit().getWeight()) / 100);
                    float yShare = (float) json.get("yShare") + (pG.getValue() * (100 - pG.getSplit().getWeight()) / 100);

                    mapUsers.put(splitUser, new JSONObject().put("total", total).put("iShare", iShare).put("yShare", yShare));
                } else {
                    float total = (float) pG.getValue();
                    float iShare = (float) total * (pG.getSplit().getWeight()) / 100;
                    float yShare = (float) total * (100 - pG.getSplit().getWeight()) / 100;
                    mapUsers.put(splitUser, new JSONObject().put("total", total).put("iShare", iShare).put("yShare", yShare));
                }
            });

            stats.put("Names", uNames);
            stats.put("Given", mapUsers.entrySet());

            return stats;
        }
        return null;
    }

    @Transactional
    public void updateSplit(Long splitId, Split newSplit) {
        Split split = splitRepository.findById(splitId).orElseThrow(() -> new IllegalStateException("split with id " + splitId + " does not exists"));
        Long userClientId = newSplit.getUserClientId();
        int weight = newSplit.getWeight();

        if (userClientId != null && userClientId > 0) {
            split.setUserClientId(userClientId);
        }

        if (weight >= 0 && weight <=100) {
            split.setWeight(weight);
        }
    }
}
