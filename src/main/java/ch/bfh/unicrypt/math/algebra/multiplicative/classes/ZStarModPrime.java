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
package ch.bfh.unicrypt.math.algebra.multiplicative.classes;

import ch.bfh.unicrypt.helper.factorization.Prime;
import ch.bfh.unicrypt.helper.random.RandomByteSequence;
import ch.bfh.unicrypt.helper.random.deterministic.DeterministicRandomByteSequence;
import ch.bfh.unicrypt.helper.sequence.Sequence;
import ch.bfh.unicrypt.helper.sequence.functions.Predicate;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeCyclicGroup;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author R. Haenni
 */
public class ZStarModPrime
	   extends ZStarMod
	   implements MultiplicativeCyclicGroup<BigInteger> {

	private final static Map<BigInteger, ZStarModPrime> instances = new HashMap<>();
	private static final long serialVersionUID = 1L;

	protected ZStarModPrime(Prime modulus) {
		super(modulus);
	}

	@Override
	public ZStarModElement getDefaultGenerator() {
		throw new UnsupportedOperationException("Not supported yet.");
		// TODO: Implement method.
	}

	@Override
	public Sequence<ZStarModElement> getIndependentGenerators() {
		return this.getIndependentGenerators(DeterministicRandomByteSequence.getInstance());
	}

	@Override
	public Sequence<ZStarModElement> getIndependentGenerators(DeterministicRandomByteSequence randomByteSequence) {
		if (randomByteSequence == null) {
			throw new IllegalArgumentException();
		}
		return this.defaultGetGenerators(randomByteSequence);
	}

	// see Handbook of Applied Cryptography, Algorithm 4.80 and Note 4.81
	protected Sequence<ZStarModElement> defaultGetGenerators(RandomByteSequence randomByteSequence) {
		return this.abstractGetRandomElements(randomByteSequence).filter(new Predicate<ZStarModElement>() {

			@Override
			public boolean test(ZStarModElement value) {
				return isGenerator(value);
			}

		});
	}

	@Override
	public boolean isGenerator(Element element) {
		throw new UnsupportedOperationException("Not supported yet.");
		// TODO: Implement method.
	}

	public static ZStarModPrime getInstance(final Prime modulus) {
		if (modulus == null) {
			throw new IllegalArgumentException();
		}
		ZStarModPrime instance = ZStarModPrime.instances.get(modulus.getValue());
		if (instance == null) {
			instance = new ZStarModPrime(modulus);
			ZStarModPrime.instances.put(modulus.getValue(), instance);
		}
		return instance;
	}

	public static ZStarModPrime getInstance(final long modulus) {
		return ZStarModPrime.getInstance(BigInteger.valueOf(modulus));
	}

	public static ZStarModPrime getInstance(final BigInteger modulus) {
		return ZStarModPrime.getInstance(Prime.getInstance(modulus));
	}

	public static ZStarModPrime getFirstInstance(int bitLength) {
		return ZStarModPrime.getInstance(Prime.getFirstInstance(bitLength));
	}

}
