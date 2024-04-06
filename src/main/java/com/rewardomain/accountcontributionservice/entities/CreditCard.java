package com.rewardomain.accountcontributionservice.entities;
import jakarta.persistence.*;

@Entity
public class CreditCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="credit_card_number")
    private String number;

    @OneToOne(mappedBy = "creditCard")
    private Account account;

    public CreditCard() {}

    public CreditCard(String ccnumber) {
        this.number = ccnumber;
    }


    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public CreditCard(Long id, String number, Account account) {
        super();
        this.id = id;
        this.number = number;
        this.account = account;
    }


    @Override
    public String toString() {
        return "CreditCard [id=" + id + ", number=" + number + ", account=" + account + "]";
    }

}
