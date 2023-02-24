package com.example.jwtex.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    User findByUsername(String username);
    Boolean existsByUsername(String username);
    User findByUsernameAndPassword(String username, String password);
}
