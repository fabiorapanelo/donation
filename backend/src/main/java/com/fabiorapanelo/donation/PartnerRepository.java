package com.fabiorapanelo.donation;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface PartnerRepository extends MongoRepository<Partner, String> {

	List<Partner> findByUserId(String userId);

}