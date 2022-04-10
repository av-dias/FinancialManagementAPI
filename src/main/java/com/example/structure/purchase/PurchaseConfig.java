package com.example.structure.purchase;

import org.springframework.context.annotation.Configuration;

@Configuration
public class PurchaseConfig {

    /*@Bean
    CommandLineRunner commandLineRunner(PurchaseRepository repository){
        return args -> {
            Purchase smartphone = new  Purchase(
                    1L,
                    490L,
                    "Luxury",
                    "Gadget",
                    LocalDate.of(2022, Month.MARCH,31)
            );
            Purchase charger = new  Purchase(
                    2L,
                    20L,
                    "Luxury",
                    "Gadget",
                    LocalDate.of(2022, Month.MARCH,31)
            );
            repository.saveAll(List.of(smartphone, charger));
        };
    }*/
}
