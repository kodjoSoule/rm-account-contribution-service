package com.rewardomain.accountcontributionservice.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountContributionResponse {

    @JsonProperty("status_code")
    private int code;
    private String message;

    public AccountContributionResponse() {
    }

    public AccountContributionResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override

    public String toString() {
        return "AccountContributionResponse{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }

}
