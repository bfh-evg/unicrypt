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
package ch.bfh.unicrypt.helper.sequence.random;

import ch.bfh.unicrypt.UniCrypt;
import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.converter.classes.bytearray.BigIntegerToByteArray;
import ch.bfh.unicrypt.helper.converter.classes.bytearray.StringToByteArray;
import ch.bfh.unicrypt.helper.converter.interfaces.Converter;
import ch.bfh.unicrypt.helper.hash.HashAlgorithm;
import ch.bfh.unicrypt.helper.sequence.random.deterministic.CTR_DRBG;
import ch.bfh.unicrypt.helper.sequence.random.deterministic.DeterministicRandomByteArraySequence;
import java.math.BigInteger;

/**
 *
 * @author R. Haenni
 */
public class RandomOracle
	   extends UniCrypt {

	private static final long serialVersionUID = 1L;

	private final DeterministicRandomByteArraySequence.Factory factory;
	private final HashAlgorithm hashAlgorithm;
	private final Converter<BigInteger, ByteArray> bigIntegerConverter;
	private final Converter<String, ByteArray> stringConverter;

	protected RandomOracle(DeterministicRandomByteArraySequence.Factory factory, HashAlgorithm hashAlgorithm, Converter<BigInteger, ByteArray> bigIntegerConverter, Converter<String, ByteArray> stringConverter) {
		this.factory = factory;
		this.hashAlgorithm = hashAlgorithm;
		this.bigIntegerConverter = bigIntegerConverter;
		this.stringConverter = stringConverter;
	}

	public final RandomByteSequence query() {
		return this.query(ByteArray.getInstance());
	}

	public final RandomByteSequence query(long input) {
		return this.query(BigInteger.valueOf(input));
	}

	public final RandomByteSequence query(BigInteger input) {
		if (input == null) {
			throw new IllegalArgumentException();
		}
		return this.query(this.bigIntegerConverter.convert(input));
	}

	public final RandomByteSequence query(String input) {
		if (input == null) {
			throw new IllegalArgumentException();
		}
		return this.query(this.stringConverter.convert(input));
	}

	public final RandomByteSequence query(ByteArray input) {
		if (input == null) {
			throw new IllegalArgumentException();
		}
		ByteArray hash = this.hashAlgorithm.getHashValue(input);
		return this.factory.getInstance(hash).getRandomByteSequence();
	}

	public static RandomOracle getInstance() {
		return RandomOracle.getInstance(HashAlgorithm.getInstance());
	}

	public static RandomOracle getInstance(HashAlgorithm hashAlgorithm) {
		return getInstance(CTR_DRBG.getFactory(hashAlgorithm), hashAlgorithm);
	}

	public static RandomOracle getInstance(DeterministicRandomByteArraySequence.Factory factory) {
		return getInstance(factory, HashAlgorithm.getInstance());
	}

	public static RandomOracle getInstance(DeterministicRandomByteArraySequence.Factory factory, HashAlgorithm hashAlgorithm) {
		return getInstance(factory, hashAlgorithm, BigIntegerToByteArray.getInstance(), StringToByteArray.getInstance());
	}

	public static RandomOracle getInstance(DeterministicRandomByteArraySequence.Factory factory, HashAlgorithm hashAlgorithm, Converter<BigInteger, ByteArray> bigIntegerConverter, Converter<String, ByteArray> stringConverter) {
		if (factory == null || hashAlgorithm == null || bigIntegerConverter == null || stringConverter == null) {
			throw new IllegalArgumentException();
		}
		return new RandomOracle(factory, hashAlgorithm, bigIntegerConverter, stringConverter);
	}

}
