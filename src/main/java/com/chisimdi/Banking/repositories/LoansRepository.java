package com.chisimdi.Banking.repositories;

import com.chisimdi.Banking.models.Loans;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoansRepository extends JpaRepository<Loans,Integer> {
    Loans findByIdAndAccountUserId(int loansId, int userId);
    List<Loans>findByAccountUserId(int userId);
    List<Loans>findByAutoPay(boolean autopay);
    List<Loans>findByStatus(String status);
    Page<Loans>findByAccountUserId(int userId, Pageable pageable);
}
