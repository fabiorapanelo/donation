package com.fabiorapanelo.donation;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface UserRepository extends CrudRepository<User, Long> {
	
	public User findOneByUsernameIgnoreCase(@Param("name") String name);
	
}
