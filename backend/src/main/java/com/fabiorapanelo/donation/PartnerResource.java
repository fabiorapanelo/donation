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
public class PartnerResource {

	@Autowired
	private PartnerRepository repository;

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@PostMapping("/partners")
	public final ResponseEntity<Void> createPartner(@RequestBody Partner partner) {
		this.repository.insert(partner);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/partners/search-by-user/{userId}")
	public final List<Partner> getLocations(@PathVariable("userId") String userId) {				
		return this.repository.findByUserId(userId);
	}
	
	@GetMapping("/partners/near-location")
	public final List<Partner> getLocations(@RequestParam("lat") double latitude,
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
				"}"), Partner.class);
		
	}
	
	@GetMapping("/partners/search-by-similar")
	public final List<Partner> searchBySimilarName(@RequestParam("name") String partnerName,
			@RequestParam("limit") long limit) {

		List<Partner> partners = this.repository.findAll();

		LevenshteinDistance levenshteinDistance = LevenshteinDistance.getDefaultInstance();

		if (StringUtils.isEmpty(partnerName)) {
			return partners.parallelStream().limit(limit).collect(Collectors.toList());
		}

		//TODO: Break the partner name into pieces and sort
		return partners.parallelStream().sorted((c1, c2) -> {
			int distance1 = levenshteinDistance.apply(partnerName, c1.getName());
			int distance2 = levenshteinDistance.apply(partnerName, c2.getName());
			return Integer.compare(distance1, distance2);
		}).limit(limit).collect(Collectors.toList());
	}

}