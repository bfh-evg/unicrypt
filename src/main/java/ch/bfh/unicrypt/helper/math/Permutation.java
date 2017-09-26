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
package ch.bfh.unicrypt.helper.math;

import ch.bfh.unicrypt.ErrorCode;
import ch.bfh.unicrypt.UniCrypt;
import ch.bfh.unicrypt.UniCryptRuntimeException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Instances of this class represent permutations {@code p:{0,...,size-1}->{0,...,size-1}} of a given {@code size}.
 * Internally, the objects store corresponding permutation vectors describing the mapping.
 * <p>
 * @author R. Haenni
 * @version 2.0
 */
public class Permutation
	   extends UniCrypt
	   implements Iterable<Integer> {

	private static final long serialVersionUID = 1L;

	private final int[] permutationVector;
	private BigInteger rank;

	private Permutation(int[] permutationVector, BigInteger rank) {
		this.permutationVector = permutationVector;
		this.rank = rank;
	}

	private Permutation(int[] permutationVector) {
		this(permutationVector, null);
	}

	/**
	 * Returns the identity permutation {@code 0->0, 1->1, ..., (size-1)->(size-1)} of a given {@code size>=0}.
	 * <p>
	 * @param size The size of the permutation
	 * @return The new permutation
	 */
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

	/**
	 * This is the standard method for creating new permutations based on a given permutation vector. For example, the
	 * vector {@code [2,1,3,0]} defines a mapping {@code 0->2, 1->1, 2->3, 3->0}.
	 * <p>
	 * @param permutationVector The given permutation vector
	 * @return The new permutation
	 */
	public static Permutation getInstance(int[] permutationVector) {
		if (permutationVector == null) {
			throw new IllegalArgumentException();
		}
		final int[] sortedVector = permutationVector.clone();
		Arrays.sort(sortedVector);
		for (int i = 0; i < sortedVector.length; i++) {
			if (sortedVector[i] != i) {
				throw new IllegalArgumentException();
			}
		}
		return new Permutation(permutationVector.clone());
	}

	/**
	 * Computes the rank of a given permutation using the algorithm by Myrvold and Ruskey: "Ranking and Unranking
	 * Permutations in Linear Time". The integers from {@code 0} to {@code size!-1} are valid ranks.
	 * <p>
	 * @param size The size of the permutation
	 * @param rank The given rank
	 * @return The new permutation
	 * @see Permutation#getRank()
	 */
	public static Permutation getInstance(int size, BigInteger rank) {
		if (size < 0 || rank == null || rank.signum() < 0) {
			throw new IllegalArgumentException();
		}
		int[] permutationVector = new int[size];
		for (int i = 0; i < size; i++) {
			permutationVector[i] = i;
		}
		BigInteger r = rank;
		for (int i = size; i > 0; i--) {
			BigInteger iBig = BigInteger.valueOf(i);
			swap(permutationVector, r.mod(iBig).intValue(), i - 1);
			r = r.divide(iBig);
		}
		// original rank >= factorial(size)
		if (r.signum() != 0) {
			throw new IllegalArgumentException();
		}
		return new Permutation(permutationVector, rank);
	}

	/**
	 * Returns the size of the permutation, which corresponds to the length of the corresponding permutation vector.
	 * <p>
	 * @return The size of the permutation
	 */
	public int getSize() {
		return this.permutationVector.length;
	}

	//
	/**
	 * Computes and returns the rank of the permutation using the ranking algorithm by Myrvold and Ruskey: "Ranking and
	 * Unranking Permutations in Linear Time". The result is an integer in the range {@code [0,size!-1]}.
	 * <p>
	 * @return The rank of the permutation
	 * @see Permutation#getInstance(int, java.math.BigInteger)
	 */
	public BigInteger getRank() {
		if (this.rank == null) {
			int size = this.getSize();
			int[] invertedPermutation = new int[size];
			for (int i = 0; i < size; i++) {
				invertedPermutation[this.permutationVector[i]] = i;
			}
			this.rank = computeRank(size, this.permutationVector.clone(), invertedPermutation);
		}
		return this.rank;
	}

	/**
	 * Returns the result of applying the permutation to some input value.
	 * <p>
	 * @param value The given input value
	 * @return The permuted value
	 */
	public int permute(int value) {
		if (value < 0 || value >= this.getSize()) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_INDEX, this, value);
		}
		return this.permutationVector[value];
	}

	/**
	 * Takes two permutations of the same size as input and computes their composition. The result is a new permutation
	 * of the same size.
	 * <p>
	 * @param other The second permutation
	 * @return The new composed permutation
	 */
	public Permutation compose(Permutation other) {
		if (other == null || other.getSize() != this.getSize()) {
			throw new IllegalArgumentException();
		}
		int size = this.getSize();
		final int[] vector = new int[size];
		for (int i = 0; i < size; i++) {
			vector[i] = this.permute(other.permute(i));
		}
		return new Permutation(vector);
	}

	/**
	 * Computes and returns the inverted permutation.
	 * <p>
	 * @return The inverted permutation
	 */
	public Permutation invert() {
		int size = this.getSize();
		final int[] vector = new int[size];
		for (int i = 0; i < size; i++) {
			vector[this.permute(i)] = i;
		}
		return new Permutation(vector);
	}

	@Override
	protected String defaultToStringType() {
		return "";
	}

	@Override
	protected String defaultToStringContent() {
		String str = Arrays.toString(this.permutationVector);
		return "(" + str.substring(1, str.length() - 1) + ")";
	}

	@Override
	public Iterator<Integer> iterator() {
		return new Iterator<Integer>() {

			private int currentIndex = 0;

			@Override
			public boolean hasNext() {
				return this.currentIndex < permutationVector.length;
			}

			@Override
			public Integer next() {
				if (!this.hasNext()) {
					throw new NoSuchElementException();
				}
				return permutationVector[this.currentIndex++];
			}

		};
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

	private static BigInteger computeRank(int n, int[] permutation, int[] invertedPermutation) {
		if (n <= 1) {
			return MathUtil.ZERO;
		}
		int s = permutation[n - 1];
		swap(permutation, n - 1, invertedPermutation[n - 1]);
		swap(invertedPermutation, s, n - 1);
		return BigInteger.valueOf(s).add(BigInteger.valueOf(n).multiply(computeRank(n - 1, permutation,
																					invertedPermutation)));
	}

	private static void swap(int[] permutation, int i, int j) {
		int x = permutation[i];
		permutation[i] = permutation[j];
		permutation[j] = x;
	}

}
