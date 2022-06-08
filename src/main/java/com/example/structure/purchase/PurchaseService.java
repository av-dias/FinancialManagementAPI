package com.example.structure.purchase;

import com.example.structure.userclient.UserClient;
import com.example.structure.userclient.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@Component
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final UserService userService;

    @Autowired
    public PurchaseService(PurchaseRepository purchaseRepository, UserService userService){
        this.purchaseRepository = purchaseRepository;
        this.userService = userService;
    }

    public Set<Purchase> getPurchases(Long userId)
    {
        return userService.getPurchaseFromUser(userId);
    }

    public void addNewPurchase(Purchase purchase, Long userId) {
        //CHECK IF USER IS DEFINED
        if(userId==null)
            throw new IllegalStateException("No user defined.");
        Optional<UserClient> user = userService.findUser(userId);
        //CHECK IF USER IS EXISTS
        if(!user.isPresent())
            throw new IllegalStateException("User does not exist.");
        //CHECK IF PURCHASE IS DEFINED
        if(purchase==null)
            throw new IllegalStateException("No purchase defined.");
        //CHECK IG DATE OF PURCHASE EXISTS
        if(purchase.getDop()==null)
            purchase.setDop(LocalDate.now());

        purchaseRepository.save(purchase);
        userService.savePurchaseFromUser(purchase, userId);
    }

    public void deletePurchase(Long purchaseId) {
        boolean exists = purchaseRepository.existsById(purchaseId);
        if(!exists){
            throw new IllegalStateException("purchase with id " + purchaseId + " does not exists");
        }
        purchaseRepository.deleteById(purchaseId);
    }

    @Transactional
    public void updatePurchase(Long purchaseId, String type, String subType, Long value, LocalDate dop){
        Purchase purchase = purchaseRepository.findById(purchaseId).orElseThrow(() -> new IllegalStateException("purchase with id " + purchaseId + " does not exists"));
        if(type !=null && type.length()>0){
            purchase.setType(type);
        }

        if(subType !=null && subType.length()>0){
            purchase.setSubType(subType);
        }

        if(value != null){
            purchase.setValue(value);
        }

        if(dop != null){
            purchase.setDop(dop);
        }
    }
}
