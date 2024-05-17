package com.neobis.cookscorner.repositories;

import com.neobis.cookscorner.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);
    List<User> findAllByNameStartsWithIgnoreCase(String name);
}
