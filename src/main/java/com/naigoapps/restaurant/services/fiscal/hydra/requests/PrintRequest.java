package com.naigoapps.restaurant.services.fiscal.hydra.requests;

import java.util.Arrays;
import java.util.List;

import com.naigoapps.restaurant.services.fiscal.hydra.fields.ByteField;
import com.naigoapps.restaurant.services.fiscal.hydra.fields.Field;
import com.naigoapps.restaurant.services.fiscal.hydra.fields.IntegerField;
import com.naigoapps.restaurant.services.fiscal.hydra.fields.StringField;

public class PrintRequest implements Request {

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

	@Override
	public List<Field> getContent() {
		//@formatter:off
		return Arrays.asList(
				new ByteField(CODE),
				new IntegerField(PRINTER_DEVICE),
				new IntegerField(last ? 6 : 1, 2),
				new StringField(line));
		//@formatter:on
	}
}
