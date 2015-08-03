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
import ch.bfh.unicrypt.crypto.encoder.exceptions.ProbabilisticEncodingException;
import ch.bfh.unicrypt.math.algebra.additive.classes.ECPolynomialField;
import ch.bfh.unicrypt.math.algebra.additive.classes.ECZModPrime;
import ch.bfh.unicrypt.math.algebra.additive.interfaces.EC;
import ch.bfh.unicrypt.math.algebra.additive.interfaces.ECElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialField;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.FiniteField;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.random.interfaces.RandomByteSequence;
import java.math.BigInteger;

public class ZModPrimeToEC
	   extends AbstractEncoder<ZModPrime, ZModElement, EC, ECElement> {

	private final ZModPrime zModPrime;
	private final EC ec;
	private AbstractEncoder encoder;
	private final int shift;

	protected ZModPrimeToEC(ZModPrime zModPrime, EC ec, int shift) {
		super();
		this.zModPrime = zModPrime;
		this.ec = ec;
		this.shift = shift;

		if (ECPolynomialField.class.isInstance(ec)) {
			ZMod zmod = ZMod.getInstance(ec.getOrder());
			encoder = ZModToBinaryPolynomialField.getInstance(zmod, (PolynomialField) ec.getFiniteField());
		} else if (ECZModPrime.class.isInstance(ec)) {
			encoder = GeneralEncoder.getInstance(ec.getFiniteField(), ec.getFiniteField());
		}
	}

	@Override
	protected Function abstractGetEncodingFunction() {
		return new EncodingFunction(this.zModPrime, this.ec, this.shift, encoder);
	}

	@Override
	protected Function abstractGetDecodingFunction() {
		return new DecodingFunction(ec, zModPrime, this.shift, encoder);
	}

	public static ZModPrimeToEC getInstance(ZModPrime zModPrime, EC ec, int shift) {
		return new ZModPrimeToEC(zModPrime, ec, shift);
	}

	static class EncodingFunction
		   extends AbstractFunction<EncodingFunction, ZModPrime, ZModElement, EC, ECElement> {

		private int shift;
		private AbstractEncoder<ZMod, ZModElement, FiniteField, DualisticElement> encoder;

		protected EncodingFunction(ZModPrime domain, EC coDomain, int shift,
			   AbstractEncoder<ZMod, ZModElement, FiniteField, DualisticElement> encoder) {
			super(domain, coDomain);
			this.shift = shift;
			this.encoder = encoder;
		}

		@Override
		protected ECElement abstractApply(ZModElement element,
			   RandomByteSequence randomByteSequence) {

			boolean firstOption = true;

			ZModPrime zModPrime = this.getDomain();
			EC ec = this.getCoDomain();

			int msgSpace = zModPrime.getOrder().toString(2).length();
			int msgBitLength = element.getValue().toString(2).length() + 2;

			BigInteger c;

			if (msgSpace / 3 > msgBitLength) {
				c = BigInteger.ZERO;
				this.shift = msgSpace / 3 * 2;
			} else if (msgSpace / 2 > msgBitLength) {
				c = BigInteger.ONE;
				this.shift = msgSpace / 2;
			} else if (msgSpace / 3 * 2 > msgBitLength) {
				c = new BigInteger("2");
				this.shift = msgSpace / 3;
			} else {
				c = new BigInteger("3");
			}

			BigInteger e = element.getValue();
			e = e.shiftLeft(shift + 2);
			e = e.add(c);

			ZModElement zModElement = this.getDomain().getElement(e);
			DualisticElement x = encoder.encode(zModElement);
			ZModElement stepp = zModPrime.getElement(4);

			int count = 0;
			while (!ec.contains(x)) {
				if (count >= (1 << shift)) {
					firstOption = false;
				}

				zModElement = zModElement.add(stepp);

				x = encoder.encode(zModElement);
				count++;

			}

			if (firstOption) {
				ECElement[] y = ec.getY(x);
				DualisticElement y1 = y[0].getY();
				DualisticElement y2 = y[1].getY();
				if (isBigger(y1, y2)) {
					return y[0];

				}
				return y[1];
			} else {

				zModElement = element.invert();
				msgBitLength = zModElement.getValue().toString(2).length();

				if (msgSpace / 3 > msgBitLength) {
					c = BigInteger.ZERO;
					this.shift = msgSpace / 3 * 2;
				} else if (msgSpace / 2 > msgBitLength) {
					c = BigInteger.ONE;
					this.shift = msgSpace / 2;
				} else if (msgSpace / 3 * 2 > msgBitLength) {
					c = new BigInteger("2");
					this.shift = msgSpace / 3;
				} else {
					c = new BigInteger("3");
				}

				e = zModElement.getValue();
				e = e.shiftLeft(shift + 2);
				e = e.add(c);

				x = encoder.encode(zModElement);

				count = 0;
				while (!ec.contains(x)) {
					if (count >= (1 << shift)) {
						throw new ProbabilisticEncodingException(e + " can not be encoded");
					}

					zModElement = zModElement.add(stepp);
					x = encoder.encode(zModElement);
					count++;

				}

				ECElement[] y = ec.getY(x);
				DualisticElement y1 = y[0].getY();
				DualisticElement y2 = y[1].getY();

				if (isBigger(y1, y2)) {
					return y[1];
				}
				return y[0];
			}

		}

	}

	static class DecodingFunction
		   extends AbstractFunction<DecodingFunction, EC, ECElement, ZModPrime, ZModElement> {

		private int shift;
		private AbstractEncoder<ZMod, ZModElement, FiniteField, DualisticElement> encoder;

		protected DecodingFunction(Set domain, Set coDomain, int shift,
			   AbstractEncoder<ZMod, ZModElement, FiniteField, DualisticElement> encoder) {
			super(domain, coDomain);
			this.shift = shift;
			this.encoder = encoder;

		}

		@Override
		protected ZModElement abstractApply(ECElement element,
			   RandomByteSequence randomByteSequence) {
			ZModPrime zModPrime = this.getCoDomain();
			EC ec = this.getDomain();
			int msgSpace = zModPrime.getOrder().toString(2).length();

			DualisticElement x = element.getX();
			DualisticElement y = element.getY();
			DualisticElement y1 = element.invert().getY();

			BigInteger x1 = encoder.decode(x).convertToBigInteger();

			BigInteger c = x1.subtract(x1.shiftRight(2).shiftLeft(2));

			if (c.equals(BigInteger.ZERO)) {
				this.shift = msgSpace / 3 * 2;
			} else if (c.equals(BigInteger.ONE)) {
				this.shift = msgSpace / 2;
			} else if (c.equals(new BigInteger("2"))) {
				this.shift = msgSpace / 3;
			}

			x1 = x1.shiftRight(shift + 2);

			if (y.isEquivalent(getBiggerY(y, y1))) {
				return zModPrime.getElement(x1);
			} else {
				return zModPrime.getElement(x1).invert();
			}
		}

	}

	public static DualisticElement getBiggerY(DualisticElement y1, DualisticElement y2) {
		return y1;
	}

	public static boolean isBigger(DualisticElement y1, DualisticElement y2) {
		return y1.isEquivalent(getBiggerY(y1, y2));
	}

}
