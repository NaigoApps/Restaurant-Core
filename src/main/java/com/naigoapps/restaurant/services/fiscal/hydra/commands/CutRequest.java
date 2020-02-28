package com.naigoapps.restaurant.services.fiscal.hydra.commands;

import com.naigoapps.restaurant.services.fiscal.hydra.fields.ByteField;
import com.naigoapps.restaurant.services.fiscal.hydra.fields.Field;
import java.util.Arrays;
import java.util.List;

public class CutRequest extends AbstractRequest {
	
	public static final byte CODE = 'm';
	
	public List<Field> getContent() {
		return Arrays.asList(new ByteField(CODE));
	}

	@Override
	public byte[] getBytes() {
		return wrap(getContent());
	}
}