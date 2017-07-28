package com.fabiorapanelo.donation;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.apache.commons.lang3.StringUtils;

public class EncryptPasswordListener {

	@PreUpdate
	@PrePersist
    public void doSomething(User user) throws NoSuchAlgorithmException, InvalidKeySpecException{
		
		if(StringUtils.isNotEmpty(user.getPassword())){
			
			String securePassword = EncryptionUtil.generateStrongPasswordHash(user.getPassword());
			user.setSecurePassword(securePassword);
			
			user.setPassword(StringUtils.EMPTY);
		}
		
	}
	
}
