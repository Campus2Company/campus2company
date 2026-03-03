package com.campus2company.auth.config;

import com.campus2company.auth.model.AccountStatus;
import com.campus2company.auth.model.Role;
import com.campus2company.auth.model.UserAccount;
import com.campus2company.auth.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Seeds an admin account in development environment.
 *
 * Design Decision: Admin accounts are created via this one-time dev seed rather than
 * allowing registration through the API. This approach:
 * 1. Prevents unauthorized admin account creation
 * 2. Keeps the registration endpoint simple and secure
 * 3. Allows controlled admin provisioning in production via database scripts or secure tools
 *
 * In production, admins should be created via:
 * - Direct database insertion with proper password hashing
 * - Secure admin provisioning scripts
 * - Infrastructure-as-code with secrets management
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class AdminSeedConfig {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    @Profile("dev")
    public CommandLineRunner seedAdminUser() {
        return args -> {
            String adminEmail = "admin@campus2company.com";

            if (userAccountRepository.existsByEmail(adminEmail)) {
                log.info("Admin user already exists, skipping seed");
                return;
            }

            UserAccount admin = UserAccount.builder()
                    .email(adminEmail)
                    .passwordHash(passwordEncoder.encode("admin123")) // Change in production!
                    .role(Role.PLATFORM_ADMIN)
                    .status(AccountStatus.ACTIVE)
                    .build();

            userAccountRepository.save(admin);
            log.info("Seeded admin user: {}", adminEmail);
        };
    }
}
