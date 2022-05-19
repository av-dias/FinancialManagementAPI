package com.example.structure.userclient;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserClient, Long> {

    @Query("SELECT u FROM UserClient u WHERE u.email = ?1")
    UserClient findUserClientByEmail(String email);

    @Query("SELECT u FROM UserClient u WHERE u.id = ?1")
    Optional<UserClient> findUserClientById(Long userId);
}