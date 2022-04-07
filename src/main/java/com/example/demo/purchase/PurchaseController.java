package com.example.demo.purchase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping(path= "api/v1/purchase")
public class PurchaseController {

    private final PurchaseService purchaseService;

    @Autowired
    public PurchaseController(PurchaseService purchaseService)
    {
        this.purchaseService = purchaseService;
    }

    @GetMapping
    public List<Purchase> getPurchases()
    {
        return purchaseService.getPurchases();
    }

    @PostMapping
    public void registerNewPurchase(@RequestBody Purchase purchase){
        purchaseService.addNewPurchase(purchase);
    }

    @DeleteMapping(path = "{purchaseId}")
    public void deletePurchase(@PathVariable("purchaseId") Long purchaseId){
        purchaseService.deletePurchase(purchaseId);
    }

    @PutMapping(path = "{purchaseId}")
    public void updatePurchase(
            @PathVariable("purchaseId") Long purchaseId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String subType,
            @RequestParam(required = false) Long value,
            @RequestParam(required = false) String dop){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/d");
        purchaseService.updatePurchase(purchaseId, type, subType, value, LocalDate.parse(dop, formatter));
    };
}
