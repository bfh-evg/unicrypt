/*
 * UniCrypt
 *
 *  UniCrypt(tm): Cryptographical framework allowing the implementation of cryptographic protocols e.g. e-voting
 *  Copyright (c) 2016 Bern University of Applied Sciences (BFH), Research Institute for
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
package ch.bfh.unicrypt.math.algebra.general.classes;

import ch.bfh.unicrypt.ErrorCode;
import ch.bfh.unicrypt.UniCryptException;
import ch.bfh.unicrypt.UniCryptRuntimeException;
import ch.bfh.unicrypt.helper.converter.classes.biginteger.StringToBigInteger;
import ch.bfh.unicrypt.helper.converter.classes.string.StringToString;
import ch.bfh.unicrypt.helper.converter.interfaces.Converter;
import ch.bfh.unicrypt.helper.math.Alphabet;
import ch.bfh.unicrypt.helper.math.MathUtil;
import ch.bfh.unicrypt.helper.random.RandomByteSequence;
import ch.bfh.unicrypt.helper.sequence.Sequence;
import ch.bfh.unicrypt.math.algebra.general.abstracts.AbstractSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import java.math.BigInteger;

/**
 *
 * @author R. Haenni
 */
public class FiniteStringSet
	   extends AbstractSet<FiniteStringElement, String> {

	private static final long serialVersionUID = 1L;

	private final Alphabet alphabet;
	private final int minLength;
	private final int maxLength;

	protected FiniteStringSet(Alphabet alphabet, int minLength, int maxLength) {
		super(String.class);
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
		return this.minLength == this.maxLength;
	}

	public Alphabet getAlphabet() {
		return this.alphabet;
	}

	@Override
	protected Converter<String, String> defaultGetStringConverter() {
		return StringToString.getInstance();
	}

	@Override
	protected boolean abstractContains(String value) {
		return value.length() >= this.minLength && value.length() <= this.maxLength
			   && this.getAlphabet().containsAll(value);
	}

	@Override
	protected FiniteStringElement abstractGetElement(String value) {
		return new FiniteStringElement(this, value);
	}

	@Override
	protected Converter<String, BigInteger> abstractGetBigIntegerConverter() {
		return StringToBigInteger.getInstance(this.alphabet, 1, this.minLength);
	}

	@Override
	protected BigInteger abstractGetOrder() {
		BigInteger size = BigInteger.valueOf(this.getAlphabet().getSize());
		BigInteger order = MathUtil.ONE;
		for (int i = 0; i < this.maxLength - this.minLength; i++) {
			order = order.multiply(size).add(MathUtil.ONE);
		}
		return order.multiply(size.pow(this.minLength));
	}

	@Override
	protected Sequence<FiniteStringElement> abstractGetRandomElements(RandomByteSequence randomByteSequence) {
		return randomByteSequence.getRandomBigIntegerSequence(this.getOrder().subtract(MathUtil.ONE))
			   .map(value -> {
				   try {
					   return getElementFrom(value);
				   } catch (UniCryptException exception) {
					   throw new UniCryptRuntimeException(ErrorCode.IMPOSSIBLE_STATE, exception, this, value);
				   }
			   });
	}

	@Override
	protected boolean abstractEquals(final Set set) {
		final FiniteStringSet other = (FiniteStringSet) set;
		return this.getAlphabet() == other.getAlphabet() && this.minLength == other.minLength
			   && this.maxLength == other.maxLength;
	}

	@Override
	protected int abstractHashCode() {
		int hash = 7;
		hash = 47 * hash + this.getAlphabet().hashCode();
		hash = 47 * hash + this.minLength;
		hash = 47 * hash + this.minLength;
		return hash;
	}

	@Override
	protected String defaultToStringContent() {
		return this.getAlphabet().toString() + "^{" + this.minLength + "..." + this.maxLength + "}";
	}

	public static FiniteStringSet getInstance(final int maxLength) {
		return FiniteStringSet.getInstance(Alphabet.getInstance(), maxLength);
	}

	public static FiniteStringSet getInstance(final Alphabet alphabet, final int maxLength) {
		return FiniteStringSet.getInstance(alphabet, 0, maxLength);
	}

	public static FiniteStringSet getInstance(final int minLength, final int maxLength) {
		return FiniteStringSet.getInstance(Alphabet.getInstance(), minLength, maxLength);
	}

	public static FiniteStringSet getInstance(final Alphabet alphabet, final int minLength, final int maxLength) {
		if (alphabet == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER);
		}
		if (minLength < 0 || maxLength < minLength) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_LENGTH, minLength, maxLength);
		}
		if (minLength == maxLength) {
			return FixedStringSet.getInstance(alphabet, minLength);
		}
		return new FiniteStringSet(alphabet, minLength, maxLength);
	}

	public static FiniteStringSet getInstance(final BigInteger minOrder) {
		return FiniteStringSet.getInstance(Alphabet.getInstance(), minOrder);
	}

	public static FiniteStringSet getInstance(final Alphabet alphabet, final BigInteger minOrder) {
		return FiniteStringSet.getInstance(alphabet, minOrder, 0);
	}

	public static FiniteStringSet getInstance(final BigInteger minOrder, int minLength) {
		return FiniteStringSet.getInstance(Alphabet.getInstance(), minOrder, minLength);
	}

	public static FiniteStringSet getInstance(final Alphabet alphabet, final BigInteger minOrder, int minLength) {
		if (alphabet == null || minOrder == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, alphabet, minOrder);
		}
		if (minOrder.signum() < 0 || minLength < 0) {
			throw new UniCryptRuntimeException(ErrorCode.NEGATIVE_VALUE, minOrder, minLength);
		}
		int maxLength = minLength;
		BigInteger size = BigInteger.valueOf(alphabet.getSize());
		BigInteger order1 = size.pow(minLength);
		BigInteger order2 = MathUtil.ONE;
		while (order1.multiply(order2).compareTo(minOrder) < 0) {
			order2 = order2.multiply(size).add(MathUtil.ONE);
			maxLength++;
		}
		if (minLength == maxLength) {
			return FixedStringSet.getInstance(alphabet, minLength);
		}
		return new FiniteStringSet(alphabet, minLength, maxLength);
	}

}
