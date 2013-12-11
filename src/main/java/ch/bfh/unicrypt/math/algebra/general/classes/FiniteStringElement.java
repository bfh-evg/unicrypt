/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.general.classes;

import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringElement;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringMonoid;
import ch.bfh.unicrypt.math.algebra.general.abstracts.AbstractElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public class FiniteStringElement
			 extends AbstractElement<FiniteStringSet, FiniteStringElement> {

	private final String string;

	protected FiniteStringElement(final FiniteStringSet set, final String string) {
		super(set);
		this.string = string;
	}

	public String getString() {
		return this.string;
	}

	public int getLength() {
		return this.getString().length();
	}

	public StringElement getByteArrayElement() {
		return StringMonoid.getInstance(this.getSet().getAlphabet()).getElement(this.getString());
	}

	@Override
	protected BigInteger standardGetValue() {
		int length = this.getString().length();
		int minLength = this.getSet().getMinLength();
		BigInteger value = BigInteger.ZERO;
		BigInteger size = BigInteger.valueOf(this.getSet().getAlphabet().getSize());
		for (int i = 0; i < length; i++) {
			int charIndex = this.getSet().getAlphabet().getIndex(this.getString().charAt(i));
			if (i < length - minLength) {
				charIndex++;
			}
			value = value.multiply(size).add(BigInteger.valueOf(charIndex));
		}
		return value;
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
