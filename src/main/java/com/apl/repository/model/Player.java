package com.apl.repository.model;

import java.time.Instant;
import java.time.LocalDate;

public class Player {

	private Long id;
	private String fullName;
	private String photoUrl;
	private String role; // batsman, bowler, allrounder, wicketkeeper, etc.
	private LocalDate dob;
	private String gender; // male/female/other/unknown
	private String battingHand; // right/left/unknown
	private String bowlingHand; // right/left/unknown
	private String notes;
	private Instant createdAt;
	private Instant updatedAt;

	// ---------------- Getters & Setters ----------------

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBattingHand() {
		return battingHand;
	}

	public void setBattingHand(String battingHand) {
		this.battingHand = battingHand;
	}

	public String getBowlingHand() {
		return bowlingHand;
	}

	public void setBowlingHand(String bowlingHand) {
		this.bowlingHand = bowlingHand;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}
}
