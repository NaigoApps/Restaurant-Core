/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.naigoapps.restaurant.model.DiningTableStatus;

/**
 *
 * @author naigo
 */
public class DiningTableDTO extends DTO {

	private String evening;

	private int coverCharges;

	private String waiter;

	private List<String> ordinations;

	private List<String> bills;

	private LocalDateTime openingTime;

	private String table;

	private DiningTableStatus status;

	public String getEvening() {
		return evening;
	}

	public void setEvening(String evening) {
		this.evening = evening;
	}

	public int getCoverCharges() {
		return coverCharges;
	}

	public void setCoverCharges(int coverCharges) {
		this.coverCharges = coverCharges;
	}

	public String getWaiter() {
		return waiter;
	}

	public void setWaiter(String waiter) {
		this.waiter = waiter;
	}

	public List<String> getOrdinations() {
		return ordinations;
	}

	public void setOrdinations(List<String> ordinations) {
		this.ordinations = ordinations;
	}

	public List<String> getBills() {
		return bills;
	}

	public void setBills(List<String> bills) {
		this.bills = bills;
	}

	public LocalDateTime getOpeningTime() {
		return openingTime;
	}

	public void setOpeningTime(LocalDateTime openingTime) {
		this.openingTime = openingTime;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public DiningTableStatus getStatus() {
		return status;
	}

	public void setStatus(DiningTableStatus status) {
		this.status = status;
	}

}
