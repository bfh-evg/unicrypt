package ch.bfh.unicrypt.math.helper;

import ch.bfh.unicrypt.math.utility.RandomUtil;
import java.util.Arrays;
import java.util.Random;

public class Permutation
			 extends UniCrypt {

	private final int[] permutationVector;

	public Permutation(int[] permutationVector) {
		this.permutationVector = permutationVector;
	}

	/**
	 * Returns the size of the permutation element, which is the length of the corresponding permutation vector. The size
	 * of a permutation element is the same as the size of the corresponding group.
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
		if (getClass() != obj.getClass()) {
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
		return Permutation.getRandomInstance(size, (Random) null);
	}

	public static Permutation getRandomInstance(int size, Random random) {
		if (size < 0) {
			throw new IllegalArgumentException();
		}
		int[] permutationVector = new int[size];
		int randomIndex;
		for (int i = 0; i < size; i++) {
			randomIndex = RandomUtil.getRandomInteger(i, random);
			permutationVector[i] = permutationVector[randomIndex];
			permutationVector[randomIndex] = i;
		}
		return new Permutation(permutationVector);
	}

}
