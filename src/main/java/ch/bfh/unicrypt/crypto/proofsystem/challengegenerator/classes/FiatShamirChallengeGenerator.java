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
package ch.bfh.unicrypt.crypto.proofsystem.challengegenerator.classes;

import ch.bfh.unicrypt.crypto.proofsystem.challengegenerator.abstracts.AbstractSigmaChallengeGenerator;
import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.converter.classes.ConvertMethod;
import ch.bfh.unicrypt.helper.converter.classes.biginteger.FiniteByteArrayToBigInteger;
import ch.bfh.unicrypt.helper.converter.classes.bytearray.BigIntegerToByteArray;
import ch.bfh.unicrypt.helper.converter.classes.bytearray.StringToByteArray;
import ch.bfh.unicrypt.helper.hash.HashAlgorithm;
import ch.bfh.unicrypt.helper.hash.HashMethod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.Z;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZElement;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.Triple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

/**
 * This class provides a Fiat-Shamir SigmaChallengeGenerator
 *
 * Instead of using the RandomOracle as other implementation of the SigmaChallengeGenerator, this class uses a hash
 * function to compute the challenge.
 *
 * @author Philémon von Bergen &lt;philemon.vonbergen@bfh.ch&gt;
 */
public class FiatShamirChallengeGenerator extends AbstractSigmaChallengeGenerator {

    private final Element proverId;
    private final HashMethod hashMethod;
    private final FiniteByteArrayToBigInteger byteArrayToBigIntConverter;

    protected FiatShamirChallengeGenerator(Set publicInputSpace, SemiGroup commitmentSpace, Z challengeSpace,
	    Element proverId, HashMethod hashMethod, FiniteByteArrayToBigInteger byteArrayToBigIntConverter) {
	super(publicInputSpace, commitmentSpace, challengeSpace);
	this.proverId = proverId;
	this.hashMethod = hashMethod;
	this.byteArrayToBigIntConverter = byteArrayToBigIntConverter;
    }

    @Override
    protected ZElement abstractGenerate(Pair input) {
	ByteArray messageHashed = Triple.getInstance(input.getAt(0), input.getAt(1), proverId).getHashValue(hashMethod);
	return this.getChallengeSpace().getElement(byteArrayToBigIntConverter.convert(messageHashed));
    }

    /**
     * Creates a SigmaChallenge generator using a hash function to generate the challenge
     *
     * The generated challenge depends on the public input, the commitment and the other input. These values are
     * combined into a triple and hashed together using the given HashMethod. The string called otherInput is converted
     * into a string element. To hash the triple (public input, commitment, other input) each of these elements are
     * converted using the converters passed in the HashMethod. The result of the HashFunction is converted back to a
     * BigInteger using the given converter as a before the methods returns its corresponding value in the challenge
     * space.
     *
     * This default factory method uses SHA256 as Hash function, Recursive hash mode, big endian as BigIntegerConverter,
     * UFT-8 String converter, and standard FiniteByteArrayToBigInteger.
     *
     * @param publicInputSpace space containing the public input
     * @param commitmentSpace space containing the commitment
     * @param challengeSpace space containing the challenge
     * @param proverId other stuff that must be hash with the public input and the commitment to obtain the challenge
     * @return a challenge generator
     */
    public static FiatShamirChallengeGenerator getInstance(Set publicInputSpace, SemiGroup commitmentSpace,
	    Z challengeSpace, Element proverId) {
	return new FiatShamirChallengeGenerator(publicInputSpace, commitmentSpace, challengeSpace, proverId,
		HashMethod.getInstance(HashAlgorithm.SHA256, ConvertMethod.getInstance(BigIntegerToByteArray.
				getInstance(ByteOrder.BIG_ENDIAN), StringToByteArray.getInstance(Charset.
					forName("UTF-8"))), HashMethod.Mode.RECURSIVE), FiniteByteArrayToBigInteger.
		getInstance(HashAlgorithm.SHA256.getHashLength()));
    }

    /**
     *
     * Creates a SigmaChallenge generator using a hash function to generate the challenge
     *
     * The generated challenge depends on the public input, the commitment and the other input. These values are
     * combined into a triple and hashed together using the given HashMethod. The string called otherInput is converted
     * into a string element. To hash the triple (public input, commitment, other input) each of these elements are
     * converted using the converters passed in the HashMethod. The result of the HashFunction is converted back to a
     * BigInteger using the given converter as a before the methods returns its corresponding value in the challenge
     * space.
     *
     * @param publicInputSpace space containing the public input
     * @param commitmentSpace space containing the commitment
     * @param challengeSpace space containing the challenge
     * @param proverId other stuff that must be hash with the public input and the commitment to obtain the challenge
     * @param hashMethod Hash method to use to generate the challenge
     * @param byteArrayToBigIntConverter the converter to be used after the computation of the hash to convert the
     * obtained byte array in a big integer representing the challenge
     * @return a challenge generator
     */
    public static FiatShamirChallengeGenerator getInstance(Set publicInputSpace, SemiGroup commitmentSpace,
	    Z challengeSpace, Element proverId, HashMethod hashMethod,
	    FiniteByteArrayToBigInteger byteArrayToBigIntConverter) {
	return new FiatShamirChallengeGenerator(publicInputSpace, commitmentSpace, challengeSpace, proverId,
		hashMethod, byteArrayToBigIntConverter);
    }
}
