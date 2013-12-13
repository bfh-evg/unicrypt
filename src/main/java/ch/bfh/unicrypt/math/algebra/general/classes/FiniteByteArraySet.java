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

	private final int minLength;
	private final int maxLength;

	protected FiniteByteArraySet(int minLength, int maxLength) {
		this.minLength = minLength;
		this.maxLength = maxLength;
	}

	public int getMinLength() {
		return this.minLength;
	}

	public int getMaxLength() {
		return this.maxLength;
	}

	public boolean fixedLength() {
		return this.getMinLength() == this.getMaxLength();
	}

	public final FiniteByteArrayElement getElement(final byte[] bytes) {
		if (bytes == null || bytes.length < this.getMinLength() || bytes.length > this.getMaxLength()) {
			throw new IllegalArgumentException();
		}
		return this.standardGetElement(bytes);
	}

	protected FiniteByteArrayElement standardGetElement(byte[] bytes) {
		return new FiniteByteArrayElement(this, bytes);
	}

	@Override
	protected FiniteByteArrayElement abstractGetElement(BigInteger value) {
		int minLength = this.getMinLength();
		BigInteger size = BigInteger.valueOf(1 << Byte.SIZE);
		LinkedList<Byte> byteList = new LinkedList<Byte>();
		while (!value.equals(BigInteger.ZERO) || byteList.size() < minLength) {
			if (byteList.size() >= minLength) {
				value = value.subtract(BigInteger.ONE);
			}
			byteList.addFirst(value.mod(size).byteValue());
			value = value.divide(size);
		}
		return this.standardGetElement(ArrayUtil.byteListToByteArray(byteList));
	}

	@Override
	protected BigInteger abstractGetOrder() {
		BigInteger size = BigInteger.valueOf(1 << Byte.SIZE);
		BigInteger order = BigInteger.ONE;
		for (int i = 0; i < this.getMaxLength() - this.getMinLength(); i++) {
			order = order.multiply(size).add(BigInteger.ONE);
		}
		return order.multiply(size.pow(this.getMinLength()));
	}

	@Override
	protected FiniteByteArrayElement abstractGetRandomElement(Random random) {
		return this.abstractGetElement(RandomUtil.getRandomBigInteger(this.getOrder().subtract(BigInteger.ONE), random));
	}

	@Override
	protected boolean abstractContains(BigInteger value) {
		return (value.signum() >= 0) && (value.compareTo(this.getOrder()) < 0);
	}

	@Override
	public boolean standardIsEqual(final Set set) {
		final FiniteByteArraySet other = (FiniteByteArraySet) set;
		return this.getMinLength() == other.getMinLength() && this.getMaxLength() == other.getMaxLength();
	}

	//
	// STATIC FACTORY METHODS
	//
	public static FiniteByteArraySet getInstance(final int maxLength) {
		return FiniteByteArraySet.getInstance(0, maxLength);
	}

	public static FiniteByteArraySet getInstance(final int minLength, final int maxLength) {
		if (minLength < 0 || maxLength < minLength) {
			throw new IllegalArgumentException();
		}
		return new FiniteByteArraySet(minLength, maxLength);
	}

	public static FiniteByteArraySet getInstance(final BigInteger minOrder) {
		return FiniteByteArraySet.getInstance(minOrder, 0);
	}

	public static FiniteByteArraySet getInstance(final BigInteger minOrder, int minLength) {
		if (minOrder == null || minOrder.signum() < 0 || minLength < 0) {
			throw new IllegalArgumentException();
		}
		int maxLength = minLength;
		BigInteger size = BigInteger.valueOf(1 << Byte.SIZE);
		BigInteger order1 = size.pow(minLength);
		BigInteger order2 = BigInteger.ONE;
		while (order1.multiply(order2).compareTo(minOrder) < 0) {
			order2 = order2.multiply(size).add(BigInteger.ONE);
			maxLength++;
		}
		return new FiniteByteArraySet(minLength, maxLength);
	}

}
