/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author naigo
 */
@Entity
@Table(name = "settings")
public class Settings extends BaseEntity {

	private float defaultCoverCharge;

	private String fiscalPrinterAddress;
	private String fiscalPrinterPath;
	private int fiscalPrinterPort;

	@Lob
	private String clientSettings;

	private String cashPassword;

	private Boolean shrinkOrdinations;

	@ManyToOne
	private Printer mainPrinter;

	public String getClientSettings() {
		return clientSettings;
	}

	public void setClientSettings(String clientSettings) {
		this.clientSettings = clientSettings;
	}

	public float getDefaultCoverCharge() {
		return defaultCoverCharge;
	}

	public void setDefaultCoverCharge(float defaultCoverCharge) {
		this.defaultCoverCharge = defaultCoverCharge;
	}

	public Boolean getShrinkOrdinations() {
		return shrinkOrdinations;
	}

	public void setShrinkOrdinations(Boolean shrinkOrdinations) {
		this.shrinkOrdinations = shrinkOrdinations;
	}

	public String getFiscalPrinterAddress() {
		return fiscalPrinterAddress;
	}

	public void setFiscalPrinterAddress(String fiscalPrinterAddress) {
		this.fiscalPrinterAddress = fiscalPrinterAddress;
	}

	public int getFiscalPrinterPort() {
		return fiscalPrinterPort;
	}

	public void setFiscalPrinterPort(int fiscalPrinterPort) {
		this.fiscalPrinterPort = fiscalPrinterPort;
	}
	
	public String getFiscalPrinterPath() {
		return fiscalPrinterPath;
	}
	
	public void setFiscalPrinterPath(String fiscalPrinterPath) {
		this.fiscalPrinterPath = fiscalPrinterPath;
	}

	public Printer getMainPrinter() {
		return mainPrinter;
	}

	public void setMainPrinter(Printer mainPrinter) {
		this.mainPrinter = mainPrinter;
	}

	public String getCashPassword() {
		return cashPassword;
	}

	public void setCashPassword(String cashPassword) {
		this.cashPassword = cashPassword;
	}
}
