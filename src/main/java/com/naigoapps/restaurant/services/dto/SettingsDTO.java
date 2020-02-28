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
public class SettingsDTO extends DTO {

	private float defaultCoverCharge;

	private String fiscalPrinterAddress;
	private int fiscalPrinterPort;

	private String fiscalPrinterPath;

	private String clientSettings;

	private Boolean shrinkOrdinations;

	private PrinterDTO mainPrinter;

	private String cashPassword;

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

	public void setMainPrinter(PrinterDTO mainPrinter) {
		this.mainPrinter = mainPrinter;
	}

	public PrinterDTO getMainPrinter() {
		return mainPrinter;
	}

	public String getCashPassword() {
		return cashPassword;
	}

	public void setCashPassword(String cashPassword) {
		this.cashPassword = cashPassword;
	}

	public String getFiscalPrinterPath() {
		return fiscalPrinterPath;
	}

	public void setFiscalPrinterPath(String fiscalPrinterPath) {
		this.fiscalPrinterPath = fiscalPrinterPath;
	}
}
