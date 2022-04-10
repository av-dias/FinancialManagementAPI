package com.example.demo.userclient;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserClient> getUserClients() { return userRepository.findAll();}

    public void addNewUser(UserClient userClient) {
        //CHECK IF USER ALREADY EXISTS
        Optional<UserClient> checkUser = userRepository.findUserClientByEmail(userClient.getEmail());
        if(checkUser.isPresent()){
            throw new IllegalStateException("Email Taken.");
        }
        //CHECK IF DATE OF CREATION EXISTS
        if(userClient.getDoc()==null) {
            userClient.setDoc(LocalDateTime.now());
            userClient.setDou(userClient.getDoc());
        }
        userRepository.save(userClient);
    }

    public void deleteUser(Long userId) {
        boolean exists = userRepository.existsById(userId);
        if(!exists){
            throw new IllegalStateException("user with id " + userId + " does not exists");
        }
        userRepository.deleteById(userId);
    }

    @Transactional
    public void updateUser(Long userId, String name, String email, String password, LocalDateTime dou){
        UserClient userClient = userRepository.findById(userId).orElseThrow(() -> new IllegalStateException("user with id " + userId + " does not exists"));
        if(name!=null && name.length()>0)
        {
            userClient.setName(name);
        }

        if(email!=null && email.length()>0){
            userClient.setEmail(email);
        }

        if(password!=null && password.length()>0){
            userClient.setPassword(password);
        }

        if(dou!=null){
            userClient.setDou(dou);
        }
    }
}