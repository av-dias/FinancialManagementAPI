package com.example.structure.income;

import com.example.structure.userclient.UserClient;
import com.example.structure.userclient.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@Component
public class IncomeService {

    private final IncomeRepository incomeRepository;
    private final UserService userService;

    @Autowired
    public IncomeService(IncomeRepository incomeRepository, UserService userService) {
        this.incomeRepository = incomeRepository;
        this.userService = userService;
    }

    public Set<Income> getIncome(Long userId) {
        return userService.getIncomeFromUser(userId);
    }

    public void addNewIncome(Income income, Long userId) {
        //CHECK IF USER IS DEFINED
        if (userId == null)
            throw new IllegalStateException("No user defined.");
        Optional<UserClient> user = userService.findUser(userId);
        //CHECK IF USER IS EXISTS
        if (!user.isPresent())
            throw new IllegalStateException("User does not exist.");

        //CHECK IF Income IS DEFINED
        if (income == null)
            throw new IllegalStateException("No purchase defined.");
        //CHECK IG DATE OF PURCHASE EXISTS
        if (income.getDoi() == null)
            income.setDoi(LocalDate.now());

        incomeRepository.save(income);
        userService.saveIncomeFromUser(income, userId);
    }

    public void deleteIncome(Long incomeId) {
        boolean exists = incomeRepository.existsById(incomeId);
        if (!exists) {
            throw new IllegalStateException("income with id " + incomeId + " does not exists");
        }
        incomeRepository.deleteById(incomeId);
    }

    @Transactional
    public void updateIncome(Long incomeId, String type, String subType, Long value, LocalDate doi) {
        Income income = incomeRepository.findById(incomeId).orElseThrow(() -> new IllegalStateException("purchase with id " + incomeId + " does not exists"));
        if (type != null && type.length() > 0) {
            income.setType(type);
        }

        if (subType != null && subType.length() > 0) {
            income.setSubType(subType);
        }

        if (value != null) {
            income.setValue(value);
        }

        if (doi != null) {
            income.setDoi(doi);
        }
    }
}
