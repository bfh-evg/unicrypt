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
package ch.bfh.unicrypt.helper.converter.classes.string;

import ch.bfh.unicrypt.helper.converter.abstracts.AbstractStringConverter;
import java.math.BigInteger;

/**
 * Instances of this class convert {@code BigInteger} values into strings. The radix of the string representation can be
 * chosen freely between {@code 2} and {@code 36}. The 36 available digits are the characters
 * {@code '0',...,'9','A',...,'Z'} or {@code '0',...,'9','a',...,'z'}, depending on the chosen mode of operation
 * (upper-case or lower-case letters). Negative numbers are preceeded by the minus sign {@code '-'}. The default
 * conversion returns the decimal representation with digits {@code '0',...,'9'}.
 * <p>
 * @author R. Haenni
 * @version 2.0
 * @see Character#MIN_RADIX
 * @see Character#MAX_RADIX
 */
public class BigIntegerToString
	   extends AbstractStringConverter<BigInteger> {

	private static final long serialVersionUID = 1L;

	// a value between 2 and 36
	private final int radix;

	// flag indicating whether lowe- or upper-case letters are used
	private final boolean upperCase;

	// used to check strings before reconverting
	private final String regExp;

	protected BigIntegerToString(int radix, boolean upperCase) {
		super(BigInteger.class);
		this.radix = radix;
		this.upperCase = upperCase;
		// compute regexp
		String range;
		if (radix <= 10) {
			range = "0-" + (radix - 1);
		} else {
			if (radix == 11) {
				if (upperCase) {
					range = "0-9A";
				} else {
					range = "0-9a";
				}
			} else {
				range = "0-9";
				if (upperCase) {
					range = range + "A-" + (char) ('A' + radix - 11);
				} else {
					range = range + "a-" + (char) ('a' + radix - 11);
				}
			}
		}
		this.regExp = "^-?[" + range + "]+$";  // this expression is not perfect, e.g. "0036" or "-0" are accepted
	}

	/**
	 * Returns the default {@code BigIntegerToString} converter using ordinary decimal representation.
	 * <p>
	 * @return The default converter
	 */
	public static BigIntegerToString getInstance() {
		return BigIntegerToString.getInstance(10, true);
	}

	/**
	 * Returns a new {@code BigIntegerToString} converter for a given {@code radix=2,...,36}. For {@code radix>10},
	 * lower-case letters {@code 'a',...,'z'} are used as additional digits.
	 * <p>
	 * @param radix The given radix
	 * @return The new converter
	 */
	public static BigIntegerToString getInstance(int radix) {
		return BigIntegerToString.getInstance(radix, true);
	}

	/**
	 * Returns a new {@code BigIntegerToString} converter for a given {@code radix=2,...,36}. For {@code radix>10}, the
	 * parameter {@code upperCase} indicates whether upper-case letters {@code 'A',...,'Z'} or lower-case letters
	 * {@code 'a',...,'z'} are used as additional digits.
	 * <p>
	 * @param radix     The given radix
	 * @param upperCase The flag indicating whether upper-case or lower-case letters are used
	 * @return The new converter
	 */
	public static BigIntegerToString getInstance(int radix, boolean upperCase) {
		if (radix < Character.MIN_RADIX || radix > Character.MAX_RADIX) {
			throw new IllegalArgumentException();
		}
		return new BigIntegerToString(radix, upperCase);
	}

	/**
	 * This is a convenience method to allow inputs of type {@code long}.
	 * <p>
	 * @param value The given value
	 * @return The resulting byte array
	 */
	public String convert(long value) {
		return this.convert(BigInteger.valueOf(value));
	}

	@Override
	protected boolean defaultIsValidOutput(String string) {
		return string.matches(this.regExp);
	}

	@Override
	protected String abstractConvert(BigInteger value
	) {
		String result = value.toString(this.radix);
		if (this.upperCase) {
			return result.toUpperCase();
		}
		return result;
	}

	@Override
	protected BigInteger abstractReconvert(String string) {
		return new BigInteger(string, this.radix);
	}

	@Override
	protected String defaultToStringContent() {
		String c = this.upperCase ? ",uppercase" : ",lowercase";
		return "" + this.radix + c;
	}

}
