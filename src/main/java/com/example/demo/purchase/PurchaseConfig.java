package com.example.demo.purchase;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Configuration
public class PurchaseConfig {

    @Bean
    CommandLineRunner commandLineRunner(PurchaseRepository repository){
        return args -> {
            Purchase smartphone = new  Purchase(
                    490L,
                    "Luxury",
                    "Gadget",
                    LocalDate.of(2022, Month.MARCH,31)
            );
            Purchase charger = new  Purchase(
                    20L,
                    "Luxury",
                    "Gadget",
                    LocalDate.of(2022, Month.MARCH,31)
            );
            repository.saveAll(List.of(smartphone, charger));
        };
    }
}
