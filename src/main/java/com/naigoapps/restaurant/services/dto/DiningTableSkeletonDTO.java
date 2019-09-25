/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.dto;

import java.time.LocalDateTime;
import java.util.Comparator;

import com.naigoapps.restaurant.model.DiningTableStatus;

/**
 *
 * @author naigo
 */
public class DiningTableSkeletonDTO extends DTO {

	private String eveningId;

	private float coverCharge;

	private int coverCharges;

	private WaiterDTO waiter;
	
	private LocalDateTime openingTime;

	private RestaurantTableDTO table;

	private DiningTableStatus status;
	
	private double total;

	public String getEveningId() {
		return eveningId;
	}

	public void setEveningId(String eveningId) {
		this.eveningId = eveningId;
	}

	public int getCoverCharges() {
		return coverCharges;
	}

	public void setCoverCharges(int coverCharges) {
		this.coverCharges = coverCharges;
	}

	public WaiterDTO getWaiter() {
		return waiter;
	}

	public void setWaiter(WaiterDTO waiter) {
		this.waiter = waiter;
	}

	public LocalDateTime getOpeningTime() {
		return openingTime;
	}

	public void setOpeningTime(LocalDateTime openingTime) {
		this.openingTime = openingTime;
	}

	public RestaurantTableDTO getTable() {
		return table;
	}

	public void setTable(RestaurantTableDTO table) {
		this.table = table;
	}

	public DiningTableStatus getStatus() {
		return status;
	}

	public void setStatus(DiningTableStatus status) {
		this.status = status;
	}

	public static Comparator<DiningTableSkeletonDTO> comparator() {
		return (a, b) -> a.getOpeningTime().compareTo(b.getOpeningTime());
	}

	public void setCoverCharge(float coverCharge) {
		this.coverCharge = coverCharge;
	}

	public float getCoverCharge() {
		return coverCharge;
	}
	
	public void setTotal(double total) {
		this.total = total;
	}
	
	public double getTotal() {
		return total;
	}
}
