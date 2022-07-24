package com.example.structure.split;

import com.example.structure.purchase.Purchase;
import com.example.structure.userclient.UserClient;
import com.example.structure.userclient.UserService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utility.protection.UserProtection;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class SplitService {

    private final SplitRepository splitRepository;
    private final UserProtection userProtection;

    private final UserService userService;

    @Autowired
    public SplitService(SplitRepository splitRepository, UserService userService) {
        this.splitRepository = splitRepository;
        this.userService = userService;
        this.userProtection = new UserProtection(userService);
    }

    public Set<Split> getSplit(Long userId) {
        Optional<UserClient> _userClient = userProtection.hasUser(userId);
        if (_userClient.isPresent()) {
            Set<Purchase> purchases = _userClient.get().getPurchases();
            Set<Split> split = new HashSet<Split>();
            purchases.forEach(purchase -> {
                if(purchase.getSplit() != null){
                    split.add(purchase.getSplit());
                    System.out.println(purchase.getSplit());
                }
            });
            return split;
        }
        return null;
    }
}
