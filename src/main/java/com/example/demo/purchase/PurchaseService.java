package com.example.demo.purchase;

import com.example.demo.userclient.UserClient;
import com.example.demo.userclient.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Component
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final UserRepository userRepository;

    @Autowired
    public PurchaseService(PurchaseRepository purchaseRepository, UserRepository userRepository){
        this.purchaseRepository = purchaseRepository;
        this.userRepository = userRepository;
    }

    public List<Purchase> getPurchases()
    {
        return purchaseRepository.findAll();
    }

    public void addNewPurchase(Purchase purchase) {
        UserClient userClient = purchase.getUserClient();
        //CHECK IF USER IS DEFINED
        if(purchase.getUserClient()==null)
            throw new IllegalStateException("No user defined.");
        //CHECK IF USER EXISTS
        boolean exists = userRepository.existsById(userClient.getId());
        if(!exists)
            throw new IllegalStateException("The user defined does not exists.");
        if(purchase.getDop()==null)
            purchase.setDop(LocalDate.now());
        purchaseRepository.save(purchase);
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
