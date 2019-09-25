package com.naigoapps.restaurant.services.fiscal.hydra.requests;

import java.util.Arrays;
import java.util.List;

import com.naigoapps.restaurant.services.fiscal.hydra.fields.ByteField;
import com.naigoapps.restaurant.services.fiscal.hydra.fields.Field;

public class CutRequest implements Request {
	
	public static final byte CODE = 'm';
	
	@Override
	public List<Field> getContent() {
		return Arrays.asList(new ByteField(CODE));
	}
	
}
