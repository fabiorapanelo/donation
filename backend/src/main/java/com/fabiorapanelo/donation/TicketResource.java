package com.fabiorapanelo.donation;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TicketResource {
	
	@Autowired
	private TicketRepository ticketRepository;

	@Autowired
	private LogRepository logRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@PostMapping("/ticket")
	public final Ticket create(@RequestBody Ticket ticket) {
		return this.ticketRepository.insert(ticket);
	}
	
	@GetMapping("/ticket/{ticketId}")
	public final Ticket find(@PathVariable("ticketId") String ticketId) {
		return this.ticketRepository.findOne(ticketId);
	}
	
	@PostMapping("/ticket/{ticketId}/user/{userId}/consume")
	public final ResponseEntity<Void> consume(@PathVariable("ticketId") String ticketId, @PathVariable("userId") String userId) {
		
		Ticket ticket = this.ticketRepository.findOne(ticketId);
		User user = this.userRepository.findOne(userId);
		
		if(ticket != null && user != null && "NEW".equals(ticket.getStatus())){
			
			Log log = new Log();
			log.setDate(new Date());
			log.setQuantity(ticket.getQuantity());
			log.setType("TICKET");
			log.setUserFrom("SYSTEM");
			log.setUserTo(userId);
			
			ticket.setStatus("CLOSED");
			this.ticketRepository.save(ticket);
			
			this.logRepository.insert(log);
			
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.badRequest().build();
		}
		
	}
	

}
