package com.chisimdi.Banking.repositories;

import com.chisimdi.Banking.models.Transactions;
import com.chisimdi.Banking.models.Transfers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transactions,Integer> {

    List<Transactions>findByAccountUserId(int userId);
    Transactions findByIdAndAccountUserId(int id,int userId);
    Page<Transactions>findByAccountUserId(int userId, Pageable pageable);

}
