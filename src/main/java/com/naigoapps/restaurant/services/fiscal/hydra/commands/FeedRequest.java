package com.naigoapps.restaurant.services.fiscal.hydra.commands;

import com.naigoapps.restaurant.services.fiscal.hydra.fields.ByteField;
import com.naigoapps.restaurant.services.fiscal.hydra.fields.Field;
import com.naigoapps.restaurant.services.fiscal.hydra.fields.IntegerField;
import java.util.Arrays;
import java.util.List;

public class FeedRequest extends AbstractRequest {

	private static final byte CODE = 'w';

	private static final int RECEIPT_STATION = 1;
	private static final int LINES = 9;

	public List<Field> getContent() {
		return Arrays.asList(new ByteField(CODE), new IntegerField(RECEIPT_STATION), new IntegerField(LINES));
	}

	@Override
	public byte[] getBytes() {
		return wrap(getContent());
	}

}
