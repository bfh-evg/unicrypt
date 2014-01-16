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
package ch.bfh.unicrypt.math.helper;

import ch.bfh.unicrypt.crypto.random.classes.PseudoRandomGeneratorCounterMode;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomGenerator;
import java.util.Arrays;

public class Permutation
	   extends UniCrypt {

	private final int[] permutationVector;

	public Permutation(int[] permutationVector) {
		this.permutationVector = permutationVector;
	}

	/**
	 * Returns the size of the permutation element, which is the length of the corresponding permutation vector. The
	 * size of a permutation element is the same as the size of the corresponding group.
	 * <p>
	 * @return The size of the permutation element
	 */
	public int getSize() {
		return this.permutationVector.length;
	}

	/**
	 * Returns the result of applying the permutation vector to a given index.
	 * <p>
	 * @param index The given index
	 * @return The permuted index
	 * @throw IndexOutOfBoundsException if {@code index} is negative or greater than {@code getSize()-1}
	 */
	public int permute(int index) {
		if (index < 0 || index >= this.getSize()) {
			throw new IndexOutOfBoundsException();
		}
		return this.permutationVector[index];
	}

	public Permutation compose(Permutation other) {
		if (other == null) {
			throw new IllegalArgumentException();
		}
		int size = this.getSize();
		final int[] vector = new int[size];
		for (int i = 0; i < size; i++) {
			vector[i] = this.permute(other.permute(i));
		}
		return new Permutation(vector);
	}

	public Permutation invert() {
		int size = this.getSize();
		final int[] vector = new int[size];
		for (int i = 0; i < size; i++) {
			vector[this.permute(i)] = i;
		}
		return new Permutation(vector);
	}

	public int[] getPermutationVector() {
		return this.permutationVector.clone();
	}

	/**
	 * Checks if an array of integers is a permutation vector, i.e., a permutation of the values from 0 to n-1. For
	 * example {3,0,1,2,4} but not {1,4,3,2}.
	 * <p>
	 * @param permutationVector The given array of integers to test
	 * @return {@literal true} if {@literal permutationVector} is a permutation vector, {@literal false} otherwise
	 * @throws IllegalArgumentException if {@literal permutationVector} is null
	 */
	public static boolean isPermutationVector(final int... permutationVector) {
		if (permutationVector == null) {
			throw new IllegalArgumentException();
		}
		final int[] sortedVector = permutationVector.clone();
		Arrays.sort(sortedVector);
		for (int i = 0; i < permutationVector.length; i++) {
			if (sortedVector[i] != i) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String standardToStringContent() {
		return "" + this.getSize();
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 97 * hash + Arrays.hashCode(this.permutationVector);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		final Permutation other = (Permutation) obj;
		return Arrays.equals(this.permutationVector, other.permutationVector);
	}

	public static Permutation getInstance(int size) {
		if (size < 0) {
			throw new IllegalArgumentException();
		}
		int[] permutationVector = new int[size];
		for (int i = 0; i < size; i++) {
			permutationVector[i] = i;
		}
		return new Permutation(permutationVector);
	}

	public static Permutation getInstance(int[] permutationVector) {
		if (!isPermutationVector(permutationVector)) {
			throw new IllegalArgumentException();
		}
		return new Permutation(permutationVector);
	}

	public static Permutation getRandomInstance(int size) {
		return Permutation.getRandomInstance(size, null);
	}

	public static Permutation getRandomInstance(int size, RandomGenerator randomGenerator) {
		if (size < 0) {
			throw new IllegalArgumentException();
		}
		if (randomGenerator == null) {
			randomGenerator = PseudoRandomGeneratorCounterMode.DEFAULT;
		}
		int[] permutationVector = new int[size];
		int randomIndex;
		for (int i = 0; i < size; i++) {
			randomIndex = randomGenerator.nextInteger(i);
			permutationVector[i] = permutationVector[randomIndex];
			permutationVector[randomIndex] = i;
		}
		return new Permutation(permutationVector);
	}

}
