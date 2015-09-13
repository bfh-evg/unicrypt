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
package ch.bfh.unicrypt.crypto.encoder.classes;

import ch.bfh.unicrypt.crypto.encoder.abstracts.AbstractEncoder;
import ch.bfh.unicrypt.crypto.encoder.interfaces.ProbabilisticEncoder;
import ch.bfh.unicrypt.exception.ErrorCode;
import ch.bfh.unicrypt.exception.UniCryptRuntimeException;
import ch.bfh.unicrypt.helper.math.MathUtil;
import ch.bfh.unicrypt.helper.random.RandomByteSequence;
import ch.bfh.unicrypt.math.algebra.additive.classes.ECZModElement;
import ch.bfh.unicrypt.math.algebra.additive.classes.ECZModPrime;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import java.math.BigInteger;

/**
 *
 * @author Christian Lutz
 * <p>
 */
public class ZModPrimeToECZModPrime
	   extends AbstractEncoder<ZModPrime, ZModElement, ECZModPrime, ECZModElement>
	   implements ProbabilisticEncoder {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	protected int shift;
	private final ECZModPrime ec;
	private final ZModPrime zModPrime;

	protected ZModPrimeToECZModPrime(ECZModPrime ec, int shift) {
		this.ec = ec;
		this.zModPrime = ec.getFiniteField();
		this.shift = shift;
	}

	@Override
	protected Function abstractGetEncodingFunction() {
		return new ECEncodingFunction(this.zModPrime, this.ec, this.shift);
	}

	@Override
	protected Function abstractGetDecodingFunction() {
		return new ECDecodingFunction(ec, this.zModPrime, this.shift);
	}

	public static ZModPrimeToECZModPrime getInstance(final ECZModPrime ec, int shift) {
		if (ec == null) {
			throw new IllegalArgumentException();
		}
		return new ZModPrimeToECZModPrime(ec, shift);
	}

	static class ECEncodingFunction
		   extends AbstractFunction<ECEncodingFunction, ZModPrime, ZModElement, ECZModPrime, ECZModElement> {

		/**
		 *
		 */
		private static final long serialVersionUID = 1L;
		private int shift;

		protected ECEncodingFunction(ZModPrime domain, ECZModPrime coDomain, int shift) {
			super(domain, coDomain);
			this.shift = shift;
		}

		@Override
		protected ECZModElement abstractApply(ZModElement element, RandomByteSequence randomByteSequence) {
			boolean firstOption = true;
			ZModPrime zModPrime = this.getCoDomain().getFiniteField();
			ECZModPrime ecPrime = this.getCoDomain();

			int msgSpace = zModPrime.getOrder().toString(2).length();
			int msgBitLength = element.getValue().toString(2).length() + 2;

			BigInteger c;

			if (msgSpace / 3 > msgBitLength) {
				c = MathUtil.ZERO;
				this.shift = msgSpace / 3 * 2;
			} else if (msgSpace / 2 > msgBitLength) {
				c = MathUtil.ONE;
				this.shift = msgSpace / 2;
			} else if (msgSpace / 3 * 2 > msgBitLength) {
				c = new BigInteger("2");
				this.shift = msgSpace / 3;
			} else {
				c = new BigInteger("3");
			}

			BigInteger e = element.getValue();
			e = e.shiftLeft(this.shift + 2);
			e = e.add(c);

			if (!zModPrime.contains(e)) {
				throw new UniCryptRuntimeException(ErrorCode.PROBABILISTIC_ENCODING_FAILURE, element, e);
			}

			ZModElement x = zModPrime.getElement(e);
			ZModElement stepp = zModPrime.getElement(4);

			int count = 0;
			while (!ecPrime.contains(x)) {
				if (count >= (1 << this.shift)) {
					firstOption = false;
				}
				x = x.add(stepp);
				count++;
			}

			if (firstOption) {
				ECZModElement[] y = ecPrime.getY(x);
				ZModElement y1 = y[0].getY();
				ZModElement y2 = y[1].getY();
				if (isBigger(y1, y2)) {
					return y[0];
				}
				return y[1];
			}

			element = element.invert();
			msgBitLength = element.getValue().toString(2).length();

			if (msgSpace / 3 > msgBitLength) {
				c = MathUtil.ZERO;
				this.shift = msgSpace / 3 * 2;
			} else if (msgSpace / 2 > msgBitLength) {
				c = MathUtil.ONE;
				this.shift = msgSpace / 2;
			} else if (msgSpace / 3 * 2 > msgBitLength) {
				c = new BigInteger("2");
				this.shift = msgSpace / 3;
			} else {
				c = new BigInteger("3");
			}

			e = element.getValue();
			e = e.shiftLeft(this.shift + 2);
			e = e.add(c);

			if (!zModPrime.contains(e)) {
				throw new UniCryptRuntimeException(ErrorCode.PROBABILISTIC_ENCODING_FAILURE, element, e);
			}

			x = zModPrime.getElement(e);

			count = 0;
			while (!ecPrime.contains(x)) {
				if (count >= (1 << this.shift)) {
					throw new UniCryptRuntimeException(ErrorCode.PROBABILISTIC_ENCODING_FAILURE, element, e, x);
				}
				x = x.add(stepp);
				count++;
			}

			ECZModElement[] y = ecPrime.getY(x);
			ZModElement y1 = y[0].getY();
			ZModElement y2 = y[1].getY();

			if (isBigger(y1, y2)) {
				return y[1];
			}
			return y[0];

		}

	}

	static class ECDecodingFunction
		   extends AbstractFunction<ECDecodingFunction, ECZModPrime, ECZModElement, ZMod, ZModElement> {

		/**
		 *
		 */
		private static final long serialVersionUID = 1L;
		private int shift;

		protected ECDecodingFunction(ECZModPrime domain, ZMod coDomain, int shift) {
			super(domain, coDomain);
			this.shift = shift;
		}

		@Override
		protected ZModElement abstractApply(ECZModElement element, RandomByteSequence randomByteSequence) {
			ECZModPrime ecPrime = this.getDomain();
			ZModPrime zModPrime = this.getDomain().getFiniteField();
			int msgSpace = zModPrime.getOrder().toString(2).length();

			ZModElement x = element.getX();
			ZModElement y = element.getY();
			ZModElement y1 = element.invert().getY();

			BigInteger x1 = x.convertToBigInteger();

			BigInteger c = x1.subtract(x1.shiftRight(2).shiftLeft(2));

			if (c.equals(MathUtil.ZERO)) {
				this.shift = msgSpace / 3 * 2;
			} else if (c.equals(MathUtil.ONE)) {
				this.shift = msgSpace / 2;
			} else if (c.equals(new BigInteger("2"))) {
				this.shift = msgSpace / 3;
			}

			x1 = x1.shiftRight(this.shift + 2);

			if (y.isEquivalent(getBiggerY(y1, y))) {
				return zModPrime.getElement(x1);

			} else {
				return zModPrime.getElement(x1).invert();
			}

		}

	}

	/**
	 * Compares the two polynomial elements and return the element with the most significant coefficient not in common.
	 * <p>
	 * @param y1
	 * @param y2
	 * @return
	 */
	public static ZModElement getBiggerY(ZModElement y1, ZModElement y2) {
		int c = y1.getValue().compareTo(y2.getValue());

		if (c == 1) {
			return y1;
		} else {
			return y2;
		}
	}

	/**
	 * Compares y1 and y2 and returns if y1 is bigger then y2 -> getBiggerY
	 * <p>
	 * @param y1
	 * @param y2
	 * @return
	 */
	public static boolean isBigger(ZModElement y1, ZModElement y2) {
		return y1.isEquivalent(getBiggerY(y1, y2));
	}

}
