package com.naigoapps.restaurant.services.fiscal.hydra.requests;

import static org.junit.Assert.*;

import org.junit.Test;

import com.naigoapps.restaurant.services.fiscal.hydra.fields.IntegerField;

public class IntegerFieldTest {

	@Test
	public void testValue1() {
		IntegerField field = new IntegerField(5);
		byte[] result = field.value();
		assertEquals(1, result.length);
		assertEquals('5', result[0]);
	}

	@Test
	public void testValue2() {
		IntegerField field = new IntegerField(45, 3);
		byte[] result = field.value();
		assertEquals(3, result.length);
		assertEquals('0', result[0]);
		assertEquals('4', result[1]);
		assertEquals('5', result[2]);
	}

}
