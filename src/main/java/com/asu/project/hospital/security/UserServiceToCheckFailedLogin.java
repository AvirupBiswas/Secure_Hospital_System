package com.asu.project.hospital.security;

import java.util.Date;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asu.project.hospital.entity.User;
import com.asu.project.hospital.repository.UserRepository;

@Service
@Transactional
public class UserServiceToCheckFailedLogin {

	public static final int MAX_FAILED_ATTEMPTS = 3;

	private static final long LOCK_TIME_DURATION = 1 * 60 * 1000; // 1 minutes

	@Autowired
	private UserRepository userRepository;

	public void increaseFailedAttempts(User user) {
		int newFailAttempts = user.getFailedAttempt() + 1;
		userRepository.updateFailedAttempts(newFailAttempts, user.getEmail());
	}

	public void resetFailedAttempts(String email) {
		userRepository.updateFailedAttempts(0, email);
	}

	public void lock(User user) {
		user.setAccountLocked(true);
		user.setLockTime(new Date());

		userRepository.save(user);
	}

	public boolean unlockWhenTimeExpired(User user) {
		long lockTimeInMillis = user.getLockTime().getTime();
		long currentTimeInMillis = System.currentTimeMillis();

		if (lockTimeInMillis + LOCK_TIME_DURATION < currentTimeInMillis) {
			user.setAccountLocked(false);
			user.setLockTime(null);
			user.setFailedAttempt(0);

			userRepository.save(user);

			return true;
		}

		return false;
	}

	public Optional<User> getByEmail(String email) {
		// TODO Auto-generated method stub
		return userRepository.findOneByEmailIgnoreCase(email);
	}
}
