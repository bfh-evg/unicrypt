/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.random.classes;

import ch.bfh.unicrypt.crypto.random.interfaces.RandomReferenceString;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.ByteArrayMonoid;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.helper.HashMethod;
import java.util.HashMap;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class PseudoRandomReferenceString
	   extends PseudoRandomGenerator
	   implements RandomReferenceString {

	private HashMap<Integer, byte[]> randomByteBufferMap;
	private int javaHashValue;

	public PseudoRandomReferenceString(HashMethod hashMethod, Element seed) {
		super(hashMethod, seed);
	}

	protected byte[] getRandomByteBuffer(int counter) {
		if (randomByteBufferMap == null) {
			randomByteBufferMap = new HashMap<Integer, byte[]>();
		}
		if (!randomByteBufferMap.containsKey(counter)) {
			randomByteBufferMap.put(counter, super.getRandomByteBuffer(counter));
		}
		return randomByteBufferMap.get(counter);
	}

	@Override
	public void reset() {
		super.reset();
	}

	@Override
	public boolean isReset() {
		return super.isReset();
	}

	public static PseudoRandomReferenceString getInstance(byte[] query) {
		if (query == null) {
			throw new IllegalArgumentException();
		}
		return getInstance(ByteArrayMonoid.getInstance().getElement(query));
	}

	public static PseudoRandomReferenceString getInstance() {
		return PseudoRandomReferenceString.getInstance(HashMethod.DEFAULT, PseudoRandomGenerator.DEFAULT_SEED);
	}

	public static PseudoRandomReferenceString getInstance(Element seed) {
		return PseudoRandomReferenceString.getInstance(HashMethod.DEFAULT, seed);
	}

	public static PseudoRandomReferenceString getInstance(HashMethod hashMethod) {
		return PseudoRandomReferenceString.getInstance(hashMethod, PseudoRandomGenerator.DEFAULT_SEED);
	}

	public static PseudoRandomReferenceString getInstance(HashMethod hashMethod, Element seed) {
		if (hashMethod == null || seed == null) {
			throw new IllegalArgumentException();
		}
		return new PseudoRandomReferenceString(hashMethod, seed);
	}

	@Override
	public int hashCode() {
		if (javaHashValue == 0) {
			javaHashValue = getHashMethod().hashCode() + getSeed().getValue().hashCode();
		}
		return javaHashValue;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final PseudoRandomReferenceString other = (PseudoRandomReferenceString) obj;
		if (getHashMethod() != getHashMethod() && (!this.getHashMethod().equals(other.getHashMethod()))) {
			return false;
		}

		if (this.getSeed().getValue() != other.getSeed().getValue()) {
			return false;
		}
		return true;
	}

}
