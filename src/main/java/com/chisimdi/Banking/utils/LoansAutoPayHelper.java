package com.chisimdi.Banking.utils;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class LoansAutoPayHelper {
    @NotNull
            @Positive
    int userId;
    @NotNull
            @Positive
    int loanId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    public int getLoanId() {
        return loanId;
    }

}
