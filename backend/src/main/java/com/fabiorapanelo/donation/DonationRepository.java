package com.fabiorapanelo.donation;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface DonationRepository extends CrudRepository<Donation, Long> {
	
}
