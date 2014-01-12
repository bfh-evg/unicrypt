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
package ch.bfh.unicrypt.crypto.random.abstracts;

import ch.bfh.unicrypt.crypto.random.interfaces.RandomGenerator;
import ch.bfh.unicrypt.math.helper.UniCrypt;
import ch.bfh.unicrypt.math.utility.MathUtil;
import java.math.BigInteger;

/**
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 1.0
 */
public abstract class AbstractRandomGenerator
	   extends UniCrypt
	   implements RandomGenerator {

	@Override
	public final boolean nextBoolean() {
		return this.abstractNextBoolean();
	}

	@Override
	public final byte nextByte() {
		return this.nextBytes(1)[0];
	}

	@Override
	public final byte[] nextBytes(int length) {
		if (length < 0) {
			throw new IllegalArgumentException();
		}
		return this.abstractNextBytes(length);
	}

	@Override
	public final int nextInteger() {
		return this.nextInteger(Integer.MIN_VALUE, Integer.MAX_VALUE);
	}

	@Override
	public final int nextInteger(int maxValue) {
		if (maxValue < 0) {
			throw new IllegalArgumentException();
		}
		return this.abstractNextInteger(maxValue);
	}

	@Override
	public final int nextInteger(int minValue, int maxValue) {
		return this.nextInteger(maxValue - minValue) + minValue;
	}

	@Override
	public final BigInteger nextBigInteger(int bitLength) {
		if (bitLength < 0) {
			throw new IllegalArgumentException();
		}
		if (bitLength == 0) {
			return BigInteger.ZERO;
		}
		return this.abstractNextBigInteger(bitLength);
	}

	@Override
	public final BigInteger nextBigInteger(BigInteger maxValue) {
		if (maxValue == null || maxValue.signum() < 0) {
			throw new IllegalArgumentException();
		}
		return this.abstractNextBigInteger(maxValue);
	}

	@Override
	public final BigInteger nextBigInteger(BigInteger minValue, BigInteger maxValue) {
		if (minValue == null || maxValue == null) {
			throw new IllegalArgumentException();
		}
		return this.nextBigInteger(maxValue.subtract(minValue)).add(minValue);
	}

	@Override
	public final BigInteger nextPrime(int bitLength) {
		if (bitLength < 2) {
			throw new IllegalArgumentException();
		}
		return this.abstractNextPrime(bitLength);
	}

	@Override
	public final BigInteger nextSavePrime(int bitLength) {
		BigInteger prime;
		BigInteger savePrime;
		do {
			prime = this.nextPrime(bitLength - 1);
			savePrime = prime.shiftLeft(1).add(BigInteger.ONE);
		} while (!MathUtil.isPrime(savePrime));
		return savePrime;
	}

	@Override
	public final BigInteger[] nextPrimePair(int bitLength1, int bitLength2) {
		if (bitLength1 <= bitLength2 || bitLength2 < 2) {
			throw new IllegalArgumentException();
		}
		BigInteger k;
		BigInteger prime1, prime2;
		BigInteger minValue, maxValue;
		do {
			prime2 = this.nextPrime(bitLength2);
			minValue = BigInteger.ONE.shiftLeft(bitLength1 - 1);
			maxValue = BigInteger.ONE.shiftLeft(bitLength1).subtract(BigInteger.ONE);
			k = this.nextBigInteger(minValue.divide(prime2).add(BigInteger.ONE), maxValue.divide(prime2));
			prime1 = prime2.multiply(k).add(BigInteger.ONE);
		} while (!MathUtil.isPrime(prime1));
		return new BigInteger[]{prime1, prime2};
	}

	protected abstract boolean abstractNextBoolean();

	protected abstract byte[] abstractNextBytes(int length);

	protected abstract int abstractNextInteger(int maxValue);

	protected abstract BigInteger abstractNextBigInteger(int bitLength);

	protected abstract BigInteger abstractNextBigInteger(BigInteger maxValue);

	protected abstract BigInteger abstractNextPrime(int bitLength);

}
