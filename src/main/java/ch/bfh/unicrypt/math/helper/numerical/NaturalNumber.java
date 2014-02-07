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
package ch.bfh.unicrypt.math.helper.numerical;

import java.math.BigInteger;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class NaturalNumber
	   extends WholeNumber {

	public NaturalNumber(BigInteger bigInteger) {
		super(bigInteger);
	}

	public NaturalNumber add(NaturalNumber other) {
		return (NaturalNumber) super.add(other);
	}

	public NaturalNumber multiply(NaturalNumber other) {
		return (NaturalNumber) super.multiply(other);
	}

	@Override
	public NaturalNumber power(int exponent) {
		return (NaturalNumber) super.power(exponent);
	}

	@Override
	public NaturalNumber power(BigInteger exponent) {
		return (NaturalNumber) super.power(exponent);
	}

	@Override
	public NaturalNumber power(NaturalNumber exponent) {
		return (NaturalNumber) super.power(exponent);
	}

	@Override
	public NaturalNumber square() {
		return (NaturalNumber) super.square();
	}

	public static NaturalNumber getInstance(int integer) {
		return NaturalNumber.getInstance(BigInteger.valueOf(integer));
	}

	public static NaturalNumber getInstance(BigInteger integer) {
		if (integer == null || integer.signum() < 0) {
			throw new IllegalArgumentException();
		}
		return new NaturalNumber(integer);
	}

}
