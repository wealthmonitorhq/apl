package com.apl.repository.model;

import java.util.List;
import java.util.Map;

public class AuctionSummary {

	private List<AuctionPlayer> allPlayers;
	private List<AuctionPlayer> soldPlayers;
	private List<AuctionTeamPurse> teamPurses;
	private Map<Long, AuctionBid> highestBids; // key: auctionPlayerId

	public AuctionSummary(List<AuctionPlayer> allPlayers, List<AuctionPlayer> soldPlayers,
			List<AuctionTeamPurse> teamPurses, Map<Long, AuctionBid> highestBids) {
		this.allPlayers = allPlayers;
		this.soldPlayers = soldPlayers;
		this.teamPurses = teamPurses;
		this.highestBids = highestBids;
	}

	// ---------------- Getters & Setters ----------------
	public List<AuctionPlayer> getAllPlayers() {
		return allPlayers;
	}

	public void setAllPlayers(List<AuctionPlayer> allPlayers) {
		this.allPlayers = allPlayers;
	}

	public List<AuctionPlayer> getSoldPlayers() {
		return soldPlayers;
	}

	public void setSoldPlayers(List<AuctionPlayer> soldPlayers) {
		this.soldPlayers = soldPlayers;
	}

	public List<AuctionTeamPurse> getTeamPurses() {
		return teamPurses;
	}

	public void setTeamPurses(List<AuctionTeamPurse> teamPurses) {
		this.teamPurses = teamPurses;
	}

	public Map<Long, AuctionBid> getHighestBids() {
		return highestBids;
	}

	public void setHighestBids(Map<Long, AuctionBid> highestBids) {
		this.highestBids = highestBids;
	}
}
