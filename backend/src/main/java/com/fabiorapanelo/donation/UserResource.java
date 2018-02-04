package com.fabiorapanelo.donation;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserResource {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private UserRepository repository;
	
	@GetMapping("/users/{userId}/balance")
	public UserInfo getBalance(@PathVariable("userId") String userId) {				
		
		UserInfo userInfo = new UserInfo();
		
		List<Log> logs = this.getUserLogs(userId);
		
		int balance = 0;
		int numberDonations = 0;
		
		for(Log log: logs){
			if(log.getUserFrom().equals(log.getUserTo())){
				//User from and to is the same - scenario shouldn't happen
			} else if(userId.equals(log.getUserFrom())){
				balance = balance - log.getQuantity();
				numberDonations++;
			} else {
				balance = balance + log.getQuantity();
			}
		}
		userInfo.setBalance(balance);
		userInfo.setNumberDonations(numberDonations);
		
		return userInfo;
	}
	
	@GetMapping("/users/{userId}/logs")
	public List<Log> getUserLogs(@PathVariable("userId") String userId) {				
		
		return mongoTemplate.find(new BasicQuery(
					"{ $or: [ " + 
						"{ userFrom: \"" + userId + "\" }," + 
						"{ userTo: \"" + userId + "\" } " + 
					"] } "), Log.class);
	}
	
	@GetMapping("/users/search-by-similar")
	public final List<User> searchBySimilarName(@RequestParam("name") String name,
			@RequestParam("limit") long limit) {

		List<User> users = this.repository.findAll();

		LevenshteinDistance levenshteinDistance = LevenshteinDistance.getDefaultInstance();

		if (StringUtils.isEmpty(name)) {
			return users.parallelStream().limit(limit).collect(Collectors.toList());
		}

		//TODO: Break the campaign name into pieces and sort
		return users.parallelStream().sorted((c1, c2) -> {
			int distance1 = levenshteinDistance.apply(name, c1.getName());
			int distance2 = levenshteinDistance.apply(name, c2.getName());
			return Integer.compare(distance1, distance2);
		}).limit(limit).collect(Collectors.toList());
	}
}
