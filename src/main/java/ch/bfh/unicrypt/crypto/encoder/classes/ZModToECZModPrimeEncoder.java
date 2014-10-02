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
import ch.bfh.unicrypt.crypto.encoder.interfaces.ProbabilisticEncoder;
import ch.bfh.unicrypt.math.MathUtil;
import ch.bfh.unicrypt.math.algebra.additive.classes.ECZModElement;
import ch.bfh.unicrypt.math.algebra.additive.classes.ECZModPrime;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.random.interfaces.RandomByteSequence;

import java.math.BigInteger;

public class ZModToECZModPrimeEncoder
	   extends AbstractEncoder<ZModPrime, ZModElement, ECZModPrime, ECZModElement>
	   implements ProbabilisticEncoder {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static final int SHIFT = 10;
	private final ECZModPrime ec;
	private final ZMod zMod;

	protected ZModToECZModPrimeEncoder(ZMod zMod,ECZModPrime ec) {
		this.ec = ec;
		this.zMod=zMod;
	}

	@Override
	protected Function abstractGetEncodingFunction() {
		return new ECEncodingFunction(this.zMod, this.ec);
	}

	@Override
	protected Function abstractGetDecodingFunction() {
		return new ECDecodingFunction(ec, this.zMod);
	}

	public static ZModToECZModPrimeEncoder getInstance(final ZMod zMod,final ECZModPrime ec) {
		if (ec == null || zMod==null) {
			throw new IllegalArgumentException();
		}
		return new ZModToECZModPrimeEncoder(zMod,ec);
	}

	static class ECEncodingFunction
		   extends AbstractFunction<ECEncodingFunction, ZMod, ZModElement, ECZModPrime, ECZModElement> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		protected ECEncodingFunction(ZMod domain, ECZModPrime coDomain) {
			super(domain, coDomain);
		}

		@Override
		protected ECZModElement abstractApply(ZModElement element, RandomByteSequence randomByteSequence) {
			boolean firstOption=true;
			ZModPrime zModPrime=this.getCoDomain().getFiniteField();
			ECZModPrime ecPrime = this.getCoDomain();

			BigInteger e = element.getValue();
			e = e.shiftLeft(SHIFT);

			if (!zModPrime.contains(e)) {
				throw new ProbabilisticEncodingException(e + " can not be encoded");
			}

			ZModElement x = zModPrime.getElement(e);
			final ZModElement ONE = zModPrime.getOneElement();

			int count = 0;
			while (!ecPrime.contains(x)) {
				if (count >= (1 << SHIFT)) {
					throw new ProbabilisticEncodingException(e + " can not be encoded");
				}
				x = x.add(ONE);
				count++;
			}
			
			if(firstOption){
				ZModElement y1 = x.power(3).add(ecPrime.getA().multiply(x)).add(ecPrime.getB());
				ZModElement y = zModPrime.getElement(MathUtil.sqrtModPrime(y1.getValue(), zModPrime.getModulus()));
				return ecPrime.getElement(x, y);
			}
			
			e=element.invert().getValue().shiftLeft(SHIFT);
			
			if (!zModPrime.contains(e)) {
				throw new ProbabilisticEncodingException(e + " can not be encoded");
			}

			x = zModPrime.getElement(e);
			

			count = 0;
			while (!ecPrime.contains(x)) {
				if (count >= (1 << SHIFT)) {
					throw new ProbabilisticEncodingException(e + " can not be encoded");
				}
				x = x.add(ONE);
				count++;
			}
			
			
			ZModElement y1 = x.power(3).add(ecPrime.getA().multiply(x)).add(ecPrime.getB());
			ZModElement y = zModPrime.getElement(MathUtil.sqrtModPrime(y1.getValue(), zModPrime.getModulus()));
			y=y.invert();
			return ecPrime.getElement(x, y);
			
			
		}
					

	}

	static class ECDecodingFunction
		   extends AbstractFunction<ECDecodingFunction, ECZModPrime, ECZModElement, ZMod, ZModElement> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		protected ECDecodingFunction(ECZModPrime domain, ZMod coDomain) {
			super(domain, coDomain);
		}

		@Override
		protected ZModElement abstractApply(ECZModElement element, RandomByteSequence randomByteSequence) {
			ECZModPrime ecPrime = this.getDomain();
			ZModPrime zModPrime=this.getDomain().getFiniteField();
			ZModElement x=(ZModElement) element.getX();
			ZModElement y=(ZModElement) element.getY();
			
			ZModElement y1 = x.power(3).add(ecPrime.getA().multiply(x)).add(ecPrime.getB());
			ZModElement yEnc = zModPrime.getElement(MathUtil.sqrtModPrime(y1.getValue(), zModPrime.getModulus()));
			
			if(y.isEquivalent(yEnc)){
				BigInteger x1 = element.getX().getValue();
				x1 = x1.shiftRight(SHIFT);
				return this.getCoDomain().getElement(x1);
			}
			else{
				BigInteger x1 = element.getX().invert().getValue();
				x1 = x1.shiftRight(SHIFT);
				return this.getCoDomain().getElement(x1);
			}
		}

	}

}
