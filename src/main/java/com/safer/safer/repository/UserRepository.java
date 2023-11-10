package com.safer.safer.repository;

import com.safer.safer.auth.ProviderType;
import com.safer.safer.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmailAndProviderType(String email, ProviderType providerType);
}
