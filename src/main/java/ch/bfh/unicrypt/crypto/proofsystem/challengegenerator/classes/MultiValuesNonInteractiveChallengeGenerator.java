/*
 * UniCrypt
 *
 *  UniCrypt(tm): Cryptographical framework allowing the implementation of cryptographic protocols e.g. e-voting
 *  Copyright (c) 2017 Bern University of Applied Sciences (BFH), Research Institute for
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

import ch.bfh.unicrypt.crypto.proofsystem.challengegenerator.abstracts.AbstractNonInteractiveChallengeGenerator;
import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.converter.classes.ConvertMethod;
import ch.bfh.unicrypt.helper.converter.classes.biginteger.ByteArrayToBigInteger;
import ch.bfh.unicrypt.helper.converter.classes.bytearray.BigIntegerToByteArray;
import ch.bfh.unicrypt.helper.converter.interfaces.Converter;
import ch.bfh.unicrypt.helper.hash.HashMethod;
import ch.bfh.unicrypt.helper.tree.Tree;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import java.math.BigInteger;

/**
 *
 * @author P. Locher
 */
public class MultiValuesNonInteractiveChallengeGenerator
	   extends AbstractNonInteractiveChallengeGenerator<ProductSet, Tuple> {

	private final ZMod singleChallengeSpace;
	private final int size;
	private final ConvertMethod convertMethod;
	private final HashMethod hashMethod;
	private final Converter<ByteArray, BigInteger> converter;
	private final Converter<BigInteger, ByteArray> indexConverter;

	protected MultiValuesNonInteractiveChallengeGenerator(ZMod singleChallengeSpace, int size, Element proverId, ConvertMethod convertMethod,
		   HashMethod hashMethod, Converter<ByteArray, BigInteger> converter, Converter<BigInteger, ByteArray> indexConverter) {
		super(ProductSet.getInstance(singleChallengeSpace, size), proverId);
		this.singleChallengeSpace = singleChallengeSpace;
		this.size = size;
		this.convertMethod = convertMethod;
		this.hashMethod = hashMethod;
		this.converter = converter;
		this.indexConverter = indexConverter;
	}

	public ZMod getSingleChallengeSpace() {
		return this.singleChallengeSpace;
	}

	public int getSize() {
		return this.size;
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

	// h(public input, index)
	// h(public input, index, proverId)
	//
	@Override
	protected Tuple abstractAbstractGenerate(Element<?> input) {

		Element<?> publicInput = this.getProverId() == null ? input : ((Pair) input).getFirst();

		Element[] elements = new Element[size];
		for (int i = 1; i <= size; i++) {
			Tree<ByteArray> indexedInput = this.getProverId() == null
				   ? Tree.getInstance(publicInput.getHashValue(this.convertMethod, this.hashMethod), this.indexConverter.convert(BigInteger.valueOf(i)))
				   : Tree.getInstance(publicInput.getHashValue(this.convertMethod, this.hashMethod), this.indexConverter.convert(BigInteger.valueOf(i)), this.getProverId().getHashValue(this.convertMethod, this.hashMethod));
			ByteArray hashedInput = this.hashMethod.getHashValue(indexedInput);

			elements[i - 1] = this.singleChallengeSpace.getElement(this.converter.convert(hashedInput).mod(this.singleChallengeSpace.getModulus()));
		}

		return Tuple.getInstance(elements);
	}

	public static MultiValuesNonInteractiveChallengeGenerator getInstance(ZMod challengeSpace, int size) {
		return MultiValuesNonInteractiveChallengeGenerator.getInstance(challengeSpace, size, null);
	}

	public static MultiValuesNonInteractiveChallengeGenerator getInstance(ZMod challengeSpace, int size, Element proverId) {
		ConvertMethod<ByteArray> convertMethod = ConvertMethod.getInstance();
		HashMethod<ByteArray> hashMethod = HashMethod.getInstance();
		int hashLength = hashMethod.getHashAlgorithm().getByteLength();
		Converter<ByteArray, BigInteger> converter = ByteArrayToBigInteger.getInstance(hashLength);
		Converter<BigInteger, ByteArray> indexConverter = BigIntegerToByteArray.getInstance();
		return MultiValuesNonInteractiveChallengeGenerator.getInstance(challengeSpace, size, proverId, convertMethod, hashMethod, converter, indexConverter);
	}

	public static <V> MultiValuesNonInteractiveChallengeGenerator getInstance(ZMod challengeSpace, int size, ConvertMethod<V> convertMethod,
		   HashMethod<V> hashMethod, Converter<ByteArray, BigInteger> converter, Converter<BigInteger, ByteArray> indexConverter) {
		return MultiValuesNonInteractiveChallengeGenerator.getInstance(challengeSpace, size, null, convertMethod,
																	   hashMethod, converter, indexConverter);

	}

	public static <V> MultiValuesNonInteractiveChallengeGenerator getInstance(ZMod challengeSpace, int size, Element proverId,
		   ConvertMethod<V> convertMethod, HashMethod<V> hashMethod, Converter<ByteArray, BigInteger> converter, Converter<BigInteger, ByteArray> indexConverter) {
		if (challengeSpace == null || size < 1 || convertMethod == null || hashMethod == null || converter == null || indexConverter == null) {
			throw new IllegalArgumentException();
		}
		return new MultiValuesNonInteractiveChallengeGenerator(challengeSpace, size, proverId, convertMethod, hashMethod, converter, indexConverter);
	}

}
