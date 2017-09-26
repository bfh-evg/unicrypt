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
package ch.bfh.unicrypt.math.algebra.general.classes;

import ch.bfh.unicrypt.ErrorCode;
import ch.bfh.unicrypt.UniCryptRuntimeException;
import ch.bfh.unicrypt.helper.cache.Cache;
import ch.bfh.unicrypt.helper.converter.classes.biginteger.PermutationToBigInteger;
import ch.bfh.unicrypt.helper.converter.interfaces.Converter;
import ch.bfh.unicrypt.helper.math.MathUtil;
import ch.bfh.unicrypt.helper.math.Permutation;
import ch.bfh.unicrypt.helper.random.RandomByteSequence;
import ch.bfh.unicrypt.helper.random.RandomByteSequenceIterator;
import ch.bfh.unicrypt.helper.sequence.Sequence;
import ch.bfh.unicrypt.helper.sequence.SequenceIterator;
import ch.bfh.unicrypt.math.algebra.general.abstracts.AbstractGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import java.math.BigInteger;

/**
 * An instance of this class represents the group of permutations for a given size. The elements of the group are
 * permutations, which contain the values from 0 to size-1 in a permuted order. Applying the group operation to two
 * permutation elements means to construct the combined permutation element. Note that this operation is not
 * commutative. The identity element is the permutation [0, ..., size-1]. To invert an element, the inverse permutation
 * is computed. Permutation elements are considered to be atomic. This means that they can be converted into a unique
 * integer value and back. The group order is the factorial of its size.
 * <p>
 * @see "Handbook of Applied Cryptography, Example 2.164"
 * @see <a href="http://en.wikipedia.org/wiki/Integer">http://en.wikipedia.org/wiki/Integer</a>
 * <p>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public class PermutationGroup
	   extends AbstractGroup<PermutationElement, Permutation> {

	private static final long serialVersionUID = 1L;

	private final int size;

	private PermutationGroup(final int size) {
		super(Permutation.class);
		this.size = size;
	}

	/**
	 * Returns the size of the permutation elements in this group. The smallest possible size is 0, which represents the
	 * trivial case of an empty permutation.
	 * <p>
	 * @return The permutation size
	 */
	public final int getSize() {
		return this.size;
	}

	@Override
	protected String defaultToStringContent() {
		return "" + this.getSize();
	}

	@Override
	protected Sequence<PermutationElement> abstractGetRandomElements(final RandomByteSequence randomByteSequence) {
		final RandomByteSequenceIterator iterator = randomByteSequence.iterator();
		return new Sequence<PermutationElement>() {

			@Override
			public SequenceIterator<PermutationElement> iterator() {
				return new SequenceIterator<PermutationElement>() {

					@Override
					protected PermutationElement abstractNext() {
						// Durstenfeld's version of the Fisher–Yates shuffle algorithm
						int[] permutationVector = new int[size];
						int randomIndex;
						for (int i = 0; i < size; i++) {
							// the following lines are necessary to use the existing random integer generation on the
							// same iterator
							randomIndex = new RandomByteSequence() {

								@Override
								public RandomByteSequenceIterator iterator() {
									return iterator;
								}

							}.getRandomIntegerSequence(i).get();
							permutationVector[i] = permutationVector[randomIndex];
							permutationVector[randomIndex] = i;
						}
						return abstractGetElement(Permutation.getInstance(permutationVector));
					}

					@Override
					public boolean hasNext() {
						return true;
					}

				};
			}

		};
	}

	@Override
	protected boolean abstractContains(Permutation value) {
		return value.getSize() == this.getSize();
	}

	@Override
	protected PermutationElement abstractGetElement(Permutation value) {
		return new PermutationElement(this, value);
	}

	@Override
	protected Converter<Permutation, BigInteger> abstractGetBigIntegerConverter() {
		return PermutationToBigInteger.getInstance(this.size);
	}

	@Override
	protected PermutationElement abstractApply(final PermutationElement element1, final PermutationElement element2) {
		return this.abstractGetElement(element1.getValue().compose(element2.getValue()));
	}

	@Override
	protected PermutationElement abstractInvert(final PermutationElement element) {
		return this.abstractGetElement(element.getValue().invert());
	}

	@Override
	protected BigInteger abstractGetOrder() {
		return MathUtil.factorial(this.getSize());
	}

	@Override
	protected PermutationElement abstractGetIdentityElement() {
		return this.abstractGetElement(Permutation.getInstance(this.getSize()));
	}

	@Override
	public boolean abstractEquals(final Set set) {
		final PermutationGroup other = (PermutationGroup) set;
		return this.getSize() == other.getSize();
	}

	@Override
	protected int abstractHashCode() {
		int hash = 7;
		hash = 47 * hash + this.getSize();
		return hash;
	}

	private static final Cache<Integer, PermutationGroup> CACHE = new Cache<>(Cache.SIZE_M);

	/**
	 * Returns a the unique instance of this class for a given non-negative permutation size.
	 * <p>
	 * @param size The size of the permutation
	 * @return The unique instance of this class for the provided size
	 */
	public static PermutationGroup getInstance(final int size) {
		if (size < 0) {
			throw new UniCryptRuntimeException(ErrorCode.NEGATIVE_VALUE);
		}
		PermutationGroup instance = PermutationGroup.CACHE.get(size);
		if (instance == null) {
			instance = new PermutationGroup(size);
			PermutationGroup.CACHE.put(size, instance);
		}
		return instance;
	}

}
