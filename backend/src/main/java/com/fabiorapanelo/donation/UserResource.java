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
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserResource {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private LogRepository logRepository;
	
	@PostMapping("/users/{userId}/add-ticket")
	public final ResponseEntity<Void> addTicket(@RequestBody Log log) {
		
		this.logRepository.insert(log);
		
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/users/{userId}/balance")
	public Integer getBalance(@PathVariable("userId") String userId) {				
		
		List<Log> logs = this.getUserLogs(userId);
		
		int total = 0;
		
		for(Log log: logs){
			if(log.getUserFrom().equals(log.getUserTo())){
				//User from and to is the same - scenario shouldn't happen
			} else if(userId.equals(log.getUserFrom())){
				total = total - log.getQuantity();
			} else {
				total = total + log.getQuantity();
			}
		}
		
		return total;
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
