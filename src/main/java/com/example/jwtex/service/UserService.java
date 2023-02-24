package com.example.jwtex.service;

import com.example.jwtex.domain.User;
import com.example.jwtex.domain.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public User create(final User user) {
        if (user == null || user.getUsername() == null) throw new RuntimeException("Invalid arguments");
        final String username = user.getUsername();
        if (repository.existsByUsername(username)){
            log.warn("Username already exists : " + username);
            throw new RuntimeException("Username already exists");
        }
        return repository.save(user);
    }

    public User getByCredentials(final String username, final String password) {
        return repository.findByUsernameAndPassword(username, password);
    }
}
