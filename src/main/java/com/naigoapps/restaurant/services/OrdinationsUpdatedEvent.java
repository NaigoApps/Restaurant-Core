package com.naigoapps.restaurant.services;

public class OrdinationsUpdatedEvent {
	private String diningTableUuid;

	public String getDiningTableUuid() {
		return diningTableUuid;
	}

	public void setDiningTableUuid(String diningTableUuid) {
		this.diningTableUuid = diningTableUuid;
	}
}
