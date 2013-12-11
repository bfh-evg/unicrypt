/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.concatenative.classes;

import ch.bfh.unicrypt.math.algebra.concatenative.abstracts.AbstractConcatenativeMonoid;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.helper.Alphabet;
import ch.bfh.unicrypt.math.utility.RandomUtil;
import java.math.BigInteger;
import java.util.Random;

/**
 *
 * @author rolfhaenni
 */
public class StringMonoid
			 extends AbstractConcatenativeMonoid<StringElement> {

	private final Alphabet alphabet;
	private final int blockLength;

	private StringMonoid(Alphabet alphabet, int blockLength) {
		this.alphabet = alphabet;
		this.blockLength = blockLength;
	}

	public Alphabet getAlphabet() {
		return this.alphabet;
	}

	@Override
	public int getBlockLength() {
		return this.blockLength;
	}

	public final StringElement getElement(final String string) {
		if (string == null || !this.getAlphabet().isValid(string) || string.length() % this.getBlockLength() != 0) {
			throw new IllegalArgumentException();
		}
		return this.standardGetElement(string);
	}

	@Override
	public final StringElement getRandomElement(int length) {
		return this.getRandomElement(length, (Random) null);
	}

	@Override
	public final StringElement getRandomElement(int length, Random random) {
		if (length < 0 || length % this.getBlockLength() != 0) {
			throw new IllegalArgumentException();
		}
		char[] chars = new char[length];
		for (int i = 0; i < length; i++) {
			chars[i] = this.getAlphabet().getCharacter(RandomUtil.getRandomInteger(this.getAlphabet().getSize() - 1, random));
		}
		return this.standardGetElement(new String(chars));
	}

	protected StringElement standardGetElement(String string) {
		return new StringElement(this, string);
	}

	@Override
	protected StringElement abstractGetElement(BigInteger value) {
		int blockLength = this.getBlockLength();
		StringBuilder strBuilder = new StringBuilder();
		BigInteger alphabetSize = BigInteger.valueOf(this.getAlphabet().getSize());
		BigInteger blockSize = alphabetSize.pow(blockLength);
		while (!value.equals(BigInteger.ZERO)) {
			value = value.subtract(BigInteger.ONE);
			BigInteger remainder = value.mod(blockSize);
			for (int i = 0; i < blockLength; i++) {
				strBuilder.append(this.getAlphabet().getCharacter(remainder.mod(alphabetSize).intValue()));
				remainder = remainder.divide(alphabetSize);
			}
			value = value.divide(blockSize);
		}
		return this.standardGetElement(strBuilder.reverse().toString());
	}

	//
	// The following protected methods implement the abstract methods from
	// various super-classes
	//
	@Override
	protected StringElement abstractGetIdentityElement() {
		return this.standardGetElement("");
	}

	@Override
	protected StringElement abstractApply(Element element1, Element element2) {
		return this.standardGetElement(((StringElement) element1).getString() + ((StringElement) element2).getString());
	}

	@Override
	protected BigInteger abstractGetOrder() {
		return Set.INFINITE_ORDER;
	}

	@Override
	protected StringElement abstractGetRandomElement(Random random) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected boolean abstractContains(BigInteger value) {
		return value.signum() >= 0;
	}

	//
	// STATIC FACTORY METHODS
	//
	public static StringMonoid getInstance(Alphabet alphabet) {
		return StringMonoid.getInstance(alphabet, 1);
	}

	public static StringMonoid getInstance(Alphabet alphabet, int blockLength) {
		if (alphabet == null || blockLength < 1) {
			throw new IllegalArgumentException();
		}
		return new StringMonoid(alphabet, blockLength);
	}

}
