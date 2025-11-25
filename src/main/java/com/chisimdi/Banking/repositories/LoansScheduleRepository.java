package com.chisimdi.Banking.repositories;

import com.chisimdi.Banking.models.LoanSchedule;
import com.chisimdi.Banking.models.Loans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoansScheduleRepository extends JpaRepository<LoanSchedule,Integer> {

    List<LoanSchedule>findByLoansIdAndLoansAccountUserId(int loansId,int userId);
    LoanSchedule findByIdAndLoansAccountUserId(int id, int userId);
}
