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
package ch.bfh.unicrypt.helper.sequence;

import ch.bfh.unicrypt.helper.math.MathUtil;
import java.math.BigInteger;

/**
 * Instances of this class offer an inexpensive way of creating iterators over a finite range of integers. If the upper
 * bound of the range is smaller than the lower bound of the range, then the range is empty. If no upper bound is given,
 * the sequence is infinite.
 * <p>
 * @author R. Haenni
 * @version 2.0
 */
public class BigIntegerSequence
	   extends Sequence<BigInteger> {

	private static final long serialVersionUID = 1L;

	private final BigInteger from;
	private final BigInteger to;

	protected BigIntegerSequence(BigInteger from) {
		super(Sequence.INFINITE);
		this.from = from;
		this.to = null;
	}

	protected BigIntegerSequence(BigInteger from, BigInteger to) {
		super(MathUtil.ZERO.max(to.subtract(from).add(MathUtil.ONE)));
		this.from = from;
		this.to = to;
	}

	/**
	 * Creates a new infinitely long BigInteger sequence for a given lower bound.
	 * <p>
	 * @param from The lower bound
	 * @return The new BigInteger sequence
	 */
	public static BigIntegerSequence getInstance(BigInteger from) {
		if (from == null) {
			throw new IllegalArgumentException();
		}
		return new BigIntegerSequence(from);
	}

	/**
	 * Creates a new BigInteger sequence for given lower and upper bounds. The result is an empty range, if the upper
	 * bound is smaller than the lower bound.
	 * <p>
	 * @param from The lower bound
	 * @param to   The upper bound
	 * @return The new BigInteger sequence
	 */
	public static BigIntegerSequence getInstance(long from, long to) {
		return BigIntegerSequence.getInstance(BigInteger.valueOf(from), BigInteger.valueOf(to));
	}

	/**
	 * Creates a new BigInteger sequence for given lower and upper bounds. The result is an empty range, if the upper
	 * bound is smaller than the lower bound.
	 * <p>
	 * @param from The lower bound
	 * @param to   The upper bound
	 * @return The new BigInteger sequence
	 */
	public static BigIntegerSequence getInstance(BigInteger from, BigInteger to) {
		if (from == null || to == null) {
			throw new IllegalArgumentException();
		}
		return new BigIntegerSequence(from, to);
	}

	/**
	 * Creates a new BigInteger sequence of all integers of a given bit length {@code b}. The values range from
	 * {@code 2^(b-1)} to {@code 2^b-1}.
	 * <p>
	 * @param bitLength The given bit length
	 * @return The new BigInteger sequence
	 */
	public static BigIntegerSequence getInstance(int bitLength) {
		if (bitLength < 0) {
			throw new IllegalArgumentException();
		}
		if (bitLength == 0) {
			BigIntegerSequence.getInstance(0, 0);
		}
		return BigIntegerSequence.getInstance(MathUtil.powerOfTwo(bitLength - 1),
											  MathUtil.powerOfTwo(bitLength).subtract(MathUtil.ONE));
	}

	@Override
	public SequenceIterator<BigInteger> iterator() {
		return new SequenceIterator<BigInteger>() {
			private BigInteger currentValue = from;

			@Override
			public boolean hasNext() {
				return to == null || this.currentValue.compareTo(to) <= 0;
			}

			@Override
			public BigInteger abstractNext() {
				BigInteger value = currentValue;
				this.currentValue = this.currentValue.add(MathUtil.ONE);
				return value;
			}

		};
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 41 * hash + this.getLength().intValue();
		hash = 41 * hash + this.from.intValue();
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() == obj.getClass()) {
			final BigIntegerSequence other = (BigIntegerSequence) obj;
			if (this.getLength() != other.getLength()) {
				return false;
			}
			if (this.getLength().signum() == 0) {
				return true;
			}
			return this.from.equals(other.from);
		}
		return super.equals(obj);
	}

	@Override
	protected String defaultToStringContent() {
		if (this.isEmpty()) {
			return "";
		}
		return this.from + "..." + this.to;
	}

}
