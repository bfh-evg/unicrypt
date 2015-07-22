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
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.random.interfaces.RandomByteSequence;

public class ZModToZmodPrime extends AbstractEncoder<ZMod, ZModElement, ZModPrime, ZModElement> {

	/**
	 *
	 */
	private static final long serialVersionUID = 191230672695611571L;
	private final ZMod zMod;
	private final ZModPrime zModPrime;

	private ZModToZmodPrime(ZMod zMod, ZModPrime zModPrime) {
		super();
		this.zMod=zMod;
		this.zModPrime=zModPrime;

	}

	@Override
	protected Function abstractGetEncodingFunction() {
		return new EncodingFunction(this.zMod, this.zModPrime);
	}

	@Override
	protected Function abstractGetDecodingFunction() {
		return new DecodingFunction(this.zModPrime, this.zMod);
	}

	public static ZModToZmodPrime getInstance(final ZMod zMod, final ZModPrime zModPrime) {
		if (zModPrime == null || zMod == null) {
			throw new IllegalArgumentException();
		}
		return new ZModToZmodPrime(zMod, zModPrime);
	}

	static class EncodingFunction extends AbstractFunction<EncodingFunction, ZMod, ZModElement, ZModPrime, ZModElement>{

		/**
		 *
		 */
		private static final long serialVersionUID = -8347692169255785315L;

		protected EncodingFunction(ZMod domain, ZModPrime coDomain) {
			super(domain, coDomain);
		}

		@Override
		protected ZModElement abstractApply(ZModElement element,
				RandomByteSequence randomByteSequence) {

			return this.getCoDomain().getElement(element.convertToBigInteger());
		}

	}

	static class DecodingFunction extends AbstractFunction<DecodingFunction, ZModPrime, ZModElement, ZMod, ZModElement>{



		protected DecodingFunction(ZModPrime domain, ZMod coDomain) {
			super(domain, coDomain);
		}

		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		@Override
		protected ZModElement abstractApply(ZModElement element,
				RandomByteSequence randomByteSequence) {
			return this.getCoDomain().getElement(element.getValue());
		}

	}

}
