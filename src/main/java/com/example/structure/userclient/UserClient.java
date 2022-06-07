package com.example.structure.userclient;

import com.example.structure.purchase.Purchase;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

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

    @OneToMany
    @JoinColumn(name = "clientId", referencedColumnName = "id")
    private Set<Purchase> purchases;

    private String name;
    private String email;
    private String password;
    private String role;
    private LocalDateTime doc; //date of creation
    private LocalDateTime dou; //date of update

    public UserClient() {
    }

    public UserClient(Long id, String name, String email, String password, LocalDateTime doc, LocalDateTime doa) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = "user";
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

    public void addPurchase(Purchase purchase) {
        this.purchases.add(purchase);
    }

    public Set<Purchase> getPurchases() {
        return purchases;
    }

    public int getTotalPurchases() {
        Iterator iter = this.purchases.iterator();
        int total = 0;
        while (iter.hasNext()) {
            Purchase element = (Purchase) iter.next();
            total += element.getValue();
        }
        return total;
    }

    public int getTotalMonthPurchases() {
        Iterator iter = this.purchases.iterator();
        int total = 0;
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH)+1;

        while (iter.hasNext()) {
            Purchase element = (Purchase) iter.next();
            int elemMonth = element.getDop().getMonthValue();
            if (elemMonth == month)
                total += element.getValue();
        }
        return total;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
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
                ", role='" + role + '\'' +
                ", doc=" + doc +
                ", doa=" + dou +
                '}';
    }
}