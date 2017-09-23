package com.fabiorapanelo.donation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CampaignResource {

	@Autowired
	private CampaignRepository repository;

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@PostMapping("/campaigns")
	public final ResponseEntity<Void> createCampaign(@RequestBody Campaign campaignMongo) {
		this.repository.insert(campaignMongo);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/campaigns/search-by-user/{userId}")
	public final List<Campaign> getLocations(@PathVariable("userId") String userId) {				
		return this.repository.findByUserId(userId);
	}
	
	@GetMapping("/campaigns/near-location")
	public final List<Campaign> getLocations(@RequestParam("lat") double latitude,
			@RequestParam("long") double longitude, @RequestParam("distance") double distance) {				
		
		return mongoTemplate.find(new BasicQuery(
				"{ location: { " + 
					"$near: { " + 
						"$geometry: { " +
							"type: \"Point\", " + 
							"coordinates: [  " + longitude + ", " + latitude + "  ] }, " +
							"$maxDistance: " + distance + 
						"} " +
					"}" + 
				"}"), Campaign.class);
		
	}

}