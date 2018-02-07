package com.fabiorapanelo.donation;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends MongoRepository<User, String> {
	
	public User findOneByUsernameIgnoreCase(@Param("name") String name);
	
}
