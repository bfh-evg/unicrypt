/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.concatenative.classes;

import ch.bfh.unicrypt.math.algebra.concatenative.abstracts.AbstractConcatenativeElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import java.math.BigInteger;
import java.util.Arrays;

/**
 *
 * @author rolfhaenni
 */
public class ByteArrayElement
			 extends AbstractConcatenativeElement<ByteArrayMonoid, ByteArrayElement> {

	private final byte[] bytes;

	protected ByteArrayElement(final ByteArrayMonoid monoid, final byte[] bytes) {
		super(monoid);
		this.bytes = bytes;
	}

	public byte[] getByteArray() {
		return this.bytes;
	}

	@Override
	public int getLength() {
		return this.getByteArray().length;
	}

	@Override
	protected BigInteger standardGetValue() {
		BigInteger value = BigInteger.ZERO;
		BigInteger size = BigInteger.valueOf(256);
		for (byte b : this.getByteArray()) {
			int intValue = b & 0xFF;
			value = value.multiply(size).add(BigInteger.valueOf(intValue + 1));
		}
		return value;
	}

	@Override
	protected boolean standardIsEqual(Element element) {
		return Arrays.equals(this.getByteArray(), ((ByteArrayElement) element).getByteArray());
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
