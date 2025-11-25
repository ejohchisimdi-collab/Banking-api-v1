package com.chisimdi.Banking.utils;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class TransferHelper {
    @Positive
            @NotNull
    int userId;
    @Positive
            @NotNull
    int sendersBankId;
    @Positive
            @NotNull
    int receiversBankId;
    @Positive
            @NotNull
    Double amount;

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setReceiversBankId(int receiversBankId) {
        this.receiversBankId = receiversBankId;
    }

    public int getReceiversBankId() {
        return receiversBankId;
    }

    public int getSendersBankId() {
        return sendersBankId;
    }

    public void setSendersBankId(int sendersBankId) {
        this.sendersBankId = sendersBankId;
    }

}
