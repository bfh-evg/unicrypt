/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.general.classes;

import ch.bfh.unicrypt.math.algebra.concatenative.classes.ByteArrayElement;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.ByteArrayMonoid;
import ch.bfh.unicrypt.math.algebra.general.abstracts.AbstractElement;
import java.math.BigInteger;
import java.util.Arrays;

/**
 *
 * @author rolfhaenni
 */
public class FiniteByteArrayElement
			 extends AbstractElement<FiniteByteArraySet, FiniteByteArrayElement> {

	private final byte[] bytes;

	protected FiniteByteArrayElement(final FiniteByteArraySet set, final byte[] bytes) {
		super(set);
		this.bytes = bytes;
	}

	public byte[] getByteArray() {
		return this.bytes;
	}

	public int getLength() {
		return this.getByteArray().length;
	}

	public ByteArrayElement getByteArrayElement() {
		return ByteArrayMonoid.getInstance().getElement(this.getByteArray());
	}

	@Override
	protected BigInteger standardGetValue() {
		int length = this.getLength();
		int minLength = this.getSet().getMinLength();
		BigInteger value = BigInteger.ZERO;
		BigInteger size = BigInteger.valueOf(1 << Byte.SIZE);
		for (int i = 0; i < length; i++) {
			int intValue = this.getByteArray()[i] & 0xFF;
			if (i < length - minLength) {
				intValue++;
			}
			value = value.multiply(size).add(BigInteger.valueOf(intValue));
		}
		return value;
	}

	@Override
	protected boolean standardIsEquivalent(FiniteByteArrayElement element) {
		return Arrays.equals(this.getByteArray(), element.getByteArray());
	}

	@Override
	public String standardToStringContent() {
		String str = "";
		String delimiter = "";
		for (int i = 0; i < this.getLength(); i++) {
			str = str + delimiter + String.format("%02X", BigInteger.valueOf(this.getByteArray()[i] & 0xFF));
			delimiter = "|";
		}
		return "\"" + str + "\"";
	}

}
