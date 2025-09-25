package com.apl.repository.model;

import java.time.Instant;

public class AuctionRetentionRule {

	private Long id;
	private Long auctionId;
	private Integer maxRetained;
	private Integer maxRetainedForeign;
	private Double totalPurseDeduction;
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

	public Integer getMaxRetained() {
		return maxRetained;
	}

	public void setMaxRetained(Integer maxRetained) {
		this.maxRetained = maxRetained;
	}

	public Integer getMaxRetainedForeign() {
		return maxRetainedForeign;
	}

	public void setMaxRetainedForeign(Integer maxRetainedForeign) {
		this.maxRetainedForeign = maxRetainedForeign;
	}

	public Double getTotalPurseDeduction() {
		return totalPurseDeduction;
	}

	public void setTotalPurseDeduction(Double totalPurseDeduction) {
		this.totalPurseDeduction = totalPurseDeduction;
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
