package com.apl.repository.model;

import java.time.Instant;

public class AuctionBid {

	private Long id;
	private Long auctionPlayerId;
	private Long teamId;
	private Double bidAmount;
	private Instant bidTime;
	private Instant createdAt;

	// getters & setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAuctionPlayerId() {
		return auctionPlayerId;
	}

	public void setAuctionPlayerId(Long auctionPlayerId) {
		this.auctionPlayerId = auctionPlayerId;
	}

	public Long getTeamId() {
		return teamId;
	}

	public void setTeamId(Long teamId) {
		this.teamId = teamId;
	}

	public Double getBidAmount() {
		return bidAmount;
	}

	public void setBidAmount(Double bidAmount) {
		this.bidAmount = bidAmount;
	}

	public Instant getBidTime() {
		return bidTime;
	}

	public void setBidTime(Instant bidTime) {
		this.bidTime = bidTime;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}
}
