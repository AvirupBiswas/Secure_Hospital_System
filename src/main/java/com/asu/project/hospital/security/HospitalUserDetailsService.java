package com.asu.project.hospital.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.asu.project.hospital.entity.User;
import com.asu.project.hospital.model.HospitalUserDetails;
import com.asu.project.hospital.repository.UserRepository;

@Service
public class HospitalUserDetailsService implements UserDetailsService{
	
	@Autowired
    UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		  Optional<User> user = userRepository.findByEmail(userName);

	        user.orElseThrow(() -> new UsernameNotFoundException("Username not found " + userName));

	        if (user != null) {
				/*
				 * sessionLog.setUserId(user.get().getUserId()); sessionLog.setTimestamp(new
				 * Date()); user.get().setSessionLog(Arrays.asList(sessionLog));
				 */
	            userRepository.save(user.get());
				/*
				 * SystemLog systemLog = new SystemLog(); systemLog.setMessage(userName +
				 * " logged in"); systemLog.setTimestamp(new Date());
				 * systemLogRepository.save(systemLog);
				 */
	        }

	        return user.map(HospitalUserDetails::new).get();
	}

}
