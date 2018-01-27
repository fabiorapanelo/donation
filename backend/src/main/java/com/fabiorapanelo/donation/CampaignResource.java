package com.fabiorapanelo.donation;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.similarity.LevenshteinDistance;
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
	
	@GetMapping("/campaigns/search-by-similar")
	public final List<Campaign> searchBySimilarName(@RequestParam("name") String campaignName,
			@RequestParam("limit") long limit) {

		List<Campaign> campaigns = this.repository.findAll();

		LevenshteinDistance levenshteinDistance = LevenshteinDistance.getDefaultInstance();

		if (StringUtils.isEmpty(campaignName)) {
			return campaigns.parallelStream().limit(limit).collect(Collectors.toList());
		}

		return campaigns.parallelStream().sorted((c1, c2) -> {
			int distance1 = levenshteinDistance.apply(campaignName, c1.getName());
			int distance2 = levenshteinDistance.apply(campaignName, c2.getName());
			return Integer.compare(distance1, distance2);
		}).limit(limit).collect(Collectors.toList());
	}

}