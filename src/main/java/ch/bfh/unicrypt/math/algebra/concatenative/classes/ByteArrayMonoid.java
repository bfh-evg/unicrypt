/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.concatenative.classes;

import ch.bfh.unicrypt.math.algebra.concatenative.abstracts.AbstractConcatenativeMonoid;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.utility.ArrayUtil;
import ch.bfh.unicrypt.math.utility.RandomUtil;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

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
		if (bytes == null || bytes.length % this.getBlockLength() != 0) {
			throw new IllegalArgumentException();
		}
		return this.standardGetElement(bytes);
	}

	@Override
	public final ByteArrayElement getRandomElement(int length) {
		return this.getRandomElement(length, (Random) null);
	}

	@Override
	public final ByteArrayElement getRandomElement(int length, Random random) {
		if (length < 0 || length % this.getBlockLength() != 0) {
			throw new IllegalArgumentException();
		}
		byte[] bytes = new byte[length];
		for (int i = 0; i < length; i++) {
			bytes[i] = RandomUtil.getRandomByte(random);
		}
		return this.standardGetElement(bytes);
	}

	protected ByteArrayElement standardGetElement(byte[] bytes) {
		return new ByteArrayElement(this, bytes);
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
		return this.standardGetElement(ArrayUtil.byteListToByteArray(byteList));
	}

	//
	// The following protected methods implement the abstract methods from
	// various super-classes
	//
	@Override
	protected ByteArrayElement abstractGetIdentityElement() {
		return this.standardGetElement(new byte[]{});
	}

	@Override
	protected ByteArrayElement abstractApply(Element element1, Element element2) {
		byte[] bytes1 = ((ByteArrayElement) element1).getByteArray();
		byte[] bytes2 = ((ByteArrayElement) element2).getByteArray();
		byte[] result = Arrays.copyOf(bytes1, bytes1.length + bytes2.length);
		System.arraycopy(bytes2, 0, result, bytes2.length, bytes2.length);
		return this.standardGetElement(result);
	}

	@Override
	protected BigInteger abstractGetOrder() {
		return Set.INFINITE_ORDER;
	}

	@Override
	protected ByteArrayElement abstractGetRandomElement(Random random) {
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
