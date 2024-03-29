package com.example.structure.income;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "api/v1/income/")
public class IncomeController {

    private final IncomeService incomeService;

    @Autowired
    public IncomeController(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    @GetMapping(path = "user/{userId}")
    public Set<Income> getIncome(@PathVariable("userId") Long userId) {
        return incomeService.getIncome(userId);
    }

    @PostMapping(path = "user/{userId}")
    public void registerNewIncome(@RequestBody Income income,
                                  @PathVariable("userId") Long userId) {
        System.out.println(income);
        incomeService.addNewIncome(income, userId);
    }

    @DeleteMapping(path = "{incomeId}")
    public void deletePurchase(@PathVariable("incomeId") Long incomeId) {
        incomeService.deleteIncome(incomeId);
    }

    @PutMapping(path = "{incomeId}")
    public void updatePurchase(
            @PathVariable("incomeId") Long incomeId,
            @RequestBody Income income) {
        incomeService.updateIncome(incomeId, income);
    }
}
