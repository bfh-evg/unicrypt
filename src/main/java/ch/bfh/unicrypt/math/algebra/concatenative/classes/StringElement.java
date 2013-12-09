/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.concatenative.classes;

import ch.bfh.unicrypt.math.algebra.concatenative.abstracts.AbstractConcatenativeElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public class StringElement
			 extends AbstractConcatenativeElement<StringMonoid, StringElement> {

	private final String string;

	protected StringElement(final StringMonoid monoid, final String string) {
		super(monoid);
		this.string = string;
	}

	public String getString() {
		return this.string;
	}

	@Override
	public int getLength() {
		return this.getString().length();
	}

	@Override
	protected BigInteger standardGetValue() {
		BigInteger value1 = BigInteger.ZERO;
		BigInteger alphabetSize = BigInteger.valueOf(this.getSet().getAlphabet().getSize());
		for (int i = 0; i < this.getString().length(); i++) {
			int charIndex = this.getSet().getAlphabet().getIndex(this.getString().charAt(i));
			value1 = value1.multiply(alphabetSize).add(BigInteger.valueOf(charIndex));
		}
		BigInteger value2 = BigInteger.ZERO;
		int blockLength = this.getSet().getBlockLength();
		BigInteger blockSize = alphabetSize.pow(blockLength);
		for (int i = 0; i < this.getLength() / blockLength; i++) {
			value2 = value2.multiply(blockSize).add(BigInteger.ONE);
		}
		return value1.add(value2);
	}

	@Override
	protected boolean standardIsEqual(Element element) {
		return this.getString().equals(((StringElement) element).getString());
	}

	@Override
	public String standardToStringContent() {
		return "\"" + this.getString() + "\"";
	}

}
