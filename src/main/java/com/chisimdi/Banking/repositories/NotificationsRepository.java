package com.chisimdi.Banking.repositories;

import com.chisimdi.Banking.models.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationsRepository extends JpaRepository<Notifications,Integer> {
}
