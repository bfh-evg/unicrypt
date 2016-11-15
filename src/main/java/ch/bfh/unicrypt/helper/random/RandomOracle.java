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
package ch.bfh.unicrypt.helper.random;

import ch.bfh.unicrypt.UniCrypt;
import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.converter.classes.bytearray.BigIntegerToByteArray;
import ch.bfh.unicrypt.helper.converter.classes.bytearray.StringToByteArray;
import ch.bfh.unicrypt.helper.converter.interfaces.Converter;
import ch.bfh.unicrypt.helper.hash.HashAlgorithm;
import ch.bfh.unicrypt.helper.random.deterministic.CTR_DRBG;
import ch.bfh.unicrypt.helper.random.deterministic.DeterministicRandomByteArraySequence;
import ch.bfh.unicrypt.helper.random.deterministic.DeterministicRandomByteSequence;
import java.math.BigInteger;

/**
 * Instances of this class are approximations of a random oracle. The accept arbitrary queries of type {@code long},
 * {@code BigInteger}, {@code String}, or {@code ByteArray} and return with a deterministic random byte sequences. To
 * convert integer and string queries into byte arrays, corresponding converters must be specified when a random oracle
 * is created. To trim the query to the seed length of the deterministic random bit generator, a hash algorithm must be
 * supplied.
 * <p>
 * @author R. Haenni
 * @version 2.0
 */
public class RandomOracle
	   extends UniCrypt {

	private static final long serialVersionUID = 1L;

	private final DeterministicRandomByteArraySequence.Factory factory;
	private final HashAlgorithm hashAlgorithm;
	private final Converter<BigInteger, ByteArray> bigIntegerConverter;
	private final Converter<String, ByteArray> stringConverter;

	protected RandomOracle(DeterministicRandomByteArraySequence.Factory factory, HashAlgorithm hashAlgorithm,
		   Converter<BigInteger, ByteArray> bigIntegerConverter, Converter<String, ByteArray> stringConverter) {
		this.factory = factory;
		this.hashAlgorithm = hashAlgorithm;
		this.bigIntegerConverter = bigIntegerConverter;
		this.stringConverter = stringConverter;
	}

	/**
	 * Returns the deterministic random byte sequence for the default query of an empty byte array.
	 * <p>
	 * @return The resulting deterministic random byte sequence
	 */
	public final DeterministicRandomByteSequence query() {
		return this.query(ByteArray.getInstance());
	}

	/**
	 * Returns a deterministic random byte sequence from a query of type {@code long}.
	 * <p>
	 * @param input The query
	 * @return The resulting deterministic random byte sequence
	 */
	public final DeterministicRandomByteSequence query(long input) {
		return this.query(BigInteger.valueOf(input));
	}

	/**
	 * Returns a deterministic random byte sequence from a query of type {@code BigInteger}.
	 * <p>
	 * @param input The query
	 * @return The resulting deterministic random byte sequence
	 */
	public final DeterministicRandomByteSequence query(BigInteger input) {
		if (input == null) {
			throw new IllegalArgumentException();
		}
		return this.query(this.bigIntegerConverter.convert(input));
	}

	/**
	 * Returns a deterministic random byte sequence from a query of type {@code String}.
	 * <p>
	 * @param input The query
	 * @return The resulting deterministic random byte sequence
	 */
	public final DeterministicRandomByteSequence query(String input) {
		if (input == null) {
			throw new IllegalArgumentException();
		}
		return this.query(this.stringConverter.convert(input));
	}

	/**
	 * Returns a deterministic random byte sequence from a query of type {@code ByteArray}.
	 * <p>
	 * @param input The query
	 * @return The resulting deterministic random byte sequence
	 */
	public final DeterministicRandomByteSequence query(ByteArray input) {
		if (input == null) {
			throw new IllegalArgumentException();
		}
		ByteArray hash = this.hashAlgorithm.getHashValue(input).extractPrefix(this.factory.getSeedByteLength());
		return this.factory.getInstance(hash).getRandomByteSequence();
	}

	/**
	 * Returns a new random oracle for the default hash algorithm and the corresponding {@link CTR_DRBG} instance.
	 * Default {@code BigInteger} and {@code String} converters are selected.
	 * <p>
	 * @return The new random oracle
	 */
	public static RandomOracle getInstance() {
		return RandomOracle.getInstance(HashAlgorithm.getInstance());
	}

	/**
	 * Returns a new random oracle for a given hash algorithm and the corresponding {@link CTR_DRBG} instance. Default
	 * {@code BigInteger} and {@code String} converters are selected.
	 * <p>
	 * @param hashAlgorithm The given hash algorithm
	 * @return The new random oracle
	 */
	public static RandomOracle getInstance(HashAlgorithm hashAlgorithm) {
		return RandomOracle.getInstance(CTR_DRBG.getFactory(hashAlgorithm), hashAlgorithm);
	}

	/**
	 * Returns a new random oracle for a given factory of a deterministic random byte sequence. The default hash
	 * algorithm is selected. The output length of the default hash algorithm must be equal to the required seed length
	 * or larger. Default {@code BigInteger} and {@code String} converters are selected.
	 * <p>
	 * @param factory The given factory of a deterministic random byte sequence
	 * @return The new random oracle
	 */
	public static RandomOracle getInstance(DeterministicRandomByteArraySequence.Factory factory) {
		return RandomOracle.getInstance(factory, HashAlgorithm.getInstance());
	}

	/**
	 * Returns a new random oracle for a given factory of a deterministic random byte sequence and a given hash
	 * algorithm. The output length of the hash algorithm must be equal to the required seed length or larger. Default
	 * {@code BigInteger} and {@code String} converters are selected.
	 * <p>
	 * @param factory       The given factory of a deterministic random byte sequence
	 * @param hashAlgorithm The given hash algorithm
	 * @return The new random oracle
	 */
	public static RandomOracle getInstance(DeterministicRandomByteArraySequence.Factory factory,
		   HashAlgorithm hashAlgorithm) {
		return RandomOracle.getInstance(factory, hashAlgorithm, BigIntegerToByteArray.getInstance(), StringToByteArray.
										getInstance());
	}

	/**
	 * Returns a new random oracle for a given factory of a deterministic random byte sequence, a given hash algorithm,
	 * and given {@code BigInteger} and {@code String} converters. The output length of the hash algorithm must be equal
	 * to the required seed length or larger.
	 * <p>
	 * @param factory             The given factory of a deterministic random byte sequence
	 * @param hashAlgorithm       The given hash algorithm
	 * @param bigIntegerConverter The given {@code BigInteger} converter
	 * @param stringConverter     The given {@code String} converter
	 * @return The new random oracle
	 */
	public static RandomOracle getInstance(DeterministicRandomByteArraySequence.Factory factory,
		   HashAlgorithm hashAlgorithm, Converter<BigInteger, ByteArray> bigIntegerConverter,
		   Converter<String, ByteArray> stringConverter) {
		if (factory == null || hashAlgorithm == null || factory.getSeedByteLength() > hashAlgorithm.getByteLength() ||
			   bigIntegerConverter == null || stringConverter == null) {
			throw new IllegalArgumentException();
		}
		return new RandomOracle(factory, hashAlgorithm, bigIntegerConverter, stringConverter);
	}

}
