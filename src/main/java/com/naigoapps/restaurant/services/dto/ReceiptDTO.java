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
public class ReceiptDTO extends BillDTO{
    private int progressive;

    private LocalDateTime printTime;
    
    public ReceiptDTO() {
    }

    public ReceiptDTO(int progressive, LocalDateTime printTime, String uuid, String table, List<String> orders, int coverCharges, float total) {
        super(uuid, table, orders, coverCharges, total);
        this.progressive = progressive;
        this.printTime = printTime;
    }

    public int getProgressive() {
        return progressive;
    }

    public LocalDateTime getPrintTime() {
        return printTime;
    }
    
    
    
}
