package com.fabiorapanelo.donation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserResource {

	@Autowired
	private MongoTemplate mongoTemplate;
	
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
}
