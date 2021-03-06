/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.dto;

import java.util.List;

/**
 *
 * @author naigo
 */
public class OrderExportDTO extends DTO {

	private String dish;

	private List<String> additions;

	private float price;

	private String notes;

	private String phase;

	public String getDish() {
		return dish;
	}

	public void setDish(String dish) {
		this.dish = dish;
	}

	public List<String> getAdditions() {
		return additions;
	}

	public void setAdditions(List<String> additions) {
		this.additions = additions;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}



}
