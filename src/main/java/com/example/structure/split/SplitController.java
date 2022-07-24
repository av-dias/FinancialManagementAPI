package com.example.structure.split;

import com.example.structure.purchase.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/split/")
public class SplitController {
    private final SplitService splitService;

    @Autowired
    public SplitController(SplitService splitService){
        this.splitService = splitService;
    }

    @GetMapping(path= "user/{userId}")
    public Set<Split> getSplit(@PathVariable("userId") Long userId){
        return splitService.getSplit(userId);
    }

}
