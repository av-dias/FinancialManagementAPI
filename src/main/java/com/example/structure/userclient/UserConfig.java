package com.example.structure.userclient;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class UserConfig {

    /*@Bean
    CommandLineRunner commandLineRunnerUser(UserRepository repository){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        return args -> {
            UserClient userClient1 = new UserClient(
                    1L,
                    "Álison Dias",
                    "al.vrdias@gmail.com",
                    passwordEncoder.encode("123"),
                    LocalDateTime.parse("2022-04-05 08:30:00", formatter),
                    LocalDateTime.parse("2022-04-05 08:30:00", formatter)
            );
            UserClient userClient2 = new UserClient(
                    2L,
                    "Ana Catarina",
                    "anacatarinarebelo98@gmail.com",
                    passwordEncoder.encode("123"),
                    LocalDateTime.parse("2022-04-06 08:30:00", formatter),
                    LocalDateTime.parse("2022-04-06 08:30:00", formatter)
            );
            repository.save(userClient1);
            repository.save(userClient2);
        };
    }*/
}