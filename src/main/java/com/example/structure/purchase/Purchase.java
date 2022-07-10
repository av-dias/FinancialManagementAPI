package com.example.structure.purchase;

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

    private Float value;
    private String name;
    private String type;
    private String subType;
    private LocalDate dop; // date of purchase

    public Purchase() {
    }

    public Purchase(Float value, String name, String type, LocalDate dop) {
        this.value = value;
        this.name = name;
        this.type = type;
        this.dop = dop;
    }

    public Purchase(Long id, Float value, String name, String type, LocalDate dop) {
        this.id = id;
        this.value = value;
        this.name = name;
        this.type = type;
        this.dop = dop;
    }

    public Float getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public LocalDate getDop() {
        return dop;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public void setDop(LocalDate dop) {
        this.dop = dop;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "id=" + id +
                ", value=" + value +
                ", type='" + name + '\'' +
                ", subType='" + type + '\'' +
                ", dop=" + dop +
                '}';
    }
}