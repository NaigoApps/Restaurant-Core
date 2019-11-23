/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author naigo
 */
public class DiningTableExportDTO extends DTO {

	private String evening;

	private int coverCharges;
	private String waiter;
	private List<OrdinationExportDTO> ordinations;
	private List<BillExportDTO> bills;

	private LocalDateTime openingTime;

	private String table;

	private double total;

	public DiningTableExportDTO() {
		ordinations = new ArrayList<>();
		bills = new ArrayList<>();
	}

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

	public List<OrdinationExportDTO> getOrdinations() {
		return ordinations;
	}

	public void setOrdinations(List<OrdinationExportDTO> ordinations) {
		this.ordinations = ordinations;
	}

	public List<BillExportDTO> getBills() {
		return bills;
	}

	public void setBills(List<BillExportDTO> bills) {
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

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

}
