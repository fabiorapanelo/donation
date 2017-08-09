package com.fabiorapanelo.donation;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

	private UserRepository userRepository;

	public AuthenticationController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@PostMapping("/authentication")
	public ResponseEntity<User> login(@RequestBody Credentials credentials) {

		User user = userRepository.findOneByUsernameIgnoreCase(credentials.getUsername());

		try {

			if (user != null && StringUtils.isNotEmpty(credentials.getPassword())
					&& StringUtils.isNotEmpty(user.getSecurePassword())
					&& StringUtils.isNotEmpty(credentials.getPassword())
					&& EncryptionUtil.validatePassword(credentials.getPassword(), user.getSecurePassword())) {
				return new ResponseEntity<User>(user, HttpStatus.OK);
			}
			
		} catch (Exception e) {
			return new ResponseEntity<User>(HttpStatus.FORBIDDEN);
		}

		return new ResponseEntity<User>(HttpStatus.FORBIDDEN);

	}
}
