package com.example.bankaccounts.dto.input;

public class InputTransferAccount extends InputAccount {
    private String amount;
    private String targetAccountId;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTargetAccountId() {
        return targetAccountId;
    }

    public void setTargetAccountId(String targetAccountId) {
        this.targetAccountId = targetAccountId;
    }
}
