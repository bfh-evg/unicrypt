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
package ch.bfh.unicrypt.random.classes;

import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.converter.classes.bytearray.BigIntegerToByteArray;
import ch.bfh.unicrypt.helper.converter.classes.bytearray.StringToByteArray;
import ch.bfh.unicrypt.helper.converter.interfaces.Converter;
import ch.bfh.unicrypt.helper.hash.HashAlgorithm;
import ch.bfh.unicrypt.random.abstracts.AbstractRandomOracle;
import ch.bfh.unicrypt.random.interfaces.RandomOracle;
import java.math.BigInteger;
import java.util.WeakHashMap;

/**
 *
 * @author R. Haenni
 */
public class PseudoRandomOracle
	   extends AbstractRandomOracle {

	private static final long serialVersionUID = 1L;

	private static PseudoRandomOracle instance;

	private final WeakHashMap<ByteArray, ReferenceRandomByteSequence> referenceRandomByteSequences;
	private final HashAlgorithm hashAlgorithm;

	protected PseudoRandomOracle(HashAlgorithm hashAlgorithm, Converter<BigInteger, ByteArray> bigIntegerConverter, Converter<String, ByteArray> stringConverter) {
		super(bigIntegerConverter, stringConverter);
		this.referenceRandomByteSequences = new WeakHashMap<>();
		this.hashAlgorithm = hashAlgorithm;
	}

	public final HashAlgorithm getHashAlgorithm() {
		return this.hashAlgorithm;
	}

	@Override
	protected ReferenceRandomByteSequence abstractQuery(ByteArray input) {
		ReferenceRandomByteSequence randomByteSequence = this.referenceRandomByteSequences.get(input);
		if (randomByteSequence == null) {
			randomByteSequence = ReferenceRandomByteSequence.getInstance(this.hashAlgorithm, input);
			this.referenceRandomByteSequences.put(input, randomByteSequence);
		}
		randomByteSequence.reset();
		return randomByteSequence;
	}

	public static PseudoRandomOracle getInstance() {
		if (PseudoRandomOracle.instance == null) {
			PseudoRandomOracle.instance = PseudoRandomOracle.getInstance(HashAlgorithm.getInstance());
		}
		return instance;
	}

	public static PseudoRandomOracle getInstance(HashAlgorithm hashAlgorithm) {
		return getInstance(hashAlgorithm, BigIntegerToByteArray.getInstance(), StringToByteArray.getInstance());
	}

	public static PseudoRandomOracle getInstance(HashAlgorithm hashAlgorithm, Converter<BigInteger, ByteArray> bigIntegerConverter, Converter<String, ByteArray> stringConverter) {
		if (hashAlgorithm == null || bigIntegerConverter == null || stringConverter == null) {
			throw new IllegalArgumentException();
		}
		return new PseudoRandomOracle(hashAlgorithm, bigIntegerConverter, stringConverter);
	}

}
