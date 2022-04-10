package com.example.structure.userclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path= "api/v1/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    public List<UserClient> getUserClients() { return userService.getUserClients();}

    @PostMapping
    public void registerNewUserClient(@RequestBody UserClient userClient){
        userService.addNewUser(userClient);
    }

    @DeleteMapping(path= "{userId}")
    public void deleteUserClient(@PathVariable("userId") Long userId){
        userService.deleteUser(userId);
    }

    @PutMapping(path= "{userId}")
    public void updateUser(
            @PathVariable("userId") Long userId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String password){
        userService.updateUser(userId, name, email, password, LocalDateTime.now());
    }
}
