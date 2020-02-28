package com.naigoapps.restaurant.services.fiscal.hydra.commands;

import com.naigoapps.restaurant.services.fiscal.hydra.fields.ByteField;
import com.naigoapps.restaurant.services.fiscal.hydra.fields.Field;
import com.naigoapps.restaurant.services.fiscal.hydra.fields.IntegerField;
import com.naigoapps.restaurant.services.fiscal.hydra.fields.StringField;
import java.util.Arrays;
import java.util.List;

public class PrintRequest extends AbstractRequest {

	public static final byte CODE = '7';
	public static final int PRINTER_DEVICE = 1;
	private String line;
	private boolean last;

	public void setLine(String line) {
		this.line = line;
	}
	
	public void setLast(boolean last) {
		this.last = last;
	}

	public List<Field> getContent() {
		//@formatter:off
		return Arrays.asList(
				new ByteField(CODE),
				new IntegerField(PRINTER_DEVICE),
				new IntegerField(last ? 6 : 1, 2),
				new StringField(line));
		//@formatter:on
	}

	@Override
	public byte[] getBytes() {
		return wrap(getContent());
	}
}
