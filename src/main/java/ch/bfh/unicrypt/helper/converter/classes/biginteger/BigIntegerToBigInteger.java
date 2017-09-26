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
package ch.bfh.unicrypt.helper.converter.classes.biginteger;

import ch.bfh.unicrypt.helper.converter.abstracts.AbstractBigIntegerConverter;
import ch.bfh.unicrypt.helper.math.MathUtil;
import java.math.BigInteger;

/**
 * Instances of this class convert {@code BigInteger} values into non-negative {@code BigInteger} values 0, 1, 2, ...
 * There are two operation modes. In the unrestricted mode, the folding function
 * {@link MathUtil#fold(java.math.BigInteger)} is used to transform negative into positive integers. In the restricted
 * operation mode, a minimal value {@code m} must be specified, and only values {@code v >= m} are converted into v-m >=
 * 0;
 * <p>
 * @author R. Haenni
 * @version 2.0
 */
public class BigIntegerToBigInteger
	   extends AbstractBigIntegerConverter<BigInteger> {

	private static final long serialVersionUID = 1L;

	// null if no minimal value is specified
	private final BigInteger minValue;

	protected BigIntegerToBigInteger() {
		this(null);
	}

	protected BigIntegerToBigInteger(BigInteger minValue) {
		super(BigInteger.class);
		this.minValue = minValue;
	}

	/**
	 * Creates a new unrestricted {@link BigIntegerToBigInteger} converter. Negative input values are converted into
	 * non-negative values.
	 * <p>
	 * @return The new converter
	 */
	public static BigIntegerToBigInteger getInstance() {
		return new BigIntegerToBigInteger();
	}

	/**
	 * This is a convenience method to creates new converters for minimal values of type {@code int}.
	 * <p>
	 * @param minValue The minimal value
	 * @return The new converter
	 * @see BigIntegerToBigInteger#getInstance(java.math.BigInteger)
	 */
	public static BigIntegerToBigInteger getInstance(long minValue) {
		return BigIntegerToBigInteger.getInstance(BigInteger.valueOf(minValue));
	}

	/**
	 * Creates a new {@link BigIntegerToBigInteger} converter for a given minimal value.
	 * <p>
	 * @param minValue The minimal value
	 * @return The new converter
	 */
	public static BigIntegerToBigInteger getInstance(BigInteger minValue) {
		if (minValue == null) {
			throw new IllegalArgumentException();
		}
		return new BigIntegerToBigInteger(minValue);
	}

	/**
	 * This is a convenience method to allow inputs of type {@code long}.
	 * <p>
	 * @param value The given value
	 * @return The resulting byte array
	 */
	public BigInteger convert(long value) {
		return this.convert(BigInteger.valueOf(value));
	}

	@Override
	protected boolean defaultIsValidInput(BigInteger value) {
		if (this.minValue != null) {
			return value.compareTo(this.minValue) >= 0;

		}
		return true;
	}

	@Override
	protected boolean defaultIsValidOutput(BigInteger value) {
		return value.signum() >= 0;
	}

	@Override
	protected BigInteger abstractConvert(BigInteger value) {
		if (this.minValue == null) {
			return MathUtil.fold(value);
		} else {
			return value.subtract(this.minValue);
		}
	}

	@Override
	protected BigInteger abstractReconvert(BigInteger value) {
		if (this.minValue == null) {
			return MathUtil.unfold(value);
		} else {
			return value.add(this.minValue);
		}
	}

	@Override
	protected String defaultToStringContent() {
		return this.minValue.toString();
	}

}
