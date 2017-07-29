package com.fabiorapanelo.donation;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface CampaignRepository extends CrudRepository<Campaign, Long> {

	public Iterable<Donation> findByNameContainingIgnoreCase(String name);

}