package com.example.structure.userclient;

import com.example.structure.income.Income;
import com.example.structure.purchase.Purchase;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.json.JSONObject;

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
    //spring.jpa.hibernate.ddl-auto=create
    @OneToMany
    @JsonBackReference
    @JoinColumn(name = "clientId", referencedColumnName = "id")
    private Set<Purchase> purchases;

    @OneToMany
    @JoinColumn(name = "clientId", referencedColumnName = "id")
    private Set<Income> income;

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

    public void addIncome(Income income) {
        this.income.add(income);
    }

    public Set<Purchase> getPurchases() {
        return purchases;
    }

    public Set<Income> getIncome() {
        return income;
    }

    public boolean hasPurchase(Purchase purchase){
        return this.getPurchases().contains(purchase);
    }

    public float getTotalPurchases() {
        Iterator iter = this.purchases.iterator();
        float total = 0;
        while (iter.hasNext()) {
            Purchase element = (Purchase) iter.next();
            total += element.getValue();
        }
        return total;
    }

    public float getTotalSavings() {
        Iterator iterIncome = this.income.iterator();

        float totalPurchases = this.getTotalPurchases();
        float total = 0;
        int totalIncome = 0;

        while (iterIncome.hasNext()) {
            Income element = (Income) iterIncome.next();
            totalIncome += element.getValue();
        }

        return totalIncome - totalPurchases;
    }

    public float getMonthSavings() {
        Iterator iterIncome = this.income.iterator();

        float monthPurchases = this.getTotalMonthPurchases(0);
        float monthIncome = 0;

        // The income of Type==Salary from January will be used on February
        // The paycheck comes on the end of the month
        Calendar calendar = Calendar.getInstance();
        int month;

        while (iterIncome.hasNext()) {
            Income element = (Income) iterIncome.next();
            int elemMonth = element.getDoi().getMonthValue();

            if (element.getType().toLowerCase().equals("salary"))
                month = calendar.get(Calendar.MONTH);
            else
                month = calendar.get(Calendar.MONTH) + 1;

            if (elemMonth == month)
                monthIncome += element.getValue();
        }
        return monthIncome - monthPurchases;
    }

    public float getTotalMonthPurchases(int month_backtrack) {
        Iterator iter = this.purchases.iterator();
        float total = 0;
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1 - month_backtrack;

        while (iter.hasNext()) {
            Purchase element = (Purchase) iter.next();
            int elemMonth = element.getDop().getMonthValue();
            if (elemMonth == month)
                total += element.getValue();
        }
        return total;
    }

    public JSONObject getMonthsPurchases(){
        JSONObject purchaseByMonth = new JSONObject();
        int total_backtrack=11; // goes from 11 to 0 with are 12 months iterations
        while(total_backtrack>=0){
            float purchase = getTotalMonthPurchases(total_backtrack);
            purchaseByMonth.accumulate(Integer.toString(total_backtrack), purchase);
        }
        System.out.println(purchaseByMonth);
        return purchaseByMonth;
    }

    public JSONObject getMonthPurchasesbyType() {
        Iterator iter = this.purchases.iterator();
        JSONObject purchaseByType = new JSONObject();
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;

        while (iter.hasNext()) {
            Purchase element = (Purchase) iter.next();
            int elemMonth = element.getDop().getMonthValue();
            String type = element.getType();
            Float value = element.getValue();
            if (elemMonth == month) {
                if(purchaseByType.has(type))
                    purchaseByType.put(type, (float)purchaseByType.get(type)+value);
                else
                    purchaseByType.put(type, value);
            }
        }
        return purchaseByType;
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