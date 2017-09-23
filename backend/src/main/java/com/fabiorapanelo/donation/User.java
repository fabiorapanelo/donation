package com.fabiorapanelo.donation;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@EntityListeners(EncryptPasswordListener.class)
public class User {

	private Long id;
	private String name;
	private String username;
	private boolean receiveDonations;
    private boolean verified;
	private String password;
	private String securePassword;
	private List<Donation> myDonations;
	private List<Donation> donationsForMe;

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
	@Column(unique=true)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isReceiveDonations() {
		return receiveDonations;
	}

	public void setReceiveDonations(boolean receiveDonations) {
		this.receiveDonations = receiveDonations;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	@JsonProperty(access = Access.WRITE_ONLY)
	@Transient
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@JsonProperty(access = Access.WRITE_ONLY)
	@NotNull
	public String getSecurePassword() {
		return securePassword;
	}

	public void setSecurePassword(String securePassword) {
		this.securePassword = securePassword;
	}

	@JsonIgnore
	@OneToMany(mappedBy = "origin")
	public List<Donation> getMyDonations() {
		return myDonations;
	}

	public void setMyDonations(List<Donation> myDonations) {
		this.myDonations = myDonations;
	}

	@JsonIgnore
	@OneToMany(mappedBy = "destination")
	public List<Donation> getDonationsForMe() {
		return donationsForMe;
	}

	public void setDonationsForMe(List<Donation> donationsForMe) {
		this.donationsForMe = donationsForMe;
	}
}
