/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.concatenative.classes;

import ch.bfh.unicrypt.math.algebra.concatenative.abstracts.AbstractConcatenativeElement;
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
		return this.bytes.clone();
	}

	@Override
	public int getLength() {
		return this.bytes.length;
	}

	@Override
	protected BigInteger standardGetValue() {
		BigInteger value1 = BigInteger.ZERO;
		BigInteger byteSize = BigInteger.valueOf(1 << Byte.SIZE);
		for (byte b : this.bytes) {
			int intValue = b & 0xFF;
			value1 = value1.multiply(byteSize).add(BigInteger.valueOf(intValue));
		}
		BigInteger value2 = BigInteger.ZERO;
		int blockLength = this.getSet().getBlockLength();
		BigInteger blockSize = BigInteger.valueOf(1 << (Byte.SIZE * blockLength));
		for (int i = 0; i < this.getLength() / blockLength; i++) {
			value2 = value2.multiply(blockSize).add(BigInteger.ONE);
		}
		return value1.add(value2);
	}

	@Override
	protected boolean standardIsEquivalent(ByteArrayElement element) {
		return Arrays.equals(this.bytes, element.bytes);
	}

	@Override
	public String standardToStringContent() {
		String str = "";
		String delimiter = "";
		for (int i = 0; i < this.getLength(); i++) {
			str = str + delimiter + String.format("%02X", BigInteger.valueOf(bytes[i] & 0xFF));
			if ((i + 1) % this.getSet().getBlockLength() == 0) {
				delimiter = "|";

			} else {
				delimiter = "-";
			}
		}
		return "\"" + str + "\"";
	}

}
