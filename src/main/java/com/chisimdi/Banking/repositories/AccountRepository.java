package com.chisimdi.Banking.repositories;

import com.chisimdi.Banking.models.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account,Integer> {

   Boolean existsByAccountNumber(String accountNumber);
   Page<Account> findByUserId(int userId, Pageable pageable);
   Account findByIdAndUserId(int id, int userId);

}
