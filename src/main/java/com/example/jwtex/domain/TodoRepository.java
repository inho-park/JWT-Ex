package com.example.jwtex.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, String> {
    @Query("select t from Todo t where t.userId = ?1")
    List<Todo> findByUserId(String userId);
}
