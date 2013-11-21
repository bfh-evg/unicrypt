/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.general.classes;

import ch.bfh.unicrypt.math.algebra.general.abstracts.AbstractSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.helper.Alphabet;
import ch.bfh.unicrypt.math.utility.RandomUtil;
import java.math.BigInteger;
import java.util.Random;

/**
 *
 * @author rolfhaenni
 */
public class FiniteStringSet
			 extends AbstractSet<FiniteStringElement> {

	private final Alphabet alphabet;
	private final int length;
	private final boolean equalLength;

	private FiniteStringSet(Alphabet alphabet, int length, boolean equalLength) {
		this.alphabet = alphabet;
		this.length = length;
		this.equalLength = equalLength;
	}

	public int getLength() {
		return this.length;
	}

	public boolean equalLength() {
		return this.equalLength;
	}

	public Alphabet getAlphabet() {
		return this.alphabet;
	}

	public final FiniteStringElement getElement(final String string) {
		if (string == null || string.length() > this.getLength() || (this.equalLength() && string.length() < this.getLength())) {
			throw new IllegalArgumentException();
		}
		return this.standardGetElement(string);
	}

	protected FiniteStringElement standardGetElement(String string) {
		return new FiniteStringElement(this, string);
	}

	@Override
	protected FiniteStringElement abstractGetElement(BigInteger value) {
		StringBuilder strBuilder = new StringBuilder(this.getLength());
		BigInteger size = BigInteger.valueOf(this.getAlphabet().getSize());
		while (!value.equals(BigInteger.ZERO)) {
			if (!this.equalLength()) {
				value = value.subtract(BigInteger.ONE);
			}
			strBuilder.append(this.getAlphabet().getCharacter(value.mod(size).intValue()));
			value = value.divide(size);
		}
		if (this.equalLength()) {
			while (strBuilder.length() < this.getLength()) {
				strBuilder.append(this.getAlphabet().getCharacter(0));
			}
		}
		return this.standardGetElement(strBuilder.reverse().toString());
	}

	@Override
	protected BigInteger abstractGetOrder() {
		BigInteger size = BigInteger.valueOf(this.getAlphabet().getSize());
		if (this.equalLength) {
			return size.pow(this.getLength());
		}
		BigInteger order = BigInteger.ONE;
		for (int i = 0; i < this.getLength(); i++) {
			order = order.multiply(size).add(BigInteger.ONE);
		}
		return order;
	}

	@Override
	protected FiniteStringElement abstractGetRandomElement(Random random) {
		return this.abstractGetElement(RandomUtil.getRandomBigInteger(this.getOrder().subtract(BigInteger.ONE), random));
	}

	@Override
	protected boolean abstractContains(BigInteger value) {
		return (value.signum() >= 0) && (value.compareTo(this.getOrder()) < 0);
	}

	@Override
	public boolean standardIsEqual(final Set set) {
		final FiniteStringSet other = (FiniteStringSet) set;
		return this.getAlphabet() == other.getAlphabet() && this.getLength() == other.getLength() && this.equalLength() == other.equalLength();
	}

	@Override
	public String standardToStringContent() {
		if (this.equalLength()) {
			return this.getAlphabet().toString() + "^" + this.getLength();
		} else {
			return this.getAlphabet().toString() + "^{0..." + this.getLength() + "}";
		}
	}

	//
	// STATIC FACTORY METHODS
	//
	public static FiniteStringSet getInstance(final Alphabet alphabet, final int length) {
		return FiniteStringSet.getInstance(alphabet, length, false);
	}

	public static FiniteStringSet getInstance(final Alphabet alphabet, final int length, boolean equalLength) {
		if (length < 0 || alphabet == null) {
			throw new IllegalArgumentException();
		}
		return new FiniteStringSet(alphabet, length, equalLength);
	}

}
