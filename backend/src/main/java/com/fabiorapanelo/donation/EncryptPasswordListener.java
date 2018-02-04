package com.fabiorapanelo.donation;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;

public class EncryptPasswordListener extends AbstractMongoEventListener<User> {

	@Override
	public void onBeforeConvert(BeforeConvertEvent<User> event) {

		User user = event.getSource();
		if (StringUtils.isNotEmpty(user.getPassword())) {

			String securePassword;
			try {
				securePassword = EncryptionUtil.generateStrongPasswordHash(user.getPassword());
				user.setSecurePassword(securePassword);
				user.setPassword(null);
			} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
				throw new RuntimeException(e);
			}

		}

		super.onBeforeConvert(event);
	}
}
