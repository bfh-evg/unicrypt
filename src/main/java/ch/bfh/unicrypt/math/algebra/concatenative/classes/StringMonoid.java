/* 
 * UniCrypt
 * 
 *  UniCrypt(tm) : Cryptographical framework allowing the implementation of cryptographic protocols e.g. e-voting
 *  Copyright (C) 2014 Bern University of Applied Sciences (BFH), Research Institute for
 *  Security in the Information Society (RISIS), E-Voting Group (EVG)
 *  Quellgasse 21, CH-2501 Biel, Switzerland
 * 
 *  Licensed under Dual License consisting of:
 *  1. GNU Affero General Public License (AGPL) v3
 *  and
 *  2. Commercial license
 * 
 *
 *  1. This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 *
 *  2. Licensees holding valid commercial licenses for UniCrypt may use this file in
 *   accordance with the commercial license agreement provided with the
 *   Software or, alternatively, in accordance with the terms contained in
 *   a written agreement between you and Bern University of Applied Sciences (BFH), Research Institute for
 *   Security in the Information Society (RISIS), E-Voting Group (EVG)
 *   Quellgasse 21, CH-2501 Biel, Switzerland.
 * 
 *
 *   For further information contact <e-mail: unicrypt@bfh.ch>
 * 
 *
 * Redistributions of files must retain the above copyright notice.
 */
package ch.bfh.unicrypt.math.algebra.concatenative.classes;

import ch.bfh.unicrypt.crypto.random.interfaces.RandomGenerator;
import ch.bfh.unicrypt.math.algebra.concatenative.abstracts.AbstractConcatenativeMonoid;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.helper.Alphabet;
import java.math.BigInteger;

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
		return this.getRandomElement(length, null);
	}

	@Override
	public final StringElement getRandomElement(int length, RandomGenerator randomGenerator) {
		if (length < 0 || length % this.getBlockLength() != 0) {
			throw new IllegalArgumentException();
		}
		char[] chars = new char[length];
		for (int i = 0; i < length; i++) {
			chars[i] = this.getAlphabet().getCharacter(randomGenerator.nextInteger(this.getAlphabet().getSize() - 1));
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
	protected StringElement abstractApply(StringElement element1, StringElement element2) {
		return this.standardGetElement(element1.getString() + element2.getString());
	}

	@Override
	protected BigInteger abstractGetOrder() {
		return Set.INFINITE_ORDER;
	}

	@Override
	protected StringElement abstractGetRandomElement(RandomGenerator randomGenerator) {
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
