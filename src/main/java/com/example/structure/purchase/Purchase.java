package com.example.structure.purchase;

import com.example.structure.documentation.Documentation;
import com.example.structure.split.Split;

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
    private String note;
    private LocalDate dop; // date of purchase

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "splitId", referencedColumnName = "id")
    private Split split;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "documentationId", referencedColumnName = "id")
    private Documentation documentation;

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

    public Long getId() {
        return id;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Split getSplit() {
        return split;
    }

    public void setSplit(Split split) {
        this.split = split;
    }

    public Documentation getDocumentation() {
        return documentation;
    }

    public void setDocumentation(Documentation documentation) {
        this.documentation = documentation;
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "id=" + id +
                ", value=" + value +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", note='" + note + '\'' +
                ", dop=" + dop +
                '}';
    }
}