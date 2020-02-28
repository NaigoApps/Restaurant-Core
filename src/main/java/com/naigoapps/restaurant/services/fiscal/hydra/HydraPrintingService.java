/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.fiscal.hydra;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.naigoapps.restaurant.services.fiscal.hydra.commands.AckRequest;
import com.naigoapps.restaurant.services.fiscal.hydra.commands.ResetRequest;
import com.naigoapps.restaurant.services.fiscal.hydra.commands.EnquireRequest;
import com.naigoapps.restaurant.services.fiscal.hydra.commands.Request;
import com.naigoapps.restaurant.services.fiscal.hydra.fields.Field;
import com.naigoapps.restaurant.services.fiscal.hydra.fields.IntegerField;
import com.naigoapps.restaurant.services.fiscal.hydra.responses.DefaultResponse;
import com.naigoapps.restaurant.services.fiscal.hydra.responses.Response;

/**
 *
 * @author naigo
 */
@Component
public class HydraPrintingService {

	private DatagramSocket printer;

	private byte[] response;
	private Semaphore sync;

	@Autowired
	private HydraGateway hydra;

	private InetAddress address;
	private int port;

	public void sendRequest(Request req, Response res) {
//		Response ackResponse = new DefaultResponse();
//		ResetRequest cancelRequest = new ResetRequest();
//		EnquireRequest startRequest = new EnquireRequest();
//		AckRequest ackRequest = new AckRequest();

//		hydra.send(wrap(cancelRequest.getContent()));

//		hydra.send(wrap(startRequest.getContent()));
//
//		hydra.send(wrap(req.getContent()));
//
//		hydra.send(wrap(ackRequest.getContent()));

	}

	private void sendMonoMessage(byte[] message) {
		DatagramPacket packet = new DatagramPacket(message, message.length, address, port);
		try {
			printer.send(packet);
		} catch (IOException ex) {
			throw new RuntimeException("Impossibile comunicare con la stampante");
		}
	}

	private void sendMessage(byte[] message, Response res) {
		DatagramPacket packet = new DatagramPacket(message, message.length, address, port);
		try {
			printer.send(packet);
			handleResponse(res);
		} catch (IOException ex) {
			throw new RuntimeException("Impossibile comunicare con la stampante");
		}
	}

	private void handleResponse(Response r) {
		receive();
		r.populate(response);
	}

	private void receive() {
		try {
			boolean result = sync.tryAcquire(5, TimeUnit.SECONDS);
			if (!result) {
				throw new RuntimeException("Timeout");
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException("Error", e);
		}
	}

	private String wrap(List<Field> fields) {
		int seps = fields.size();
		int size = fields.stream().mapToInt(Field::size).sum();
		ByteBuffer bb = ByteBuffer.allocate(seps + size + 4);
		int index = 1;
		bb.position(index);
		for (Field f : fields) {
			bb.put(f.value());
			index += f.size();
			bb.position(index);
			bb.put(Codes.SEP);
			index++;
			bb.position(index);
		}
		int checksum = 0;
		for (int i = 0; i < bb.array().length; i++) {
			checksum += ((int) bb.get(i)) & 0xFF;
			checksum = checksum & 0xFF;
		}
		checksum %= 100;
		IntegerField checksumField = new IntegerField(checksum, 2);

		bb.position(0);
		bb.put(Codes.PRE);
		bb.position(1 + seps + size);
		bb.put(checksumField.value());
		bb.put(Codes.POST);
		return new String(bb.array(), StandardCharsets.US_ASCII);
	}

}
