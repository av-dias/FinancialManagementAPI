package com.example.structure.documentation;

import javax.persistence.*;

@Entity
@Table
public class Documentation {
    @Id
    @SequenceGenerator(
            name = "documentation_sequence",
            sequenceName = "documentation_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "documentation_sequence"
    )
    private int id;

    private int type;

    private String path;

    public Documentation(int id) {
        this.id = id;
    }

    public Documentation(int type, String path) {
        this.type = type;
        this.path = path;
    }

    public Documentation(int id, int type, String path) {
        this.id = id;
        this.type = type;
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
