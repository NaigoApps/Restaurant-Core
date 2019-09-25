package com.naigoapps.restaurant.services.dto;

public class MessageRequestDTO {
	private String[] locationUuids;
	private String tableUuid;
	private String message;

	public String[] getLocationUuids() {
		return locationUuids;
	}

	public void setLocationUuids(String[] locationUuids) {
		this.locationUuids = locationUuids;
	}

	public String getTableUuid() {
		return tableUuid;
	}

	public void setTableUuid(String tableUuid) {
		this.tableUuid = tableUuid;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
