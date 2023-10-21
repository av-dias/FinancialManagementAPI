package com.example.structure.split;

import javax.persistence.*;

@Entity
@Table
public class Split {
    @Id
    @SequenceGenerator(
            name = "split_sequence",
            sequenceName = "split_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "split_sequence"
    )
    private Long id;
    private Long userId;
    private int weight;

    public Split(int weight) {
        this.weight = weight;
    }

    public Split(int weight, Long userId) {
        this.weight = weight;
        this.userId = userId;
    }

    public Split() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long user_id) {
        this.userId = user_id;
    }

    @Override
    public String toString() {
        return "Split{" +
                "id=" + id +
                ", weight=" + weight +
                ", userId='" + userId +
                '}';
    }
}
