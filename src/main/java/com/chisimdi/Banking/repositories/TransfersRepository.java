package com.chisimdi.Banking.repositories;

import com.chisimdi.Banking.models.Transfers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransfersRepository extends JpaRepository<Transfers,Integer> {
    Transfers findByIdAndTransferringAccountUserId(int id, int userId);
    Page<Transfers>findByReversed(String reversed,Pageable pageable);
    List<Transfers>findByTransferringAccountUserId(int userId);
    Page<Transfers>findByTransferringAccountUserId(int userId, Pageable pageable);
}
