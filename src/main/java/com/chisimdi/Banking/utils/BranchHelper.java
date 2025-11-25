package com.chisimdi.Banking.utils;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class BranchHelper {
    @Positive
            @NotNull
    int userId;
    @Positive
            @NotNull
    int branchId;

    public int getBranchId() {
        return branchId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

}
