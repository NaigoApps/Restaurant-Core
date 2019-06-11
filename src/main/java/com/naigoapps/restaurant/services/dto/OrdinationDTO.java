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
public class OrdinationDTO extends DTO{
    
    private String table;
    
    private LocalDateTime creationTime;
    
    private int progressive;
    
    private List<OrderDTO> orders;
    
    private boolean dirty;

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public LocalDateTime getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(LocalDateTime creationTime) {
		this.creationTime = creationTime;
	}

	public int getProgressive() {
		return progressive;
	}

	public void setProgressive(int progressive) {
		this.progressive = progressive;
	}

	public List<OrderDTO> getOrders() {
		return orders;
	}

	public void setOrders(List<OrderDTO> orders) {
		this.orders = orders;
	}

	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

    

}
