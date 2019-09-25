package com.naigoapps.restaurant.services.fiscal.hydra.requests;

import java.util.Arrays;
import java.util.List;

import com.naigoapps.restaurant.services.fiscal.hydra.fields.ByteField;
import com.naigoapps.restaurant.services.fiscal.hydra.fields.Field;
import com.naigoapps.restaurant.services.fiscal.hydra.fields.IntegerField;

public class FeedRequest implements Request {

	private static final byte CODE = 'w';

	private static final int RECEIPT_STATION = 1;
	private static final int LINES = 9;

	@Override
	public List<Field> getContent() {
		return Arrays.asList(new ByteField(CODE), new IntegerField(RECEIPT_STATION), new IntegerField(LINES));
	}
}
