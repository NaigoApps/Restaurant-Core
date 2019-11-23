/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author naigo
 */
public class OrdinationExportDTO extends DTO {

	private LocalDateTime creationTime;

	private List<OrderExportDTO> orders;

	public LocalDateTime getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(LocalDateTime creationTime) {
		this.creationTime = creationTime;
	}

	public List<OrderExportDTO> getOrders() {
		return orders;
	}

	public void setOrders(List<OrderExportDTO> orders) {
		this.orders = orders;
	}

}
