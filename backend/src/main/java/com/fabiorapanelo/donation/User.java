package com.fabiorapanelo.donation;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@EntityListeners(EncryptPasswordListener.class)
public class User {

	private Long id;
	private String name;
	private String username;
	private String type;
	private String password;
	private String securePassword;
	private List<Donation> myDonations;
	private List<Donation> donationsForMe;
	private List<Campaign> myCampaings;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotNull
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@NotNull
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Transient
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@NotNull
	public String getSecurePassword() {
		return securePassword;
	}

	public void setSecurePassword(String securePassword) {
		this.securePassword = securePassword;
	}

	@OneToMany(mappedBy = "origin")
	public List<Donation> getMyDonations() {
		return myDonations;
	}

	public void setMyDonations(List<Donation> myDonations) {
		this.myDonations = myDonations;
	}

	@OneToMany(mappedBy = "destination")
	public List<Donation> getDonationsForMe() {
		return donationsForMe;
	}

	public void setDonationsForMe(List<Donation> donationsForMe) {
		this.donationsForMe = donationsForMe;
	}

	@OneToMany(mappedBy = "createdBy")
	public List<Campaign> getMyCampaings() {
		return myCampaings;
	}

	public void setMyCampaings(List<Campaign> myCampaings) {
		this.myCampaings = myCampaings;
	}
}
