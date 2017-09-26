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
import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.converter.classes.biginteger.ByteArrayToBigInteger;
import ch.bfh.unicrypt.helper.converter.classes.bytearray.ByteArrayToByteArray;
import ch.bfh.unicrypt.helper.converter.interfaces.Converter;
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
public class FiniteByteArraySet
	   extends AbstractSet<FiniteByteArrayElement, ByteArray> {

	private static final long serialVersionUID = 1L;

	private final int minLength;
	private final int maxLength;

	protected FiniteByteArraySet(int minLength, int maxLength) {
		super(ByteArray.class);
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
		return this.minLength == maxLength;
	}

	public final FiniteByteArrayElement getElement(byte[] bytes) {
		return this.getElement(ByteArray.getInstance(bytes));
	}

	// for strings of the form "00|95|2B|9B|E2|FD|30|89"
	public final FiniteByteArrayElement getElement(String string) {
		return this.getElement(ByteArray.getInstance(string));
	}

	@Override
	protected Converter<ByteArray, ByteArray> defaultGetByteArrayConverter() {
		return ByteArrayToByteArray.getInstance();
	}

	@Override
	protected boolean abstractContains(ByteArray value) {
		return value.getLength() >= this.minLength && value.getLength() <= this.getMaxLength();
	}

	@Override
	protected FiniteByteArrayElement abstractGetElement(ByteArray value) {
		return new FiniteByteArrayElement(this, value);
	}

	@Override
	protected Converter<ByteArray, BigInteger> abstractGetBigIntegerConverter() {
		return ByteArrayToBigInteger.getInstance(1, this.minLength);
	}

	@Override
	protected BigInteger abstractGetOrder() {
		BigInteger size = MathUtil.powerOfTwo(Byte.SIZE);
		BigInteger order = MathUtil.ONE;
		for (int i = 0; i < this.maxLength - this.minLength; i++) {
			order = order.multiply(size).add(MathUtil.ONE);
		}
		return order.multiply(size.pow(this.minLength));
	}

	@Override
	protected Sequence<FiniteByteArrayElement> abstractGetRandomElements(RandomByteSequence randomByteSequence) {
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
	public boolean abstractEquals(final Set set) {
		final FiniteByteArraySet other = (FiniteByteArraySet) set;
		return this.minLength == other.minLength && this.maxLength == other.maxLength;
	}

	@Override
	protected int abstractHashCode() {
		int hash = 7;
		hash = 47 * hash + this.minLength;
		hash = 47 * hash + this.maxLength;
		return hash;
	}

	public static FiniteByteArraySet getInstance(final int maxLength) {
		return FiniteByteArraySet.getInstance(0, maxLength);
	}

	public static FiniteByteArraySet getInstance(final int minLength, final int maxLength) {
		if (minLength < 0 || maxLength < minLength) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_LENGTH, minLength, maxLength);
		}
		if (minLength == maxLength) {
			return FixedByteArraySet.getInstance(minLength);
		}
		return new FiniteByteArraySet(minLength, maxLength);
	}

	public static FiniteByteArraySet getInstance(final BigInteger minOrder) {
		return FiniteByteArraySet.getInstance(minOrder, 0);
	}

	public static FiniteByteArraySet getInstance(final BigInteger minOrder, int minLength) {
		if (minOrder == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER);
		}
		if (minOrder.signum() < 0 || minLength < 0) {
			throw new UniCryptRuntimeException(ErrorCode.NEGATIVE_VALUE, minOrder, minLength);
		}
		int maxLength = minLength;
		BigInteger size = MathUtil.powerOfTwo(Byte.SIZE);
		BigInteger order1 = size.pow(minLength);
		BigInteger order2 = MathUtil.ONE;
		while (order1.multiply(order2).compareTo(minOrder) < 0) {
			order2 = order2.multiply(size).add(MathUtil.ONE);
			maxLength++;
		}
		if (minLength == maxLength) {
			return FixedByteArraySet.getInstance(minLength);
		}
		return new FiniteByteArraySet(minLength, maxLength);
	}

}
