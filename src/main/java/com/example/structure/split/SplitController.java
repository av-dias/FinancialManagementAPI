package com.example.structure.split;

import com.example.structure.purchase.Purchase;
import com.example.structure.purchase.PurchaseService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/split/")
public class SplitController {
    private final SplitService splitService;

    @Autowired
    public SplitController(SplitService splitService) {
        this.splitService = splitService;
    }

    //Checks with whom I have split the purchases
    @GetMapping(path = "users/user/{userId}")
    public Set<Split> getUsersSplit(@PathVariable("userId") Long userId) {
        return splitService.getUsersSplits(userId);
    }

    //Checks my own splits
    @GetMapping(path = "user/{userId}")
    public Set<Purchase> getSplit(@PathVariable("userId") Long userId) {
        return splitService.getPurchasesFromSplit(userId);
    }

    //Create new split purchase
    @PostMapping(path = "user/{userId}/purchase/{purchaseId}")
    public void saveNewSplit(@PathVariable("userId") Long userId, @PathVariable("purchaseId") Long purchaseId, @RequestBody String data) {
        JSONObject json = new JSONObject(data);
        int weight = Integer.parseInt((String) json.get("weight"));
        String userEmail = (String) json.get("userEmail");
        splitService.saveNewSplit(userId, purchaseId, weight, userEmail);
    }

    //Information about my split status
    @GetMapping(path = "users/user/{userId}/stats")
    public String getSplitStats(@PathVariable("userId") Long userId){
        JSONObject json = splitService.getSplitStats(userId);
        return json.toString();
    }
}
