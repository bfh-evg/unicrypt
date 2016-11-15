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
 * <p>
 * @author R. Haenni
 * @see "B. King, 'Mapping an arbitrary message to an elliptic curve when defined over GF(2^n)', 2009, Algorithm 1"
 */
public class ZModToECZModPrime
	   extends AbstractEncoder<ZMod, ZModElement, ECZModPrime, ECZModElement>
	   implements ProbabilisticEncoder {

	private static final long serialVersionUID = 1L;

	private static final int DEFAULT_TRIALS = 64;

	private final ECZModPrime ec;
	private final int trials;
	private final ZMod domain;

	protected ZModToECZModPrime(ECZModPrime ec, int trials) {
		this.ec = ec;
		this.trials = trials;
		this.domain = ZMod.getInstance(ec.getFiniteField().getOrder().divide(BigInteger.valueOf(trials)));
	}

	public static ZModToECZModPrime getInstance(ECZModPrime ec) {
		return ZModToECZModPrime.getInstance(ec, DEFAULT_TRIALS);
	}

	public static ZModToECZModPrime getInstance(ECZModPrime ec, int trials) {
		if (ec == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER);
		}
		if (trials < 1) {
			throw new UniCryptRuntimeException(ErrorCode.INCOMPATIBLE_ARGUMENTS, trials);
		}
		// compute min to be compatible with small test curves
		trials = BigInteger.valueOf(trials).min(ec.getFiniteField().getOrder()).intValue();
		return new ZModToECZModPrime(ec, trials);
	}

	@Override
	protected Function abstractGetEncodingFunction() {
		return new EncodingFunction(this.domain, this.ec);
	}

	@Override
	protected Function abstractGetDecodingFunction() {
		return new DecodingFunction(this.ec, this.domain);
	}

	private class EncodingFunction
		   extends AbstractFunction<EncodingFunction, ZMod, ZModElement, ECZModPrime, ECZModElement> {

		protected EncodingFunction(ZMod domain, ECZModPrime coDomain) {
			super(domain, coDomain);
		}

		@Override
		protected ECZModElement abstractApply(ZModElement element, RandomByteSequence randomByteSequence) {
			final ZModPrime field = ec.getFiniteField();
			ZModElement candidate = field.getElement(element.getValue().multiply(BigInteger.valueOf(trials)));
			for (int i = 0; i < trials; i++) {
				if (ec.contains(candidate)) {
					return ec.getElement(candidate);
				}
				candidate = field.getElement(candidate.getValue().add(MathUtil.ONE));
			}
			throw new UniCryptRuntimeException(ErrorCode.PROBABILISTIC_ENCODING_FAILURE, ec, element);
		}

	}

	private class DecodingFunction
		   extends AbstractFunction<DecodingFunction, ECZModPrime, ECZModElement, ZMod, ZModElement> {

		protected DecodingFunction(ECZModPrime domain, ZMod coDomain) {
			super(domain, coDomain);
		}

		@Override
		protected ZModElement abstractApply(ECZModElement element, RandomByteSequence randomByteSequence) {
			BigInteger x = element.getX().getValue();
			return domain.getElement(x.divide(BigInteger.valueOf(trials)));
		}

	}

}
