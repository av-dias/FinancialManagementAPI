package utility.protection;

import com.example.structure.purchase.Purchase;
import com.example.structure.purchase.PurchaseService;
import com.example.structure.userclient.UserClient;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PurchaseProtection {
    private final PurchaseService purchaseService;

    public PurchaseProtection(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    public Optional<Purchase> hasPurchase(Long purchaseId) {
        //CHECK IF purchase IS DEFINED
        if (purchaseId == null)
            throw new IllegalStateException("No user defined.");
        Optional<Purchase> purchase = purchaseService.findPurchase(purchaseId);
        //CHECK IF purchaseId IS EXISTS
        if (!purchase.isPresent())
            throw new IllegalStateException("User does not exist.");
        return purchase;
    }

    public void updatePurchase(Purchase purchase){
        Optional<Purchase> _purchase = this.hasPurchase(purchase.getId());
        if(_purchase.isPresent()){
            purchaseService.savePurchase(purchase);
        }
    }
}
