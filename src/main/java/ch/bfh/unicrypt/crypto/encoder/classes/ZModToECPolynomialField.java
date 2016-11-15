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
package ch.bfh.unicrypt.crypto.encoder.classes;

import ch.bfh.unicrypt.ErrorCode;
import ch.bfh.unicrypt.UniCryptRuntimeException;
import ch.bfh.unicrypt.crypto.encoder.abstracts.AbstractEncoder;
import ch.bfh.unicrypt.crypto.encoder.interfaces.ProbabilisticEncoder;
import ch.bfh.unicrypt.helper.array.classes.BitArray;
import ch.bfh.unicrypt.helper.converter.classes.biginteger.BitArrayToBigInteger;
import ch.bfh.unicrypt.helper.converter.interfaces.Converter;
import ch.bfh.unicrypt.helper.math.MathUtil;
import ch.bfh.unicrypt.helper.random.RandomByteSequence;
import ch.bfh.unicrypt.math.algebra.additive.classes.ECPolynomialElement;
import ch.bfh.unicrypt.math.algebra.additive.classes.ECPolynomialField;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialField;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import java.math.BigInteger;

public class ZModToECPolynomialField
	   extends AbstractEncoder<ZMod, ZModElement, ECPolynomialField, ECPolynomialElement>
	   implements ProbabilisticEncoder {

	private static final long serialVersionUID = 1L;

	private static final int DEFAULT_TRIALS = 64;

	private final ECPolynomialField ec;
	private final int trials;
	private final ZMod domain;

	protected ZModToECPolynomialField(ECPolynomialField ec, int trials) {
		this.ec = ec;
		this.trials = trials;
		this.domain = ZMod.getInstance(ec.getFiniteField().getOrder().divide(BigInteger.valueOf(trials)));
	}

	public static ZModToECPolynomialField getInstance(ECPolynomialField ec) {
		return ZModToECPolynomialField.getInstance(ec, DEFAULT_TRIALS);
	}

	public static ZModToECPolynomialField getInstance(ECPolynomialField ec, int trials) {
		if (ec == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER);
		}
		if (trials < 1) {
			throw new UniCryptRuntimeException(ErrorCode.INCOMPATIBLE_ARGUMENTS, trials);
		}
		// compute min to be compatible with small test curves
		trials = BigInteger.valueOf(trials).min(ec.getFiniteField().getOrder()).intValue();
		return new ZModToECPolynomialField(ec, trials);
	}

	@Override
	protected Function abstractGetEncodingFunction() {
		return new EncodingFunction(this.domain, this.ec);
	}

	@Override
	protected Function abstractGetDecodingFunction() {
		return new DecodingFunction(this.ec, this.domain);
	}

