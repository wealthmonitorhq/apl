package com.apl.repository.model;

import java.time.Instant;
import java.time.LocalDate;

public class Auction {

	private Long id;
	private Long tournamentId;
	private String name;
	private String season;
	private LocalDate auctionDate;
	private String venue;
	private String status; // upcoming, live, completed
	private Instant createdAt;
	private Instant updatedAt;

	// getters & setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTournamentId() {
		return tournamentId;
	}

	public void setTournamentId(Long tournamentId) {
		this.tournamentId = tournamentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}

	public LocalDate getAuctionDate() {
		return auctionDate;
	}

	public void setAuctionDate(LocalDate auctionDate) {
		this.auctionDate = auctionDate;
	}

	public String getVenue() {
		return venue;
	}

	public void setVenue(String venue) {
		this.venue = venue;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
