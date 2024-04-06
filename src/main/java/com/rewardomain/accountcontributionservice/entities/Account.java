package com.rewardomain.accountcontributionservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "owner")
    @JsonProperty("name")
    private String owner;

    @JsonProperty("account_number")
    @Column(name = "account_number")
    private String number;

    @Column(name = "total_benefits")
    private double benefits;

    @JsonIgnore
    @OneToMany(mappedBy = "account")
    private List<Beneficiary> beneficiaries = new ArrayList<>();

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_card_id")
    private CreditCard creditCard;

    public Account() {}

    public Account(String name, String anumber) {
        this.owner = name;
        this.number = anumber;
    }

    public Account(String name, String anumber, double benefits, List<Beneficiary> beneficiaries, CreditCard creditCard) {
        this.owner = name;
        this.number = anumber;
        this.benefits = benefits;
        this.beneficiaries = beneficiaries;
        this.creditCard = creditCard;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public double getBenefits() {
        return benefits;
    }

    public void setBenefits(double benefits) {
        this.benefits = benefits;
    }

    public List<Beneficiary> getBeneficiaries() {
        return beneficiaries;
    }

    public void setBeneficiaries(List<Beneficiary> beneficiaries) {
        this.beneficiaries = beneficiaries;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    // toString
    @Override
    public String toString() {
        return "Account [id=" + id + ", owner=" + owner + ", number=" + number + ", benefits=" + benefits + "]";
    }


    public boolean isValid() {
        double totalPercentage = 0.0;
        for (Beneficiary beneficiary : beneficiaries) {
            totalPercentage += beneficiary.getPercentage();
        }
        return totalPercentage == 100.0 ? true: false;
    }

    public double getReward() {
        return this.benefits;
    }
}
