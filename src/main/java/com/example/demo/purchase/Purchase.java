package com.example.demo.purchase;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table
public class Purchase {
    @Id
    @SequenceGenerator(
            name = "purchase_sequence",
            sequenceName = "purchase_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "purchase_sequence"
    )
    private Long id;
    private Long value;
    private String type;
    private String subType;
    private LocalDate dop;

    public Purchase() {
    }

    public Purchase(Long value, String type, String subType, LocalDate dop) {
        this.value = value;
        this.type = type;
        this.subType = subType;
        this.dop = dop;
    }

    public Long getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    public String getSubType() {
        return subType;
    }

    public LocalDate getDop() {
        return dop;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public void setValue(Long value) {
        this.value = value;
    }
}