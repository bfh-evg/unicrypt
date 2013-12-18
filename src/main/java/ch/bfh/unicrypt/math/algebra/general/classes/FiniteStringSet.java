/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.general.classes;

import ch.bfh.unicrypt.crypto.random.interfaces.RandomGenerator;
import ch.bfh.unicrypt.math.algebra.general.abstracts.AbstractSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.helper.Alphabet;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public class FiniteStringSet
			 extends AbstractSet<FiniteStringElement> {

	private final Alphabet alphabet;
	private final int minLength;
	private final int maxLength;

	protected FiniteStringSet(Alphabet alphabet, int minLength, int maxLength) {
		this.alphabet = alphabet;
		this.minLength = minLength;
		this.maxLength = maxLength;
	}

	public int getMinLength() {
		return this.minLength;
	}

	public int getMaxLength() {
		return this.maxLength;
	}

	public boolean fixedLength() {
		return this.getMinLength() == this.getMaxLength();
	}

	public Alphabet getAlphabet() {
		return this.alphabet;
	}

	public final FiniteStringElement getElement(final String string) {
		if (string == null || string.length() < this.getMinLength() || string.length() > this.getMaxLength()) {
			throw new IllegalArgumentException();
		}
		return this.standardGetElement(string);
	}

	protected FiniteStringElement standardGetElement(String string) {
		return new FiniteStringElement(this, string);
	}

	@Override
	protected FiniteStringElement abstractGetElement(BigInteger value) {
		int minLength = this.getMinLength();
		BigInteger size = BigInteger.valueOf(this.getAlphabet().getSize());
		StringBuilder strBuilder = new StringBuilder(this.getMaxLength());
		while (!value.equals(BigInteger.ZERO) || strBuilder.length() < minLength) {
			if (strBuilder.length() >= minLength) {
				value = value.subtract(BigInteger.ONE);
			}
			strBuilder.append(this.getAlphabet().getCharacter(value.mod(size).intValue()));
			value = value.divide(size);
		}
		return this.standardGetElement(strBuilder.reverse().toString());
	}

	@Override
	protected BigInteger abstractGetOrder() {
		BigInteger size = BigInteger.valueOf(this.getAlphabet().getSize());
		BigInteger order = BigInteger.ONE;
		for (int i = 0; i < this.getMaxLength() - this.getMinLength(); i++) {
			order = order.multiply(size).add(BigInteger.ONE);
		}
		return order.multiply(size.pow(this.getMinLength()));
	}

	@Override
	protected FiniteStringElement abstractGetRandomElement(RandomGenerator randomGenerator) {
		return this.abstractGetElement(randomGenerator.nextBigInteger(this.getOrder().subtract(BigInteger.ONE)));
	}

	@Override
	protected boolean abstractContains(BigInteger value) {
		return (value.signum() >= 0) && (value.compareTo(this.getOrder()) < 0);
	}

	@Override
	public boolean standardIsEquivalent(final Set set) {
		final FiniteStringSet other = (FiniteStringSet) set;
		return this.getAlphabet() == other.getAlphabet() && this.getMinLength() == other.getMinLength() && this.getMaxLength() == other.getMaxLength();
	}

	@Override
	public String standardToStringContent() {
		return this.getAlphabet().toString() + "^{" + this.getMinLength() + "..." + this.getMaxLength() + "}";
	}

	//
	// STATIC FACTORY METHODS
	//
	public static FiniteStringSet getInstance(final Alphabet alphabet, final int maxLength) {
		return FiniteStringSet.getInstance(alphabet, 0, maxLength);
	}

	public static FiniteStringSet getInstance(final Alphabet alphabet, final int minLength, final int maxLength) {
		if (alphabet == null || minLength < 0 || maxLength < minLength) {
			throw new IllegalArgumentException();
		}
		return new FiniteStringSet(alphabet, minLength, maxLength);
	}

	public static FiniteStringSet getInstance(final Alphabet alphabet, final BigInteger minOrder) {
		return FiniteStringSet.getInstance(alphabet, minOrder, 0);
	}

	public static FiniteStringSet getInstance(final Alphabet alphabet, final BigInteger minOrder, int minLength) {
		if (alphabet == null || minOrder == null || minOrder.signum() < 0 || minLength < 0) {
			throw new IllegalArgumentException();
		}
		int maxLength = minLength;
		BigInteger size = BigInteger.valueOf(alphabet.getSize());
		BigInteger order1 = size.pow(minLength);
		BigInteger order2 = BigInteger.ONE;
		while (order1.multiply(order2).compareTo(minOrder) < 0) {
			order2 = order2.multiply(size).add(BigInteger.ONE);
			maxLength++;
		}
		return new FiniteStringSet(alphabet, minLength, maxLength);
	}

}
