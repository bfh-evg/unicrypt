/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.general.classes;

import ch.bfh.unicrypt.math.algebra.general.abstracts.AbstractSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.utility.ArrayUtil;
import ch.bfh.unicrypt.math.utility.RandomUtil;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.Random;

/**
 *
 * @author rolfhaenni
 */
public class FiniteByteArraySet
			 extends AbstractSet<FiniteByteArrayElement> {

	private final int length;
	private final boolean equalLength;

	private FiniteByteArraySet(int length, boolean equalLength) {
		this.length = length;
		this.equalLength = equalLength;
	}

	public int getLength() {
		return this.length;
	}

	public boolean equalLength() {
		return this.equalLength;
	}

	public final FiniteByteArrayElement getElement(final byte[] bytes) {
		if (bytes == null || bytes.length > this.getLength() || (this.equalLength() && bytes.length < this.getLength())) {
			throw new IllegalArgumentException();
		}
		return this.standardGetElement(bytes);
	}

	protected FiniteByteArrayElement standardGetElement(byte[] bytes) {
		return new FiniteByteArrayElement(this, bytes);
	}

	@Override
	protected FiniteByteArrayElement abstractGetElement(BigInteger value) {
		LinkedList<Byte> byteList = new LinkedList<Byte>();
		BigInteger size = BigInteger.valueOf(1 << Byte.SIZE);
		while (!value.equals(BigInteger.ZERO)) {
			if (!this.equalLength()) {
				value = value.subtract(BigInteger.ONE);
			}
			byteList.addFirst(value.mod(size).byteValue());
			value = value.divide(size);
		}
		if (this.equalLength()) {
			while (byteList.size() < this.getLength()) {
				byteList.addFirst((byte) 0);
			}
		}
		return this.standardGetElement(ArrayUtil.byteListToByteArray(byteList));
	}

	@Override
	protected BigInteger abstractGetOrder() {
		BigInteger size = BigInteger.valueOf(1 << Byte.SIZE);
		if (this.equalLength) {
			return size.pow(this.getLength());
		}
		BigInteger order = BigInteger.ONE;
		for (int i = 0; i < this.getLength(); i++) {
			order = order.multiply(size).add(BigInteger.ONE);
		}
		return order;
	}

	@Override
	protected FiniteByteArrayElement abstractGetRandomElement(Random random
	) {
		return this.abstractGetElement(RandomUtil.getRandomBigInteger(this.getOrder().subtract(BigInteger.ONE), random));
	}

	@Override
	protected boolean abstractContains(BigInteger value
	) {
		return (value.signum() >= 0) && (value.compareTo(this.getOrder()) < 0);
	}

	@Override
	public boolean standardIsEqual(final Set set
	) {
		final FiniteByteArraySet other = (FiniteByteArraySet) set;
		return this.getLength() == other.getLength() && this.equalLength() == other.equalLength();
	}

	//
	// STATIC FACTORY METHODS
	//
	public static FiniteByteArraySet getInstance(final int length) {
		return FiniteByteArraySet.getInstance(length, false);
	}

	public static FiniteByteArraySet getInstance(final int length, final boolean equalLength) {
		if (length < 0) {
			throw new IllegalArgumentException();
		}
		return new FiniteByteArraySet(length, equalLength);
	}

	public static FiniteByteArraySet getInstance(final BigInteger minOrder) {
		return FiniteByteArraySet.getInstance(minOrder, false);
	}

	public static FiniteByteArraySet getInstance(final BigInteger minOrder, boolean equalLength) {
		if (minOrder == null || minOrder.signum() < 0) {
			throw new IllegalArgumentException();
		}
		int length = 0;
		BigInteger size = BigInteger.valueOf(1 << Byte.SIZE);
		BigInteger order = BigInteger.ONE;
		while (order.compareTo(minOrder) < 0) {
			order = order.multiply(size);
			if (equalLength) {
				order = order.add(BigInteger.ONE);
			}
			length++;
		}
		return new FiniteByteArraySet(length, equalLength);
	}

}
