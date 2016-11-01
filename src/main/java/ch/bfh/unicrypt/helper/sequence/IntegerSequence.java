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

import java.math.BigInteger;

/**
 * Instances of this class offer an inexpensive way of creating iterators over a finite range of integers. If the upper
 * bound of the range is smaller than the lower bound of the range, then the range is empty.
 * <p>
 * @author R. Haenni
 * @version 2.0
 */
public class IntegerSequence
	   extends Sequence<Integer> {

	private static final long serialVersionUID = 1L;

	private final int from;
	private final int to;

	protected IntegerSequence(int from, int to) {
		super(BigInteger.valueOf(Math.max(to - from + 1, 0)));
		this.from = from;
		this.to = to;
	}

	/**
	 * Creates a new integer sequence for given upper and lower integer bounds. The result is an empty range, if the
	 * upper bound is smaller than the lower bound.
	 * <p>
	 * @param from The lower bound
	 * @param to   The upper bound
	 * @return The new integer sequence
	 */
	public static IntegerSequence getInstance(int from, int to) {
		return new IntegerSequence(from, to);
	}

	@Override
	public SequenceIterator<Integer> iterator() {
		return new SequenceIterator<Integer>() {
			private int currentValue = from;

			@Override
			public boolean hasNext() {
				return this.currentValue <= to;
			}

			@Override
			public Integer abstractNext() {
				Integer value = currentValue;
				this.currentValue = this.currentValue + 1;
				return value;
			}

		};
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 41 * hash + this.getLength().intValue();
		hash = 41 * hash + this.from;
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() == obj.getClass()) {
			final IntegerSequence other = (IntegerSequence) obj;
			if (!this.getLength().equals(other.getLength())) {
				return false;
			}
			if (this.getLength().signum() == 0) {
				return true;
			}
			return this.from == other.from;
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
