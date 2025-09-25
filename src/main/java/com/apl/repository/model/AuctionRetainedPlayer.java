package com.apl.repository.model;

import java.time.Instant;

public class AuctionRetainedPlayer {

	private Long id;
	private Long auctionId;
	private Long teamId;
	private Long playerId;
	private Double retentionPrice;
	private Boolean isForeign;
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

	public Long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(Long playerId) {
		this.playerId = playerId;
	}

	public Double getRetentionPrice() {
		return retentionPrice;
	}

	public void setRetentionPrice(Double retentionPrice) {
		this.retentionPrice = retentionPrice;
	}

	public Boolean getIsForeign() {
		return isForeign;
	}

	public void setIsForeign(Boolean isForeign) {
		this.isForeign = isForeign;
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
