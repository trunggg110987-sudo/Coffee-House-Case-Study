package com.coffeeshopmanagement.repository;

import com.coffeeshopmanagement.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,Long> {
}
