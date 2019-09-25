package com.naigoapps.restaurant.services.dto;

/**
 *
 * @author naigo
 */
public class LocationDTO extends DTO{
    private String name;
    
    private PrinterDTO printer;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

	public PrinterDTO getPrinter() {
		return printer;
	}

	public void setPrinter(PrinterDTO printer) {
		this.printer = printer;
	}
    
    
}
