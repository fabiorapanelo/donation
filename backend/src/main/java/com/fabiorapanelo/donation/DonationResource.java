package com.fabiorapanelo.donation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DonationResource {
	
	@Autowired
	private CampaignRepository campaignRepository;
	
	@Autowired
	private LogRepository logRepository;
	
	@PostMapping("/donate")
	public final ResponseEntity<Void> donate(@RequestBody Log log) {
		
		String campaignId = log.getCampaign();
		
		Campaign campaign = this.campaignRepository.findOne(campaignId);
		List<Log> donations = campaign.getDonations();
		if(donations == null){
			donations = new ArrayList<>();
		}
		donations.add(log);
		campaign.setDonations(donations);
		
		this.logRepository.insert(log);
		this.campaignRepository.save(campaign);
		
		return ResponseEntity.ok().build();
	}

}
