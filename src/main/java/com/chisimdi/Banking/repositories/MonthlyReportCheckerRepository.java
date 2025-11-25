package com.chisimdi.Banking.repositories;

import com.chisimdi.Banking.models.MonthlyReportChecker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface MonthlyReportCheckerRepository extends JpaRepository<MonthlyReportChecker,Integer> {

    Boolean existsByLocalDateAndAccountId(LocalDate localDate,int accountId);
}
