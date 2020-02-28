package com.naigoapps.restaurant.services;

public class OrdinationsUpdatedEvent {
	private String diningTableUuid;
	
	public OrdinationsUpdatedEvent(String uuid) {
		diningTableUuid = uuid;
	}

	public String getDiningTableUuid() {
		return diningTableUuid;
	}
}
