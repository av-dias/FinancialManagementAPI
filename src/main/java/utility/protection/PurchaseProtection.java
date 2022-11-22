package utility.protection;

import com.example.structure.purchase.Purchase;
import com.example.structure.purchase.PurchaseService;
import com.example.structure.split.Split;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

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

    public void updatePurchase(Purchase purchase) {
        Optional<Purchase> _purchase = this.hasPurchase(purchase.getId());
        if (_purchase.isPresent()) {
            purchaseService.savePurchase(purchase);
        }
    }

    public Optional<Purchase> getPurchaseFromSplit(Split split) {
        return purchaseService.getPurchaseFromSplit(split);
    }

    public Optional<Set<Purchase>> getPurchaseBySplits(Set<Split> split) {
        return purchaseService.findPurchasesFromSplits(split);
    }

    public String getUserbyPurchase(Purchase p){
        return purchaseService.getUserByPurchase(p);
    }
}
