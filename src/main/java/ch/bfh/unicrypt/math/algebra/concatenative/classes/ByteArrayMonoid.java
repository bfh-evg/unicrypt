/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.concatenative.classes;

import ch.bfh.unicrypt.crypto.random.interfaces.RandomGenerator;
import ch.bfh.unicrypt.math.algebra.concatenative.abstracts.AbstractConcatenativeMonoid;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.helper.ByteArray;
import ch.bfh.unicrypt.math.utility.ArrayUtil;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author rolfhaenni
 */
public class ByteArrayMonoid
	   extends AbstractConcatenativeMonoid<ByteArrayElement> {

	private final int blockLength;

	private ByteArrayMonoid(int blockLength) {
		this.blockLength = blockLength;
	}

	@Override
	public int getBlockLength() {
		return this.blockLength;
	}

	public final ByteArrayElement getElement(final byte[] bytes) {
		return this.getElement(ByteArray.getInstance(bytes));
	}

	public final ByteArrayElement getElement(final ByteArray byteArray) {
		if (byteArray == null || byteArray.getLength() % this.getBlockLength() != 0) {
			throw new IllegalArgumentException();
		}
		return this.standardGetElement(byteArray);
	}

	@Override
	public final ByteArrayElement getRandomElement(int length) {
		return this.getRandomElement(length, null);
	}

	@Override
	public final ByteArrayElement getRandomElement(int length, RandomGenerator randomGenerator) {
		if (length < 0 || length % this.getBlockLength() != 0) {
			throw new IllegalArgumentException();
		}
		return this.standardGetElement(ByteArray.getRandomInstance(length, randomGenerator));
	}

	protected ByteArrayElement standardGetElement(ByteArray byteArray) {
		return new ByteArrayElement(this, byteArray);
	}

	@Override
	protected ByteArrayElement abstractGetElement(BigInteger value) {
		int blockLength = this.getBlockLength();
		LinkedList<Byte> byteList = new LinkedList<Byte>();
		BigInteger byteSize = BigInteger.valueOf(1 << Byte.SIZE);
		BigInteger blockSize = BigInteger.valueOf(1 << (Byte.SIZE * blockLength));
		while (!value.equals(BigInteger.ZERO)) {
			value = value.subtract(BigInteger.ONE);
			BigInteger remainder = value.mod(blockSize);
			for (int i = 0; i < blockLength; i++) {
				byteList.addFirst(remainder.mod(byteSize).byteValue());
				remainder = remainder.divide(byteSize);
			}
			value = value.divide(blockSize);
		}
		return this.standardGetElement(ByteArray.getInstance(ArrayUtil.byteListToByteArray(byteList)));
	}

	//
	// The following protected methods implement the abstract methods from
	// various super-classes
	//
	@Override
	protected ByteArrayElement abstractGetIdentityElement() {
		return this.standardGetElement(ByteArray.getInstance());
	}

	@Override
	protected ByteArrayElement abstractApply(ByteArrayElement element1, ByteArrayElement element2) {
		return this.standardGetElement(element1.getByteArray().concatenate(element2.getByteArray()));
	}

	@Override
	protected BigInteger abstractGetOrder() {
		return Set.INFINITE_ORDER;
	}

	@Override
	protected ByteArrayElement abstractGetRandomElement(RandomGenerator randomGenerator) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected boolean abstractContains(BigInteger value) {
		return value.signum() >= 0;
	}
	//
	// STATIC FACTORY METHODS
	//
	private static final Map<Integer, ByteArrayMonoid> instances = new HashMap<Integer, ByteArrayMonoid>();

	public static ByteArrayMonoid getInstance() {
		return ByteArrayMonoid.getInstance(1);
	}

	/**
	 * Returns the singleton object of this class.
	 * <p>
	 * @param blockLength
	 * @return The singleton object of this class
	 */
	public static ByteArrayMonoid getInstance(int blockLength) {
		if (blockLength < 1) {
			throw new IllegalArgumentException();
		}
		ByteArrayMonoid instance = ByteArrayMonoid.instances.get(Integer.valueOf(blockLength));
		if (instance == null) {
			instance = new ByteArrayMonoid(blockLength);
			ByteArrayMonoid.instances.put(Integer.valueOf(blockLength), instance);
		}
		return instance;
	}

}
