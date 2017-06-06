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
package ch.bfh.unicrypt.helper.converter.classes.biginteger;

import ch.bfh.unicrypt.helper.converter.abstracts.AbstractBigIntegerConverter;
import ch.bfh.unicrypt.helper.math.MathUtil;
import ch.bfh.unicrypt.helper.math.Permutation;
import java.math.BigInteger;

/**
 * Instances of this class convert permutations of a given size {@code s} into non-negative {@code BigInteger} values
 * {@code 0, 1, 2, ..., s!-1}, using the ranking algorithm by Myrvold and Ruskey: "Ranking and Unranking Permutations in
 * Linear Time".
 * <p>
 * @see Permutation#getRank()
 * @author R. Haenni
 * @version 2.0
 */
public class PermutationToBigInteger
	   extends AbstractBigIntegerConverter<Permutation> {

	private static final long serialVersionUID = 1L;

	private final int size;
	private BigInteger inputSize; // factorial of size

	protected PermutationToBigInteger(int size) {
		super(Permutation.class);
		this.size = size;
		this.inputSize = null; // lazy factorial computation
	}

	/**
	 * Creates a new {@link PermutationToBigInteger} converter for a given permutation size.
	 * <p>
	 * @param size The given size
	 * @return The new converter
	 */
	public static PermutationToBigInteger getInstance(int size) {
		if (size < 0) {
			throw new IllegalArgumentException();
		}
		return new PermutationToBigInteger(size);
	}

	@Override
	protected boolean defaultIsValidInput(Permutation permutation) {
		return permutation.getSize() == this.size;
	}

	@Override
	protected boolean defaultIsValidOutput(BigInteger value) {
		if (this.inputSize == null) {
			this.inputSize = MathUtil.factorial(size);
		}
		return value.signum() >= 0 && value.compareTo(this.inputSize) < 0;
	}

	@Override
	protected BigInteger abstractConvert(Permutation permutation) {
		return permutation.getRank();
	}

	@Override
	protected Permutation abstractReconvert(BigInteger value) {
		return Permutation.getInstance(this.size, value);
	}

}
