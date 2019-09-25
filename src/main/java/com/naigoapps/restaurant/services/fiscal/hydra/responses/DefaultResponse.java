package com.naigoapps.restaurant.services.fiscal.hydra.responses;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;

import com.naigoapps.restaurant.services.fiscal.hydra.fields.Field;

public class DefaultResponse extends Response{

	@Override
	protected List<Field> extractAdditionalFields(ByteBuffer buffer) {
		return Collections.emptyList();
	}
	
}
