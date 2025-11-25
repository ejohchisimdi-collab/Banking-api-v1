package com.chisimdi.Banking.repositories;

import com.chisimdi.Banking.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    boolean existsByUserName(String userName);
    User findByUserName(String userName);
    Page<User> findByRoles(String role, Pageable pageable);
    User findByRolesAndId(String role,int id);
}
