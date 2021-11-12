/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.fiscal.hydra;

import com.naigoapps.restaurant.model.Bill;
import com.naigoapps.restaurant.services.fiscal.hydra.fsms.ReceiptPrintFSM;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author naigo
 */
@Component
public class HydraNetworkPrintingService {

	@Autowired
	private HydraGateway gateway;

	public void print(Bill bill) throws IOException {
		new ReceiptPrintFSM(gateway, bill).start();
	}

}
