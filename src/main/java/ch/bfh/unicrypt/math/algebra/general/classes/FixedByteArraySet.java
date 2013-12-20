/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.general.classes;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class FixedByteArraySet
	   extends FiniteByteArraySet {

	private FixedByteArraySet(int length) {
		super(length, length);
	}

	public int getLength() {
		return this.getMinLength();
	}

	private static final Map<Integer, FixedByteArraySet> instances = new HashMap<Integer, FixedByteArraySet>();

	public static FixedByteArraySet getInstance(final int length) {
		if (length < 0) {
			throw new IllegalArgumentException();
		}
		FixedByteArraySet instance = FixedByteArraySet.instances.get(Integer.valueOf(length));
		if (instance == null) {
			instance = new FixedByteArraySet(length);
			FixedByteArraySet.instances.put(Integer.valueOf(length), instance);
		}
		return instance;
	}

	public static FixedByteArraySet getInstance(final BigInteger minOrder) {
		if (minOrder == null || minOrder.signum() < 0) {
			throw new IllegalArgumentException();
		}
		int length = 0;
		BigInteger size = BigInteger.valueOf(1 << Byte.SIZE);
		BigInteger order = BigInteger.ONE;
		while (order.compareTo(minOrder) < 0) {
			order = order.multiply(size);
			length++;
		}
		return FixedByteArraySet.getInstance(length);
	}

}
