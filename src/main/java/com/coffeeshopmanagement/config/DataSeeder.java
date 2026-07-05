package com.coffeeshopmanagement.config;

import com.coffeeshopmanagement.entity.Account;
import com.coffeeshopmanagement.entity.Role;
import com.coffeeshopmanagement.repository.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (accountRepository.findByUsername("admin").isEmpty()) {
            Account admin = new Account();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("123456"));
            admin.setFullName("Quan ly");
            admin.setRole(Role.ADMIN);
            accountRepository.save(admin);
        }

        if (accountRepository.findByUsername("staff").isEmpty()) {
            Account staff = new Account();
            staff.setUsername("staff");
            staff.setPassword(passwordEncoder.encode("123456"));
            staff.setFullName("Nhan vien");
            staff.setRole(Role.STAFF);
            accountRepository.save(staff);
        }
    }
}
