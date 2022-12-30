package com.example.structure.purchase;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "api/v1/purchase/")
public class PurchaseController {

    private final PurchaseService purchaseService;

    @Autowired
    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @GetMapping(path = "user/{userId}")
    public Set<Purchase> getPurchases(@PathVariable("userId") Long userId) {
        return purchaseService.getPurchases(userId);
    }

    @PostMapping(path = "user/{userId}")
    public void registerNewPurchase(@RequestBody Purchase purchase,
                                    @PathVariable("userId") Long userId) {
        purchaseService.addNewPurchase(purchase, userId);
    }

    @DeleteMapping(path = "{purchaseId}")
    public void deletePurchase(@PathVariable("purchaseId") Long purchaseId) {
        purchaseService.deletePurchase(purchaseId);
    }

    @PutMapping(path = "{purchaseId}")
    public void updatePurchase(
            @PathVariable("purchaseId") Long purchaseId,
            @RequestBody Purchase purchase) {
        purchaseService.updatePurchase(purchaseId, purchase);
    }

    @GetMapping(path= "user/{userId}/stats")
    public String updatePurchase(
            @PathVariable("userId") Long userId){
        JSONObject stats = new JSONObject();
        stats.put("purchaseTypeByMonthRelative", purchaseService.calcPurchaseTypeByMonthRelative(userId));
        stats.put("purchaseTypeByMonthMine", purchaseService.calcPurchaseTypeByMonthMine(userId));
        stats.put("purchaseTypeByMonthYour", purchaseService.calcPurchaseTypeByMonthYour(userId));
        stats.put("purchaseTypeByMonthReal", purchaseService.calcPurchaseTypeByMonthReal(userId));
        stats.put("purchaseTypeByMonthCouple", purchaseService.calcPurchaseTypeByMonthCouple(userId));
        stats.put("purchaseTypeByAvgRelative", purchaseService.calcPurchaseTypeByAverageRelative(userId));
        stats.put("purchaseTypeByAvgMine", purchaseService.calcPurchaseTypeByAverageMine(userId));
        stats.put("purchaseTypeByAvgYour", purchaseService.calcPurchaseTypeByAverageYour(userId));
        stats.put("purchaseTypeByAvgReal", purchaseService.calcPurchaseTypeByAverageReal(userId));
        stats.put("purchaseTypeByAvgCouple", purchaseService.calcPurchaseTypeByAverageCouple(userId));

        return stats.toString();
    }
}
