package com.example.structure.transactions;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table
public class Transactions {
    @Id
    @SequenceGenerator(
            name = "transactions_sequence",
            sequenceName = "transactions_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "transactions_sequence"
    )
    private Long id;

    private Long user_destination_id;

    private Long user_origin_id;

    private Float amount;

    private LocalDate dot;

    public Transactions(){
    }

    public Transactions(Long id, Long user_destination_id, Long user_origin_id, Float amount, LocalDate dot, String description) {
        this.id = id;
        this.user_destination_id = user_destination_id;
        this.user_origin_id = user_origin_id;
        this.amount = amount;
        this.dot = dot;
        this.description = description;
    }

    public Transactions(Long user_destination_id, Long user_origin_id, Float amount, LocalDate dot, String description) {
        this.user_destination_id = user_destination_id;
        this.user_origin_id = user_origin_id;
        this.amount = amount;
        this.dot = dot;
        this.description = description;
    }

    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser_destination_id() {
        return user_destination_id;
    }

    public void setUser_destination_id(Long user_destination_id) {
        this.user_destination_id = user_destination_id;
    }

    public Long getUser_origin_id() {
        return user_origin_id;
    }

    public void setUser_origin_id(Long user_origin_id) {
        this.user_origin_id = user_origin_id;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public LocalDate getDot() {
        return dot;
    }

    public void setDot(LocalDate dot) {
        this.dot = dot;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Transactions{" +
                "id=" + id +
                ", user_destination_id=" + user_destination_id +
                ", user_origin_id=" + user_origin_id +
                ", amount=" + amount +
                ", dot=" + dot +
                ", description='" + description + '\'' +
                '}';
    }
}
