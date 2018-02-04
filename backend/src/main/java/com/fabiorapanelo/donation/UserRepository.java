package com.fabiorapanelo.donation;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface UserRepository extends MongoRepository<User, String> {
	
	public User findOneByUsernameIgnoreCase(@Param("name") String name);
	
}
