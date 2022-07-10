package com.example.structure.income;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table
public class Income {
    @Id
    @SequenceGenerator(
            name = "income_sequence",
            sequenceName = "income_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "income_sequence"
    )

    private Long id;

    private Float value;

    private String type;

    private String subType;

    private LocalDate doi; // date of income

    public Income(){

    }

    public Income(Long id, Float value, String type, String subType, LocalDate doi) {
        this.id = id;
        this.value = value;
        this.type = type;
        this.subType = subType;
        this.doi = doi;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public void setDoi(LocalDate doi) {
        this.doi = doi;
    }

    public Long getId() {
        return id;
    }

    public Float getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    public String getSubType() {
        return subType;
    }

    public LocalDate getDoi() {
        return doi;
    }

    @Override
    public String toString() {
        return "Income{" +
                "id=" + id +
                ", value=" + value +
                ", type='" + type + '\'' +
                ", subType='" + subType + '\'' +
                ", doi=" + doi +
                '}';
    }
}
