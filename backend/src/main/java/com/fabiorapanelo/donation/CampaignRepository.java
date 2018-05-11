package com.fabiorapanelo.donation;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CampaignRepository extends MongoRepository<Campaign, String> {

	List<Campaign> findByUserId(String userId);

	
	
}

