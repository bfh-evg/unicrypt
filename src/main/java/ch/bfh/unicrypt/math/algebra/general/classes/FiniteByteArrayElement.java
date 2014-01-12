/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.general.classes;

import ch.bfh.unicrypt.math.algebra.concatenative.classes.ByteArrayElement;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.ByteArrayMonoid;
import ch.bfh.unicrypt.math.algebra.general.abstracts.AbstractElement;
import ch.bfh.unicrypt.math.helper.ByteArray;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public class FiniteByteArrayElement
	   extends AbstractElement<FiniteByteArraySet, FiniteByteArrayElement> {

	private final ByteArray byteArray;

	protected FiniteByteArrayElement(final FiniteByteArraySet set, final ByteArray byteArray) {
		super(set);
		this.byteArray = byteArray;
	}

	public ByteArray getByteArray() {
		return this.byteArray;
	}

	public int getLength() {
		return this.byteArray.getLength();
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
			int intValue = this.byteArray.getByte(i) & 0xFF;
			if (i < length - minLength) {
				intValue++;
			}
			value = value.multiply(size).add(BigInteger.valueOf(intValue));
		}
		return value;
	}

	@Override
	protected boolean standardIsEquivalent(FiniteByteArrayElement element) {
		return this.byteArray.equals(element.byteArray);
	}

	@Override
	public String standardToStringContent() {
		return this.byteArray.toString();
	}

}
