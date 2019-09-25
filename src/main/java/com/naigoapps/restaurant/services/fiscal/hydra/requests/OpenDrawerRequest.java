package com.naigoapps.restaurant.services.fiscal.hydra.requests;

import java.util.Arrays;
import java.util.List;

import com.naigoapps.restaurant.services.fiscal.hydra.fields.ByteField;
import com.naigoapps.restaurant.services.fiscal.hydra.fields.Field;
import com.naigoapps.restaurant.services.fiscal.hydra.fields.IntegerField;

public class OpenDrawerRequest implements Request {
	
	public static final byte CODE = 'q';
	
	@Override
	public List<Field> getContent() {
		return Arrays.asList(new ByteField(CODE), new IntegerField(3));
	}
	
}
