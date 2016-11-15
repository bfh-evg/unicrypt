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
import ch.bfh.unicrypt.helper.random.RandomByteSequence;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarMod;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModElement;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.ZStarModElement;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.ZStarModPrime;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import java.math.BigInteger;

public class ZStarModPrimeToGStarModSafePrime
	   extends AbstractEncoder<ZStarModPrime, ZStarModElement, GStarModSafePrime, GStarModElement> {

	private final GStarModSafePrime gStarModSafePrime;

	protected ZStarModPrimeToGStarModSafePrime(GStarModSafePrime gStarModSafePrime) {
		this.gStarModSafePrime = gStarModSafePrime;
	}

	public static ZStarModPrimeToGStarModSafePrime getInstance(GStarModSafePrime gStarModSafePrime) {
		if (gStarModSafePrime == null) {
			throw new IllegalArgumentException();
		}
		return new ZStarModPrimeToGStarModSafePrime(gStarModSafePrime);
	}

	public GStarModSafePrime getGStarModSafePrime() {
		return this.gStarModSafePrime;
	}

	@Override
	protected Function abstractGetEncodingFunction() {
		return new EncodingFunction(this.gStarModSafePrime.getZStarModOrder(), this.gStarModSafePrime);
	}

	@Override
	protected Function abstractGetDecodingFunction() {
		return new DecodingFunction(this.gStarModSafePrime, this.gStarModSafePrime.getZStarModOrder());
	}

	private static class EncodingFunction
		   extends
		   AbstractFunction<EncodingFunction, ZStarModPrime, ZStarModElement, GStarModSafePrime, GStarModElement> {

		protected EncodingFunction(ZStarModPrime domain, GStarMod coDomain) {
			super(domain, coDomain);
		}

		@Override
		protected GStarModElement abstractApply(ZStarModElement element, RandomByteSequence randomByteSequence) {
			BigInteger value = element.getValue();
			GStarModSafePrime coDomain = this.getCoDomain();
			if (coDomain.contains(value)) {
				return coDomain.getElement(value);
			}
			value = coDomain.getModulus().subtract(value);
			if (coDomain.contains(value)) {
				return coDomain.getElement(value);
			}
			throw new UniCryptRuntimeException(ErrorCode.ELEMENT_CONVERSION_FAILURE, element);
		}

	}

	private static class DecodingFunction
		   extends
		   AbstractFunction<DecodingFunction, GStarModSafePrime, GStarModElement, ZStarModPrime, ZStarModElement> {

		protected DecodingFunction(GStarMod domain, ZStarModPrime coDomain) {
			super(domain, coDomain);
		}

		@Override
		protected ZStarModElement abstractApply(GStarModElement element, RandomByteSequence randomByteSequence) {
			BigInteger value = element.getValue();
			ZStarModPrime coDomain = this.getCoDomain();
			if (coDomain.contains(value)) {
				return coDomain.getElement(value);
			}
			return coDomain.getElement(this.getDomain().getModulus().subtract(value));
		}

	}

}
