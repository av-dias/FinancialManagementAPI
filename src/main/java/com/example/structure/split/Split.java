package com.example.structure.split;

import com.example.structure.userclient.UserClient;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
    private int id;
    private Long user_id;
    private int weight;

    public Split(int weight) {
        this.weight = weight;
    }

    public Split() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Long getUserClientId() {
        return user_id;
    }

    public void setUserClientId(Long userClientId) {
        this.user_id = userClientId;
    }
}
