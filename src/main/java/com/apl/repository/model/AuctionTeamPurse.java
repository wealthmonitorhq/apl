package com.apl.repository.model;

import java.time.Instant;

public class AuctionTeamPurse {

	private Long id;
	private Long auctionId;
	private Long teamId;
	private Double initialPurse;
	private Double remainingPurse;
	private Instant createdAt;
	private Instant updatedAt;

	// getters & setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAuctionId() {
		return auctionId;
	}

	public void setAuctionId(Long auctionId) {
		this.auctionId = auctionId;
	}

	public Long getTeamId() {
		return teamId;
	}

	public void setTeamId(Long teamId) {
		this.teamId = teamId;
	}

	public Double getInitialPurse() {
		return initialPurse;
	}

	public void setInitialPurse(Double initialPurse) {
		this.initialPurse = initialPurse;
	}

	public Double getRemainingPurse() {
		return remainingPurse;
	}

	public void setRemainingPurse(Double remainingPurse) {
		this.remainingPurse = remainingPurse;
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
