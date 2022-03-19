package com.asu.project.hospital.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.asu.project.hospital.entity.User;

public interface UserRepository extends JpaRepository<User, String> {
	Optional<User> findByEmail(String username);

	Optional<User> findOneByEmailIgnoreCase(String username);

	User findByUserId(String id);

	@Query("UPDATE User u SET u.failedAttempt = ?1 WHERE u.email = ?2")
	@Modifying
	public void updateFailedAttempts(int failAttempts, String email);
}
