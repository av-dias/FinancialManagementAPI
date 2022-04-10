package com.example.demo.userclient;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table
public class UserClient {
    @Id
    @SequenceGenerator(
            name = "userclient_sequence",
            sequenceName = "userclient_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "userclient_sequence"
    )

    private Long id;
    private String name;
    private String email;
    private String password;
    private LocalDateTime doc; //date of creation
    private LocalDateTime dou; //date of update

    public UserClient() {
    }

    public UserClient(Long id, String name, String email, String password, LocalDateTime doc, LocalDateTime doa) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.doc = doc;
        this.dou = doa;
    }

    public UserClient(String name, String email, String password, LocalDateTime doc, LocalDateTime doa) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.doc = doc;
        this.dou = doa;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public LocalDateTime getDoc() {
        return doc;
    }

    public LocalDateTime getDou() {
        return dou;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDoc(LocalDateTime doc) {
        this.doc = doc;
    }

    public void setDou(LocalDateTime dou) {
        this.dou = dou;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", doc=" + doc +
                ", doa=" + dou +
                '}';
    }
}