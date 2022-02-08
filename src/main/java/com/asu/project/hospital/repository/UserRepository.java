package com.asu.project.hospital.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asu.project.hospital.entity.User;

public interface UserRepository extends JpaRepository<User, String> {
	Optional<User> findByEmail(String username);

	Optional<User> findOneByEmailIgnoreCase(String username);
}
