/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.general.classes;

import ch.bfh.unicrypt.math.helper.Alphabet;
import java.math.BigInteger;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class FixedStringSet
	   extends FiniteStringSet {

	private FixedStringSet(Alphabet alphabet, int length) {
		super(alphabet, length, length);
	}

	public int getLength() {
		return this.getMinLength();
	}

	public static FixedStringSet getInstance(final Alphabet alphabet, final int length) {
		if (alphabet == null || length < 0) {
			throw new IllegalArgumentException();
		}
		return new FixedStringSet(alphabet, length);
	}

	public static FiniteStringSet getInstance(final Alphabet alphabet, final BigInteger minOrder) {
		if (alphabet == null || minOrder == null || minOrder.signum() < 0) {
			throw new IllegalArgumentException();
		}
		int length = 0;
		BigInteger size = BigInteger.valueOf(alphabet.getSize());
		BigInteger order = BigInteger.ONE;
		while (order.compareTo(minOrder) < 0) {
			order = order.multiply(size);
			length++;
		}
		return FixedStringSet.getInstance(alphabet, length);
	}

}
