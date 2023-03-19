package com.example.structure.purchase;

import com.example.structure.split.Split;
import com.example.structure.userclient.UserClient;
import com.example.structure.userclient.UserService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.transaction.Transactional;
import javax.xml.transform.Result;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@Component
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final UserService userService;

    @Autowired
    public PurchaseService(PurchaseRepository purchaseRepository, UserService userService) {
        this.purchaseRepository = purchaseRepository;
        this.userService = userService;
    }

    public Set<Purchase> getPurchases(Long userId) {
        return userService.getPurchaseFromUser(userId);
    }

    public Optional<Purchase> findPurchase(Long purchaseId) {
        return purchaseRepository.findById(purchaseId);
    }

    public Optional<Set<Purchase>> findPurchasesFromSplits(Set<Split> s){
        return purchaseRepository.findPurchaseBySplits(s);
    }

    public void addNewPurchase(Purchase purchase, Long userId) {
        //CHECK IF USER IS DEFINED
        if (userId == null)
            throw new IllegalStateException("No user defined.");
        Optional<UserClient> user = userService.findUser(userId);
        //CHECK IF USER IS EXISTS
        if (!user.isPresent())
            throw new IllegalStateException("User does not exist.");
        //CHECK IF PURCHASE IS DEFINED
        if (purchase == null)
            throw new IllegalStateException("No purchase defined.");
        //CHECK IG DATE OF PURCHASE EXISTS
        if (purchase.getDop() == null)
            purchase.setDop(LocalDate.now());

        purchaseRepository.save(purchase);
        userService.savePurchaseFromUser(purchase, userId);
    }

    public void deletePurchase(Long purchaseId) {
        boolean exists = purchaseRepository.existsById(purchaseId);
        if (!exists) {
            throw new IllegalStateException("purchase with id " + purchaseId + " does not exists");
        }
        purchaseRepository.deleteById(purchaseId);
    }

    @Transactional
    public void updatePurchase(Long purchaseId, Purchase newPurchase) {
        Purchase purchase = purchaseRepository.findById(purchaseId).orElseThrow(() -> new IllegalStateException("purchase with id " + purchaseId + " does not exists"));
        String name = newPurchase.getName();
        String type = newPurchase.getType();
        String subType = newPurchase.getSubType();
        Float value = newPurchase.getValue();
        LocalDate dop = newPurchase.getDop();

        if (name != null && name.length() > 0) {
            purchase.setName(name);
        }

        if (type != null && type.length() > 0) {
            purchase.setType(type);
        }

        if (subType != null && subType.length() > 0) {
            purchase.setSubType(subType);
        }

        if (value != null) {
            purchase.setValue(value);
        }

        if (dop != null) {
            purchase.setDop(dop);
        }
    }

    public void savePurchase(Purchase purchase) {
        purchaseRepository.save(purchase);
    }

    public Optional<Purchase> getPurchaseFromSplit(Split split){
        Optional<Purchase> _purchase = purchaseRepository.findPurchaseBySplit(split);
        return _purchase;
    }

    public String getUserByPurchase(Purchase p){
        return purchaseRepository.findUserbyPurchaseId(p.getId());
    }

    private JSONObject formatCalcMonthToJson(JSONObject result, Set<String> res ){
        res.forEach(row -> {
            JSONObject temp = new JSONObject();
            String[] rowTokenizer = row.split(",");
            String[] tmpDate = rowTokenizer[2].split("[ -]");
            String dayFormat = tmpDate[0] + tmpDate[1];

            if(result.has(dayFormat)){
                temp = (JSONObject) result.get(dayFormat);
                temp.put(rowTokenizer[0], rowTokenizer[1]);
            }else{
                temp.put(rowTokenizer[0], rowTokenizer[1]);
            }
            result.put(dayFormat,temp);
        });

        return result;
    }

    private JSONObject formatResultDate(JSONObject result, Set<String> res ){
        res.forEach(row -> {
            JSONObject temp = new JSONObject();
            String[] rowTokenizer = row.split(",");
            String[] tmpDate = rowTokenizer[1].split("[ -]");
            String dayFormat = tmpDate[0] + tmpDate[1];

            result.put(dayFormat,rowTokenizer[0]);
        });

        return result;
    }
    private JSONObject formatResultoJson(JSONObject result, Set<String> res ){
            result.put("result",res.toArray()[0]);
        return result;
    }


    private JSONObject formatCalcAvgToJson(JSONObject result, Set<String> res ){
        res.forEach(row -> {
            String[] rowTokenizer = row.split(",");

            result.put(rowTokenizer[0],rowTokenizer[1]);
        });

        return result;
    }

    /********** ********** **********
     Type and Average by Type Monthly Spendings Chart
     ********** ********** ********** */

    public JSONObject calcPurchaseTypeByMonthRelative(Long userId){
        JSONObject result = new JSONObject();
        Set<String> res = purchaseRepository.findPurchaseTypeByMonthRelative(userId);

        return formatCalcMonthToJson(result, res);
    }

    public JSONObject calcPurchaseTypeByMonthYour(Long userId){
        JSONObject result = new JSONObject();
        Set<String> res = purchaseRepository.findPurchaseTypeByMonthYour(userId);

        return formatCalcMonthToJson(result, res);
    }

    public JSONObject calcPurchaseTypeByMonthReal(Long userId){
        JSONObject result = new JSONObject();
        Set<String> res = purchaseRepository.findPurchaseTypeByMonthReal(userId);

        return formatCalcMonthToJson(result, res);
    }

    public JSONObject calcPurchaseTypeByMonthCouple(Long userId){
        JSONObject result = new JSONObject();
        Set<String> res = purchaseRepository.findPurchaseTypeByMonthCouple(userId);

        return formatCalcMonthToJson(result, res);
    }

    public JSONObject calcPurchaseTypeByAverageRelative(Long userId){
        JSONObject result = new JSONObject();
        Set<String> res = purchaseRepository.findPurchaseTypeByAverageRelative(userId);

        return formatCalcAvgToJson(result, res);
    }

    public JSONObject calcPurchaseTypeByAverageYour(Long userId){
        JSONObject result = new JSONObject();
        Set<String> res = purchaseRepository.findPurchaseTypeByAverageYour(userId);

        return formatCalcAvgToJson(result, res);
    }

    public JSONObject calcPurchaseTypeByAverageReal(Long userId){
        JSONObject result = new JSONObject();
        Set<String> res = purchaseRepository.findPurchaseTypeByAverageReal(userId);

        return formatCalcAvgToJson(result, res);
    }

    public JSONObject calcPurchaseTypeByAverageCouple(Long userId){
        JSONObject result = new JSONObject();
        Set<String> res = purchaseRepository.findPurchaseTypeByAverageCouple(userId);

        return formatCalcAvgToJson(result, res);
    }

    /********** ********** **********
     Monthly Spendings Chart
     ********** ********** ********** */
    public JSONObject calcMonthlySpendingRelative(Long userId) throws SQLException {
        JSONObject result = new JSONObject();
        Set<String> res = purchaseRepository.findMonthlyRelativePurchase(userId);

        return formatResultDate(result,res);
    }

    public JSONObject calcMonthlySpendingReal(Long userId){
        JSONObject result = new JSONObject();
        Set<String> res = purchaseRepository.findMonthlyRealPurchase(userId);

        return formatResultDate(result, res);
    }

    public JSONObject calcMonthlySpendingCouple(Long userId){
        JSONObject result = new JSONObject();
        Set<String> res = purchaseRepository.findMonthlyCouplePurchase(userId);

        return formatResultDate(result, res);
    }

    /********** ********** **********
     Monthly Cumulative Earnings
     ********** ********** ********** */
    public JSONObject calcMonthlyCumulativeEarning(Long userId){
        JSONObject result = new JSONObject();
        Set<String> res = purchaseRepository.findMonthlyEarning(userId);

        return formatResultDate(result, res);
    }
}
