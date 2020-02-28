package com.naigoapps.restaurant.services.fiscal.hydra.responses;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.naigoapps.restaurant.services.fiscal.hydra.Pair;
import com.naigoapps.restaurant.services.fiscal.hydra.commands.AckRequest;
import com.naigoapps.restaurant.services.fiscal.hydra.fields.Field;
import com.naigoapps.restaurant.services.fiscal.hydra.fields.IntegerField;

public abstract class Response {

	// @formatter:off
	private static final int CUTTER = 0b10000000;
	private static final int TIMEOUT = 0b01000000;
	private static final int FULL = 0b00100000;
	private static final int OFFLINE = 0b00010000;
	private static final int BATTERY = 0b00001000;
	private static final int PAPER = 0b00000100;
	private static final int FATAL = 0b00000010;
	private static final int BUSY = 0b00000001;

	private static final int REPORT = 0b10000000;
	private static final int CASH_OUT = 0b01000000;
	private static final int CASH_IN = 0b00100000;
	private static final int PAYING = 0b00010000;

	private static final int RECEIPT = 0b00000100;
	private static final int DAY = 0b00000010;
	private static final int OPEN = 0b00000001;
	// @formatter:on

	protected byte[] data;

	public void populate(byte[] data) {
		if(data != null){
			this.data = Arrays.copyOf(data, data.length);
		}
	}

	public boolean isAck() {
		return data.length == 1 && data[0] == AckRequest.CODE;
	}

	public boolean wasSuccessful() {
		return getReplyCodeField().getValue() == 0;
	}

	protected abstract List<Field> extractAdditionalFields(ByteBuffer buffer);

	public IntegerField getReplyCodeField() {
		if(data == null){
			return new IntegerField(-1);
		}
		ByteBuffer buffer = ByteBuffer.wrap(data);
		byte[] replyCode = new byte[2];
		buffer.position(1);
		buffer.get(replyCode);
		return IntegerField.fromBytes(replyCode, true);
	}

	public IntegerField getDeviceStatusField() {
		if(data == null){
			return new IntegerField(-1);
		}
		ByteBuffer buffer = ByteBuffer.wrap(data);
		byte[] deviceStatus = new byte[2];
		buffer.position(4);
		buffer.get(deviceStatus);
		return IntegerField.fromBytes(deviceStatus);
	}

	public IntegerField getFiscalStatusField() {
		if(data == null){
			return new IntegerField(-1);
		}
		ByteBuffer buffer = ByteBuffer.wrap(data);
		byte[] fiscalStatus = new byte[2];
		buffer.position(7);
		buffer.get(fiscalStatus);
		return IntegerField.fromBytes(fiscalStatus);
	}

	public List<Field> getFields() {
		List<Field> fields = new ArrayList<>();
		if(data == null){
			return fields;
		}
		ByteBuffer buffer = ByteBuffer.wrap(data);
		byte[] replyCode = new byte[2];
		byte[] deviceStatus = new byte[2];
		byte[] fiscalStatus = new byte[2];
		byte[] checksum = new byte[2];

		buffer.position(1);
		buffer.get(replyCode);
		buffer.get();
		fields.add(IntegerField.fromBytes(replyCode));

		buffer.get(deviceStatus);
		buffer.get();
		fields.add(IntegerField.fromBytes(deviceStatus));

		buffer.get(fiscalStatus);
		buffer.get();
		fields.add(IntegerField.fromBytes(fiscalStatus));

		fields.addAll(extractAdditionalFields(buffer));

		buffer.get(checksum);
		fields.add(IntegerField.fromBytes(checksum));
		return fields;
	}

	public List<Pair> getResult() {
		List<Pair> result = new ArrayList<>();

		int deviceStatus = getDeviceStatusField().getValue();
		int fiscalStatus = getFiscalStatusField().getValue();

		result.add(entry("Status", isSet(deviceStatus, OFFLINE) ? "Offline" : "Online"));
		result.add(entry("Busy", isSet(deviceStatus, BUSY) ? "Yes" : "No"));
		result.add(entry("Paper", isSet(deviceStatus, PAPER) ? "End" : "Ok"));
		result.add(entry("File", isSet(deviceStatus, FULL) ? "Full" : "Ok"));
		result.add(entry("Battery", isSet(deviceStatus, BATTERY) ? "Low" : "Ok"));
		result.add(entry("Cutter", isSet(deviceStatus, CUTTER) ? "Error" : "Ok"));
		result.add(entry("Timeout", isSet(deviceStatus, TIMEOUT) ? "Error" : "Ok"));
		result.add(entry("Error", isSet(deviceStatus, FATAL) ? "Yes" : "No"));

		result.add(entry("Drawer", isSet(fiscalStatus, OPEN) ? "Open" : "Closed"));
		result.add(entry("Payment", isSet(fiscalStatus, PAYING) ? "In progress" : "None"));
		result.add(entry("Receipt", isSet(fiscalStatus, RECEIPT) ? "In progress" : "None"));
		result.add(entry("Day", isSet(fiscalStatus, DAY) ? "Open" : "Closed"));
		result.add(entry("Report", isSet(fiscalStatus, REPORT) ? "In progress" : "None"));
		result.add(entry("Cash out receipt", isSet(fiscalStatus, CASH_OUT) ? "Open" : "None"));
		result.add(entry("Cash in receipt", isSet(fiscalStatus, CASH_IN) ? "Open" : "None"));

		return result;
	}

	private Pair entry(String a, String b) {
		return new Pair(a, b);
	}

	private boolean isSet(int b, int bit) {
		return (b & bit) > 0;
	}

}
