package com.apl.repository.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public class Tournament {

	private Long id;
	private String name;
	private String logoUrl;
	private String place;
	private String organizerName;
	private String season; // e.g., 2024/25
	private LocalDate startDate;
	private LocalDate endDate;
	private BigDecimal prizeMoney;
	private Long winnerTeamId;
	private Long runnerUpTeamId;
	private Instant createdAt;
	private Instant updatedAt;

	// ---------------- Getters & Setters ----------------

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getOrganizerName() {
		return organizerName;
	}

	public void setOrganizerName(String organizerName) {
		this.organizerName = organizerName;
	}

	public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public BigDecimal getPrizeMoney() {
		return prizeMoney;
	}

	public void setPrizeMoney(BigDecimal prizeMoney) {
		this.prizeMoney = prizeMoney;
	}

	public Long getWinnerTeamId() {
		return winnerTeamId;
	}

	public void setWinnerTeamId(Long winnerTeamId) {
		this.winnerTeamId = winnerTeamId;
	}

	public Long getRunnerUpTeamId() {
		return runnerUpTeamId;
	}

	public void setRunnerUpTeamId(Long runnerUpTeamId) {
		this.runnerUpTeamId = runnerUpTeamId;
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
