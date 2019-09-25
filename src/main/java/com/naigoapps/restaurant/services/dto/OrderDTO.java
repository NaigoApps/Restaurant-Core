/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.dto;

/**
 *
 * @author naigo
 */
public class OrderDTO extends DTO{

    private String ordinationId;
    
//    private DishDTO dish;
    
//    private List<AdditionDTO> additions;
    
    private float price;
    
    private String notes;
    
    private PhaseDTO phase;
    
    private String billId;

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

	public PhaseDTO getPhase() {
		return phase;
	}

	public void setPhase(PhaseDTO phase) {
		this.phase = phase;
	}

	public String getOrdinationId() {
		return ordinationId;
	}

	public void setOrdinationId(String ordinationId) {
		this.ordinationId = ordinationId;
	}

//	public List<AdditionDTO> getAdditions() {
//		return additions;
//	}
//
//	public void setAdditions(List<AdditionDTO> additions) {
//		this.additions = additions;
//	}
//
//	public DishDTO getDish() {
//		return dish;
//	}
//
//	public void setDish(DishDTO dish) {
//		this.dish = dish;
//	}

	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

    
}
