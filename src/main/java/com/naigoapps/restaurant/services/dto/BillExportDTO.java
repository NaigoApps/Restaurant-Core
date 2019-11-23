package com.naigoapps.restaurant.services.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author naigo
 */
public class BillExportDTO extends DTO {

	private Integer progressive;
	private LocalDateTime printTime;
	private List<String> orders;
	private int coverCharges;
	private float total;

	public Integer getProgressive() {
		return progressive;
	}

	public void setProgressive(Integer progressive) {
		this.progressive = progressive;
	}

	public LocalDateTime getPrintTime() {
		return printTime;
	}

	public void setPrintTime(LocalDateTime printTime) {
		this.printTime = printTime;
	}

	public List<String> getOrders() {
		return orders;
	}

	public void setOrders(List<String> orders) {
		this.orders = orders;
	}

	public int getCoverCharges() {
		return coverCharges;
	}

	public void setCoverCharges(int coverCharges) {
		this.coverCharges = coverCharges;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

}
