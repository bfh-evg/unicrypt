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
package ch.bfh.unicrypt.helper.converter;

import ch.bfh.unicrypt.helper.Permutation;
import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.math.MathUtil;
import java.math.BigInteger;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class PermutationConverter
	   extends Converter<Permutation> {

	private final BigIntegerConverter bigIntegerConverter;

	public PermutationConverter(BigIntegerConverter bigIntegerConverter) {
		super(Permutation.class.getName());
		this.bigIntegerConverter = bigIntegerConverter;
	}

	@Override
	protected ByteArray abstractConvertToByteArray(Permutation permutation) {
		BigInteger pairedValue = MathUtil.pair(BigInteger.valueOf(permutation.getSize()), permutation.getRank());
		return this.bigIntegerConverter.convertToByteArray(pairedValue);
	}

	@Override
	protected Permutation abstractConvertFromByteArray(ByteArray byteArray) {
		BigInteger pairedValue = this.bigIntegerConverter.convertFromByteArray(byteArray);
		BigInteger[] values = MathUtil.unpair(pairedValue);
		return Permutation.getInstance(values[0].intValue(), values[1]);
	}

	public static PermutationConverter getInstance() {
		return PermutationConverter.getInstance(BigIntegerConverter.getInstance());
	}

	public static PermutationConverter getInstance(BigIntegerConverter bigIntegerConverter) {
		if (bigIntegerConverter == null) {
			throw new IllegalArgumentException();
		}
		return new PermutationConverter(bigIntegerConverter);
	}

}
