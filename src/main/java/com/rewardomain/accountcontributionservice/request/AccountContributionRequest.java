package com.rewardomain.accountcontributionservice.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountContributionRequest {
    private String name;
    @JsonProperty("credit_card_number") private String ccnumber;
    @JsonProperty("account_number") private String anumber;
    @JsonProperty("allocation_percentage") private double percentage;

    public AccountContributionRequest() {
    }

    public AccountContributionRequest(String name, String ccnumber, String anumber, double percentage) {
        this.name = name;
        this.ccnumber = ccnumber;
        this.anumber = anumber;
        this.percentage = percentage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCcnumber() {
        return ccnumber;
    }

    public void setCcnumber(String ccnumber) {
        this.ccnumber = ccnumber;
    }

    public String getAnumber() {
        return anumber;
    }

    public void setAnumber(String anumber) {
        this.anumber = anumber;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    @Override

    public String toString() {
        return "AccountContributionRequest{" +
                "name='" + name + '\'' +
                ", ccnumber='" + ccnumber + '\'' +
                ", anumber='" + anumber + '\'' +
                ", percentage=" + percentage +
                '}';
    }

}