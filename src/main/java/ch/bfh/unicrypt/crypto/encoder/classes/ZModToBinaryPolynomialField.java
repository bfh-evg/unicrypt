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

import ch.bfh.unicrypt.crypto.encoder.abstracts.AbstractEncoder;
import ch.bfh.unicrypt.helper.array.classes.BitArray;
import ch.bfh.unicrypt.helper.converter.classes.biginteger.BitArrayToBigInteger;
import ch.bfh.unicrypt.helper.random.RandomByteSequence;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialField;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import java.math.BigInteger;

/**
 * Encodes a ZModElement as a binary polynomial by taking the bit-array representation of the ZModElement to create the
 * polynomial
 * <p>
 * @author lutzch
 * <p>
 */
public class ZModToBinaryPolynomialField
	   extends AbstractEncoder<ZMod, ZModElement, PolynomialField, PolynomialElement> {

	private static final long serialVersionUID = -4567955434932946745L;

	private final ZMod zMod;
	private final PolynomialField binaryPolynomialField;
	private final BitArrayToBigInteger bitToBigIntConverter;

	private ZModToBinaryPolynomialField(ZMod zMod, PolynomialField binaryPolynomialField) {
		this.zMod = zMod;
		this.binaryPolynomialField = binaryPolynomialField;
		this.bitToBigIntConverter = BitArrayToBigInteger.getInstance();
	}

	@Override
	protected Function abstractGetEncodingFunction() {
		return new EncodingFunction(this.zMod, this.binaryPolynomialField);
	}

	@Override
	protected Function abstractGetDecodingFunction() {
		return new DecodingFunction(this.binaryPolynomialField, this.zMod);
	}

	public static ZModToBinaryPolynomialField getInstance(ZMod zMod, PolynomialField binaryPolynomialField) {
		if (zMod == null || binaryPolynomialField == null) {
			throw new IllegalArgumentException();
		}
		return new ZModToBinaryPolynomialField(zMod, binaryPolynomialField);
	}

	private class EncodingFunction
		   extends AbstractFunction<EncodingFunction, ZMod, ZModElement, PolynomialField, PolynomialElement> {

		protected EncodingFunction(final ZMod domain, final PolynomialField coDomain) {
			super(domain, coDomain);
		}

		@Override
		protected PolynomialElement abstractApply(final ZModElement element,
			   final RandomByteSequence randomByteSequence) {
			BitArray bitArray = bitToBigIntConverter.reconvert(element.getValue());
			return this.getCoDomain().getElement(bitArray);
		}

	}

	class DecodingFunction
		   extends AbstractFunction<DecodingFunction, PolynomialField, PolynomialElement, ZMod, ZModElement> {

		protected DecodingFunction(final PolynomialField domain, final ZMod coDomain) {
			super(domain, coDomain);
		}

		@Override
		protected ZModElement abstractApply(final PolynomialElement element,
			   final RandomByteSequence randomByteSequence) {
			BitArray bitArray = element.getValue().getCoefficients();
			BigInteger bigInt = bitToBigIntConverter.convert(bitArray);
			return this.getCoDomain().getElement(bigInt.mod(this.getCoDomain().getModulus()));
		}

	}

}