//	public static void main(String[] args) {
//		ECPolynomialField ec = ECPolynomialField.getInstance(ECPolynomialFieldParameters.TEST11);
//		ZModToECPolynomialField encoder = ZModToECPolynomialField.getInstance(ec, 15);
//		for (PolynomialElement e1 : ec.getFiniteField().getElements()) {
//			if (ec.contains(e1)) {
//				System.out.println(" " + e1);
//			}
//		}
//		for (ZModElement element : encoder.getDomain().getElements().limit(100)) {
//			System.out.println(encoder.encode(element));
//		}
//	}
	private class EncodingFunction
		   extends AbstractFunction<EncodingFunction, ZMod, ZModElement, ECPolynomialField, ECPolynomialElement> {

		protected EncodingFunction(ZMod domain, ECPolynomialField coDomain) {
			super(domain, coDomain);
		}

		@Override
		protected ECPolynomialElement abstractApply(ZModElement element, RandomByteSequence randomByteSequence) {
			Converter<BitArray, BigInteger> converter = BitArrayToBigInteger.getInstance();
			final PolynomialField field = ec.getFiniteField();
			BigInteger candidate = element.getValue().multiply(BigInteger.valueOf(trials));
			for (int i = 0; i < trials; i++) {
				PolynomialElement candidateElement = field.getElement(converter.reconvert(candidate));
				if (ec.contains(candidateElement)) {
					return ec.getElement(candidateElement);
				}
				candidate = candidate.add(MathUtil.ONE);
			}
			throw new UniCryptRuntimeException(ErrorCode.PROBABILISTIC_ENCODING_FAILURE, ec, element);
		}

//		@Override
//		protected ECPolynomialElement abstractApply(ZModElement element, RandomByteSequence randomByteSequence) {
//			boolean firstOption = true;
//
//			ZModPrime zModPrime = this.getDomain();
//			ECPolynomialField ecPolynimial = this.getCoDomain();
//
//			int msgSpace = zModPrime.getOrder().toString(2).length();
//			int msgBitLength = element.getValue().toString(2).length() + 2;
//
//			BigInteger c;
//
//			if (msgSpace / 3 > msgBitLength) {
//				c = MathUtil.ZERO;
//				this.shift = msgSpace / 3 * 2;
//			} else if (msgSpace / 2 > msgBitLength) {
//				c = MathUtil.ONE;
//				this.shift = msgSpace / 2;
//			} else if (msgSpace / 3 * 2 > msgBitLength) {
//				c = new BigInteger("2");
//				this.shift = msgSpace / 3;
//			} else {
//				c = new BigInteger("3");
//			}
//
//			BigInteger e = element.getValue();
//			e = e.shiftLeft(this.shift + 2);
//			e = e.add(c);
//
//			ZModElement zModElement = this.getDomain().getElement(e);
//
//			ZModToBinaryPolynomialField enc
//				   = ZModToBinaryPolynomialField.getInstance(zModPrime, ecPolynimial.getFiniteField());
//			PolynomialElement x = enc.encode(zModElement);
//			ZModElement stepp = zModPrime.getElement(4);
//
//			int count = 0;
//			while (!ecPolynimial.contains(x)) {
//				if (count >= (1 << this.shift)) {
//					firstOption = false;
//				}
//
//				zModElement = zModElement.add(stepp);
//
//				x = enc.encode(zModElement);
//				count++;
//
//			}
//
//			if (firstOption) {
//				ECPolynomialElement e1 = ecPolynimial.getElement(x);
//				ECPolynomialElement e2 = e1.invert();
//				if (isBigger(e1.getY(), e2.getY())) {
//					return e1;
//				}
//				return e2;
//			} else {
//
//				zModElement = element.invert();
//				msgBitLength = zModElement.getValue().toString(2).length();
//
//				if (msgSpace / 3 > msgBitLength) {
//					c = MathUtil.ZERO;
//					this.shift = msgSpace / 3 * 2;
//				} else if (msgSpace / 2 > msgBitLength) {
//					c = MathUtil.ONE;
//					this.shift = msgSpace / 2;
//				} else if (msgSpace / 3 * 2 > msgBitLength) {
//					c = new BigInteger("2");
//					this.shift = msgSpace / 3;
//				} else {
//					c = new BigInteger("3");
//				}
//
//				e = zModElement.getValue();
//				e = e.shiftLeft(this.shift + 2);
//				e = e.add(c);
//
//				x = enc.encode(zModElement);
//
//				count = 0;
//				while (!ecPolynimial.contains(x)) {
//					if (count >= (1 << this.shift)) {
//						throw new UniCryptRuntimeException(ErrorCode.PROBABILISTIC_ENCODING_FAILURE, element, e);
//					}
//
//					zModElement = zModElement.add(stepp);
//					x = enc.encode(zModElement);
//					count++;
//
//				}
//
//				ECPolynomialElement e1 = ecPolynimial.getElement(x);
//				ECPolynomialElement e2 = e1.invert();
//				if (isBigger(e1.getY(), e2.getY())) {
//					return e2;
//				}
//				return e1;
//			}
//
//		}
	}

	private static class DecodingFunction
		   extends AbstractFunction<DecodingFunction, ECPolynomialField, ECPolynomialElement, ZMod, ZModElement> {

		public DecodingFunction(ECPolynomialField domain, ZMod coDomain) {
			super(domain, coDomain);
		}

		@Override
		protected ZModElement abstractApply(ECPolynomialElement element, RandomByteSequence randomByteSequence) {
			throw new UnsupportedOperationException("Not supported yet.");
		}

//		@Override
//		protected ZModElement abstractApply(ECPolynomialElement element, RandomByteSequence randomByteSequence) {
//
//			ZModPrime zModPrime = this.getCoDomain();
//			ECPolynomialField ec = this.getDomain();
//			int msgSpace = zModPrime.getOrder().toString(2).length();
//			ZModToBinaryPolynomialField enc
//				   = ZModToBinaryPolynomialField.getInstance(zModPrime, ec.getFiniteField());
//
//			PolynomialElement x = element.getX();
//			PolynomialElement y = element.getY();
//			PolynomialElement y1 = element.invert().getY();
//
//			BigInteger x1 = enc.decode(x).convertToBigInteger();
//
//			BigInteger c = x1.subtract(x1.shiftRight(2).shiftLeft(2));
//
//			if (c.equals(MathUtil.ZERO)) {
//				this.shift = msgSpace / 3 * 2;
//			} else if (c.equals(MathUtil.ONE)) {
//				this.shift = msgSpace / 2;
//			} else if (c.equals(new BigInteger("2"))) {
//				this.shift = msgSpace / 3;
//			}
//
//			x1 = x1.shiftRight(this.shift + 2);
//
//			if (y.isEquivalent(getBiggerY(y, y1))) {
//				return zModPrime.getElement(x1);
//			} else {
//				return zModPrime.getElement(x1).invert();
//			}
//
//		}
	}

//	// Compares the two polynomial elements and return the element with the most significant coefficient not in common.
//	private static PolynomialElement getBiggerY(PolynomialElement y1, PolynomialElement y2) {
//		int degree = y1.add(y2).getValue().getDegree();
//		BigInteger y1Coeff = y1.getValue().getCoefficient(degree).convertToBigInteger();
//		BigInteger y2Coeff = y2.getValue().getCoefficient(degree).convertToBigInteger();
//
//		if (y1Coeff.compareTo(y2Coeff) > 0) {
//			return y1;
//		}
//		return y2;
//	}
//
//	// Compares y1 and y2 and returns if y1 is bigger then y2 -> getBiggerY
//	private static boolean isBigger(PolynomialElement y1, PolynomialElement y2) {
//		return y1.isEquivalent(getBiggerY(y1, y2));
//	}
}
