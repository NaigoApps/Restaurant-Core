package com.naigoapps.restaurant.services.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author naigo
 */
public class BillDTO extends DTO {
	private Integer progressive;
	private CustomerDTO customer;
	private LocalDateTime printTime;
	private String tableUuid;
	private List<OrdersGroupDTO> orders;
	private int coverCharges;
	private float total;
	private float estimatedTotal;

	public Integer getProgressive() {
		return progressive;
	}

	public void setProgressive(Integer progressive) {
		this.progressive = progressive;
	}

	public CustomerDTO getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerDTO customer) {
		this.customer = customer;
	}

	public LocalDateTime getPrintTime() {
		return printTime;
	}

	public void setPrintTime(LocalDateTime printTime) {
		this.printTime = printTime;
	}

	public String getTableUuid() {
		return tableUuid;
	}

	public void setTableUuid(String uuid) {
		this.tableUuid = uuid;
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

	public List<OrdersGroupDTO> getOrders() {
		return orders;
	}

	public void setOrders(List<OrdersGroupDTO> orders) {
		this.orders = orders;
	}

	public float getEstimatedTotal() {
		return estimatedTotal;
	}

	public void setEstimatedTotal(float estimatedTotal) {
		this.estimatedTotal = estimatedTotal;
	}

}
