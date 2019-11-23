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
public class PrinterDTO extends DTO {

	private String name;

	private int lineCharacters;

	private String address;

	private String port;

	public PrinterDTO() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLineCharacters(int lineCharacters) {
		this.lineCharacters = lineCharacters;
	}

	public int getLineCharacters() {
		return lineCharacters;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

}
