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
package ch.bfh.unicrypt.helper.numerical;

import java.math.BigInteger;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class WholeNumber
	   extends Numerical<WholeNumber> {

	public static final NaturalNumber ZERO = new NaturalNumber(BigInteger.ZERO);
	public static final NaturalNumber ONE = new NaturalNumber(BigInteger.ONE);

	private static final BigInteger MAX_INTEGER = BigInteger.valueOf(Integer.MAX_VALUE);

	protected WholeNumber(BigInteger bigInteger) {
		super(bigInteger);
	}

	@Override
	protected boolean abstractIsCompatible(WholeNumber other) {
		return true;
	}

	@Override
	protected WholeNumber abstractAdd(WholeNumber other) {
		return WholeNumber.getInstance(this.bigInteger.add(other.bigInteger));
	}

	@Override
	protected WholeNumber abstractMultiply(WholeNumber other) {
		return WholeNumber.getInstance(this.bigInteger.multiply(other.bigInteger));
	}

	@Override
	protected WholeNumber abstractSubtract(WholeNumber other) {
		return WholeNumber.getInstance(this.bigInteger.subtract(other.bigInteger));
	}

	@Override
	protected WholeNumber abstractNegate() {
		return WholeNumber.getInstance(this.bigInteger.negate());
	}

	@Override
	protected WholeNumber abstractTimes(BigInteger factor) {
		return WholeNumber.getInstance(this.bigInteger.multiply(factor));
	}

	@Override
	protected WholeNumber abstractPower(BigInteger exponent) {
		if (exponent.compareTo(MAX_INTEGER) <= 0) {
			return WholeNumber.getInstance(this.bigInteger.pow(exponent.intValue()));
		}
		if (this.bigInteger.equals(BigInteger.ZERO) || this.bigInteger.equals(BigInteger.ONE)) {
			return this;
		}
		if (this.bigInteger.equals(BigInteger.valueOf(-1))) {
			if (exponent.testBit(0)) {
				return this;
			}
			return ONE;
		}
		// TODO: Implement square and multiply!
		throw new UnsupportedOperationException();
	}

	@Override
	protected String defaultToStringValue() {
		return this.bigInteger.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		WholeNumber other = (WholeNumber) obj;
		return this.bigInteger.equals(other.bigInteger);
	}

	@Override
	public int hashCode() {
		int hash = 17;
		hash = 17 * hash + this.getClass().hashCode();
		hash = 17 * hash + this.bigInteger.hashCode();
		return hash;
	}

	public static WholeNumber getInstance(int integer) {
		return WholeNumber.getInstance(BigInteger.valueOf(integer));
	}

	public static WholeNumber getInstance(BigInteger integer) {
		if (integer == null) {
			throw new IllegalArgumentException();
		}
		if (integer.signum() >= 0) {
			return new NaturalNumber(integer);
		}
		return new WholeNumber(integer);
	}

}
