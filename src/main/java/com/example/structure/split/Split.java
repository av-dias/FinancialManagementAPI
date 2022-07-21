package com.example.structure.split;

import com.example.structure.userclient.UserClient;

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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private UserClient userClient;
    private int weight;

    public Split(int weight) {
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
