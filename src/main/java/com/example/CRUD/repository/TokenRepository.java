package com.example.CRUD.repository;

import com.example.CRUD.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {


        Token findByToken(String token);
}
