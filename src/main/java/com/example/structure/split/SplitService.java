package com.example.structure.split;

import com.example.structure.purchase.Purchase;
import com.example.structure.purchase.PurchaseService;
import com.example.structure.userclient.UserClient;
import com.example.structure.userclient.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utility.protection.UserProtection;
import utility.protection.PurchaseProtection;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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

    public Set<Split> getSplit(Long userId){
        Optional<Set<Split>> _split = splitRepository.findSplitbyUser(userId);
        if(_split.isPresent()){
            return _split.get();
        }
        return null;
    }

    public void saveNewSplit(Long userId, Long purchaseId, Split split) {
        Optional<UserClient> _userClient = userProtection.hasUser(userId);
        Optional<Purchase> _purchase = purchaseProtection.hasPurchase(purchaseId);
        //VERIFY IF USER AND PURCHASE EXIST AND USER HAS PURCHASE
        if (_userClient.isPresent() && _purchase.isPresent() && _userClient.get().hasPurchase(_purchase.get())) {
            // VERIFY SPLIT VALUES
            Long splitUser = split.getUserClientId();
            int weight = split.getWeight();
            Optional<UserClient> _splitUserClient = userProtection.hasUser(splitUser);
            if (_splitUserClient.isPresent() && weight >= 0) {
                // CREATE NEW SPLIT and UPDATE PURCHASE WITH SPLIT
                splitRepository.save(split);
                _purchase.get().setSplit(split);
                purchaseProtection.updatePurchase(_purchase.get());
            }
        }
    }
}
