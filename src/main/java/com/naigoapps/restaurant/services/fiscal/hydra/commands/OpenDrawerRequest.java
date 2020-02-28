package com.naigoapps.restaurant.services.fiscal.hydra.commands;

import com.naigoapps.restaurant.services.fiscal.hydra.fields.ByteField;
import com.naigoapps.restaurant.services.fiscal.hydra.fields.Field;
import com.naigoapps.restaurant.services.fiscal.hydra.fields.IntegerField;
import java.util.Arrays;
import java.util.List;

public class OpenDrawerRequest extends AbstractRequest {
	
	public static final byte CODE = 'q';

	public List<Field> getContent() {
		return Arrays.asList(new ByteField(CODE), new IntegerField(3));
	}

	@Override
	public byte[] getBytes() {
		return wrap(getContent());
	}
}
