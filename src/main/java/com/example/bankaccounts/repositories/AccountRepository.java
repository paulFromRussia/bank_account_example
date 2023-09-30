package com.example.bankaccounts.repositories;

import com.example.bankaccounts.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query("select a from Account a where a.name like :name")
    Optional<Account> findByName(@Param("name") String name);
}
