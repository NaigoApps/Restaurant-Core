/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.naigoapps.restaurant.model.DiningTableStatus;

/**
 *
 * @author naigo
 */
public class DiningTableDTO extends DTO {

	private String eveningId;

	private float coverCharge;

	private int coverCharges;

	private WaiterDTO waiter;
	
	private List<OrdersGroupDTO> orders;

	private List<OrdinationDTO> ordinations;

	private List<BillDTO> bills;

	private LocalDateTime openingTime;

	private RestaurantTableDTO table;

	private DiningTableStatus status;
	
	private double total;

	public DiningTableDTO() {
		ordinations = new ArrayList<>();
		bills = new ArrayList<>();
	}

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

	public List<OrdinationDTO> getOrdinations() {
		return ordinations;
	}

	public void setOrdinations(List<OrdinationDTO> ordinations) {
		this.ordinations = ordinations;
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

	public List<BillDTO> getBills() {
		return bills;
	}

	public void setBills(List<BillDTO> bills) {
		this.bills = bills;
	}

	public static Comparator<DiningTableDTO> comparator() {
		return (a, b) -> a.getOpeningTime().compareTo(b.getOpeningTime());
	}

	public void setCoverCharge(float coverCharge) {
		this.coverCharge = coverCharge;
	}

	public float getCoverCharge() {
		return coverCharge;
	}
	
	public void setOrders(List<OrdersGroupDTO> orders) {
		this.orders = orders;
	}
	
	public List<OrdersGroupDTO> getOrders() {
		return orders;
	}
	
	public void setTotal(double total) {
		this.total = total;
	}
	
	public double getTotal() {
		return total;
	}
}
