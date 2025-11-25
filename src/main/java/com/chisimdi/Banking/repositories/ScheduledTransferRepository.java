package com.chisimdi.Banking.repositories;

import com.chisimdi.Banking.models.ScheduledTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduledTransferRepository extends JpaRepository<ScheduledTransfer,Integer> {
   List<ScheduledTransfer> findByCompleted(String completed);
}
