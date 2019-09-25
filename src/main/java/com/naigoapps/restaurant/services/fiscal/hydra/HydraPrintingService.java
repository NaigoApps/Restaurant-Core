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
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;

import com.naigoapps.restaurant.model.Settings;
import com.naigoapps.restaurant.model.dao.SettingsDao;
import com.naigoapps.restaurant.services.fiscal.hydra.fields.Field;
import com.naigoapps.restaurant.services.fiscal.hydra.fields.IntegerField;
import com.naigoapps.restaurant.services.fiscal.hydra.requests.AckRequest;
import com.naigoapps.restaurant.services.fiscal.hydra.requests.CancelRequest;
import com.naigoapps.restaurant.services.fiscal.hydra.requests.EnquireRequest;
import com.naigoapps.restaurant.services.fiscal.hydra.requests.Request;
import com.naigoapps.restaurant.services.fiscal.hydra.responses.DefaultResponse;
import com.naigoapps.restaurant.services.fiscal.hydra.responses.Response;

/**
 *
 * @author naigo
 */
@Startup
@Singleton
public class HydraPrintingService {
	
	private static final int DEFAULT_PRINTER_PORT = 9101;
	private static final String DEFAULT_PRINTER_ADDRESS = "192.168.1.24";

	private DatagramSocket printer;
	private boolean stopped;

	private byte[] response;
	private Semaphore sync;

	@Inject
	private SettingsDao settingsDao;
	
	private InetAddress address;
	private int port;
	
	@PostConstruct
	public void init() {
		try {
			Settings settings = settingsDao.find();
			
			port = settings.getFiscalPrinterPort();
			if(port < 1024) {
				port = DEFAULT_PRINTER_PORT;
			}
			
			String addr = settings.getFiscalPrinterAddress();
			if(addr == null) {
				addr = DEFAULT_PRINTER_ADDRESS;
			}
			this.address = InetAddress.getByName(addr);
			
			sync = new Semaphore(0);
			printer = new DatagramSocket(port);
			stopped = false;

			new Thread(() -> {
				byte[] buffer = new byte[256];
				while (!stopped) {
					DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
					try {
						printer.receive(packet);
						ByteBuffer bytes = ByteBuffer.allocate(packet.getLength());
						bytes.put(packet.getData(), 0, packet.getLength());
						response = bytes.array();
						sync.release();
					} catch (IOException e) {
						throw new InternalServerErrorException("Impossibile comunicare con la stampante fiscale");
					}
				}
			}).start();
		} catch (IOException ex) {
			throw new InternalServerErrorException("Impossibile comunicare con la stampante fiscale", ex);
		}
	}

	public void sendRequest(Request req, Response res) {
		Response ackResponse = new DefaultResponse();
		CancelRequest cancelRequest = new CancelRequest();
		EnquireRequest startRequest = new EnquireRequest();
		AckRequest ackRequest = new AckRequest();
		sendMonoMessage(cancelRequest.getBytes());
		sendMessage(startRequest.getBytes(), ackResponse);
		if (ackResponse.isAck()) {
			sendMessage(wrap(req.getContent()), ackResponse);
			if (ackResponse.isAck()) {
				sendMonoMessage(ackRequest.getBytes());
				handleResponse(res);
			} else {
				throw new InternalServerErrorException("Richiesta non eseguita");
			}
		} else {
			throw new InternalServerErrorException("Richiesta non accettata");
		}
	}

	private void sendMonoMessage(byte[] message) {
		DatagramPacket packet = new DatagramPacket(message, message.length, address, port);
		try {
			printer.send(packet);
		} catch (IOException ex) {
			throw new InternalServerErrorException("Impossibile comunicare con la stampante");
		}
	}

	private void sendMessage(byte[] message, Response res) {
		DatagramPacket packet = new DatagramPacket(message, message.length, address, port);
		try {
			printer.send(packet);
			handleResponse(res);
		} catch (IOException ex) {
			throw new InternalServerErrorException("Impossibile comunicare con la stampante");
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
				throw new InternalServerErrorException("Timeout");
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new InternalServerErrorException("Error", e);
		}
	}

	private byte[] wrap(List<Field> fields) {
		int seps = fields.size();
		int size = fields.stream().mapToInt(Field::size).sum();
		ByteBuffer bb = ByteBuffer.allocate(seps + size + 4);
		int index = 1;
		bb.position(index);
		for(Field f : fields) {
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
		return bb.array();
	}

	@PreDestroy
	public void destroy() {
		stopped = true;
	}
}
