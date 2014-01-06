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

		BigInteger value1 = new BigInteger(1, this.bytes);
		BigInteger value2 = BigInteger.ZERO;
		int blockLength = this.getSet().getBlockLength();

		//As I do not know what this code really should do, I just tuned it... but the variable names I have chosen ... are stupid.
		//Futher tuning by removing the for loop and do it mathematically!
		//TODO: Describe what it does!
		if (this.getLength() > 0) {
			byte[] oneOone = new byte[this.getLength()];
			int amount = oneOone.length / blockLength;
			for (int i = 0; i < amount; i++) {
				oneOone[(oneOone.length - 1) - (i * blockLength)] = 1; //It does something like: 100000000100000000100000000 ->MSB
			}
			value2 = new BigInteger(1, oneOone);
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
