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
package ch.bfh.unicrypt.crypto.proofsystem.challengegenerator.classes;

import ch.bfh.unicrypt.crypto.proofsystem.challengegenerator.abstracts.AbstractNonInteractiveSigmaChallengeGenerator;
import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.converter.classes.ConvertMethod;
import ch.bfh.unicrypt.helper.converter.classes.biginteger.ByteArrayToBigInteger;
import ch.bfh.unicrypt.helper.converter.interfaces.Converter;
import ch.bfh.unicrypt.helper.hash.HashMethod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import java.math.BigInteger;

/**
 * This class provides a non-interactive sigma challenge generator using the Fiat-Shamir heuristic.
 *
 * @author P. Locher
 */
public class FiatShamirSigmaChallengeGenerator
	   extends AbstractNonInteractiveSigmaChallengeGenerator {

	private final ConvertMethod convertMethod;
	private final HashMethod hashMethod;
	private final Converter<ByteArray, BigInteger> converter;

	protected FiatShamirSigmaChallengeGenerator(ZMod challengeSpace, Element proverId, ConvertMethod convertMethod,
		   HashMethod hashMethod, Converter<ByteArray, BigInteger> converter) {
		super(challengeSpace, proverId);
		this.convertMethod = convertMethod;
		this.hashMethod = hashMethod;
		this.converter = converter;
	}

	public ConvertMethod<?> getConvertMethod() {
		return this.convertMethod;
	}

	public HashMethod<?> getHashMethod() {
		return this.hashMethod;
	}

	public Converter<ByteArray, BigInteger> getConverter() {
		return this.converter;
	}

	@Override
	protected ZModElement abstractAbstractGenerate(Element input) {
		ByteArray hashedInput = input.getHashValue(this.convertMethod, this.hashMethod);
		return this.getChallengeSpace()
			   .getElement(this.converter.convert(hashedInput).mod(this.challengeSpace.getModulus()));
	}

	public static FiatShamirSigmaChallengeGenerator getInstance(ZMod challengeSpace) {
		return FiatShamirSigmaChallengeGenerator.getInstance(challengeSpace, null);
	}

	/**
	 * Creates a SigmaChallenge generator using a hash function to generate the challenge
	 * <p>
	 * The generated challenge depends on the public input, the commitment and the other input. These values are
	 * combined into a triple and hashed together using the given ElementHashMethod. The string called otherInput is
	 * converted into a string element. To hash the triple (public input, commitment, other input) each of these
	 * elements are converted using the converters passed in the ElementHashMethod. The result of the HashFunction is
	 * converted back to a BigInteger using the given converter as a before the methods returns its corresponding value
	 * in the challenge space.
	 * <p>
	 * This default factory method uses SHA256 as Hash function, Recursive hash mode, big endian as BigIntegerConverter,
	 * UFT-8 String converter, and standard FiniteByteArrayToBigInteger.
	 * <p>
	 * @param challengeSpace
	 * @param proverId       other stuff that must be hash with the public input and the commitment to obtain the
	 *                       challenge
	 * @return a challenge generator
	 */
	public static FiatShamirSigmaChallengeGenerator getInstance(ZMod challengeSpace, Element proverId) {
		ConvertMethod<ByteArray> convertMethod = ConvertMethod.getInstance();
		HashMethod<ByteArray> hashMethod = HashMethod.getInstance();
		int hashLength = hashMethod.getHashAlgorithm().getByteLength();
		Converter<ByteArray, BigInteger> converter = ByteArrayToBigInteger.getInstance(hashLength);
		return FiatShamirSigmaChallengeGenerator.getInstance(challengeSpace, proverId, convertMethod, hashMethod,
															 converter);
	}

	public static <V> FiatShamirSigmaChallengeGenerator getInstance(ZMod challengeSpace, ConvertMethod<V> convertMethod,
		   HashMethod<V> hashMethod, Converter<ByteArray, BigInteger> converter) {
		return FiatShamirSigmaChallengeGenerator.getInstance(challengeSpace, null, convertMethod, hashMethod,
															 converter);
	}

	/**
	 *
	 * Creates a SigmaChallenge generator using a hash function to generate the challenge
	 * <p>
	 * The generated challenge depends on the public input, the commitment and the other input. These values are
	 * combined into a triple and hashed together using the given ElementHashMethod. The string called otherInput is
	 * converted into a string element. To hash the triple (public input, commitment, other input) each of these
	 * elements are converted using the converters passed in the ElementHashMethod. The result of the HashFunction is
	 * converted back to a BigInteger using the given converter as a before the methods returns its corresponding value
	 * in the challenge space.
	 * <p>
	 * @param <V>
	 * @param challengeSpace
	 * @param proverId       other stuff that must be hash with the public input and the commitment to obtain the
	 *                       challenge
	 * @param convertMethod
	 * @param hashMethod     Hash method to use to generate the challenge
	 * @param converter      the converter to be used after the computation of the hash to convert the obtained byte
	 *                       array in a big integer representing the challenge
	 * @return a challenge generator
	 */
	public static <V> FiatShamirSigmaChallengeGenerator getInstance(ZMod challengeSpace, Element proverId,
		   ConvertMethod<V> convertMethod, HashMethod<V> hashMethod, Converter<ByteArray, BigInteger> converter) {
		if (challengeSpace == null || convertMethod == null || hashMethod == null || converter == null) {
			throw new IllegalArgumentException();
		}
		return new FiatShamirSigmaChallengeGenerator(challengeSpace, proverId, convertMethod, hashMethod, converter);
	}

	public static FiatShamirSigmaChallengeGenerator getInstance(Function function) {
		return FiatShamirSigmaChallengeGenerator.getInstance(function, null);
	}

	public static FiatShamirSigmaChallengeGenerator getInstance(Function function, Element proverId) {
		ConvertMethod<ByteArray> convertMethod = ConvertMethod.getInstance();
		HashMethod<ByteArray> hashMethod = HashMethod.getInstance();
		int hashLength = hashMethod.getHashAlgorithm().getByteLength();
		Converter<ByteArray, BigInteger> converter = ByteArrayToBigInteger.getInstance(hashLength);
		return FiatShamirSigmaChallengeGenerator.getInstance(function, proverId, convertMethod, hashMethod, converter);
	}

	public static <V> FiatShamirSigmaChallengeGenerator getInstance(Function function, ConvertMethod<V> convertMethod,
		   HashMethod<V> hashMethod, Converter<ByteArray, BigInteger> converter) {
		return FiatShamirSigmaChallengeGenerator.getInstance(function, null, convertMethod, hashMethod, converter);

	}

	public static <V> FiatShamirSigmaChallengeGenerator getInstance(Function function, Element proverId,
		   ConvertMethod<V> convertMethod, HashMethod<V> hashMethod, Converter<ByteArray, BigInteger> converter) {
		if (function == null || !function.getCoDomain().isSemiGroup() || convertMethod == null || hashMethod == null || converter == null) {
			throw new IllegalArgumentException();
		}
		ZMod challengeSpace = ZMod.getInstance(function.getDomain().getMinimalOrder());
		return new FiatShamirSigmaChallengeGenerator(challengeSpace, proverId, convertMethod, hashMethod, converter);
	}

}
