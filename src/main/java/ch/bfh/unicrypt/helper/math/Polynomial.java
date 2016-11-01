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
import ch.bfh.unicrypt.helper.array.classes.BitArray;
import ch.bfh.unicrypt.helper.array.classes.SparseArray;
import ch.bfh.unicrypt.helper.sequence.Sequence;
import java.util.Map;

/**
 * Instances of this class represent polynomials {@code P(x)} with coefficients of a generic type. The internal
 * representation of the coefficients is optimized. If all the coefficients are either 0 or 1 (corresponding values of
 * the generic type must be declared), then an instance of {@link BitArray} is used internally, otherwise an instance of
 * {@link SparseArray}. Besides getter methods for the coefficients and the degree of the polynomial, no additional
 * functionality is provided.
 * <p>
 * @author P. Locher
 * @author R. Haenni
 * @version 2.0
 * @param <C> The generic type of the polynomial coefficients
 */
public class Polynomial<C>
	   extends UniCrypt {

	public static final int ZERO_POLYNOMIAL_DEGREE = -1;
	private static final long serialVersionUID = 1L;

	// the polynomial's degree
	private final int degree;

	// holds the non-zero coefficients, might be null if the polynomial is binary
	private final SparseArray<C> coefficients;

	// holds the coefficients of binary polynomials, might be null if the polynomial is not binary
	private final BitArray binaryCoefficients;

	// polynomial's zero coefficient
	private final C zeroCoefficient;

	// polynomial's one coefficient
	private final C oneCoefficient;

	private Polynomial(SparseArray<C> coefficients, C zeroCoefficient, C oneCoefficient) {
		this.coefficients = coefficients;
		this.zeroCoefficient = zeroCoefficient;
		this.oneCoefficient = oneCoefficient;

		int maxIndex = -1;
		boolean isBinary = true;
		for (Integer index : coefficients.getIndicesExcept()) {
			maxIndex = Math.max(maxIndex, index);
			isBinary = isBinary && this.oneCoefficient.equals(coefficients.getAt(index));
		}
		if (maxIndex == -1) {
			this.degree = ZERO_POLYNOMIAL_DEGREE;
		} else {
			this.degree = maxIndex;
		}

		if (isBinary) {
			if (this.degree == ZERO_POLYNOMIAL_DEGREE) {
				this.binaryCoefficients = BitArray.getInstance();
			} else {
				boolean[] bits = new boolean[this.degree + 1];
				for (Integer index : coefficients.getIndicesExcept()) {
					bits[index] = true;
				}
				this.binaryCoefficients = BitArray.getInstance(bits);
			}
		} else {
			this.binaryCoefficients = null;
		}
	}

	private Polynomial(BitArray coefficients, C zeroCoefficient, C oneCoefficient) {
		this.coefficients = null;
		this.binaryCoefficients = coefficients;
		this.zeroCoefficient = zeroCoefficient;
		this.oneCoefficient = oneCoefficient;
		if (coefficients.getLength() == 0) {
			this.degree = ZERO_POLYNOMIAL_DEGREE;
		} else {
			this.degree = coefficients.getLength() - 1;
		}
	}

	/**
	 * Constructs a new polynomial from the entries of a given map of type {@code <Integer, C>}. Corresponding values of
	 * type {@code C} need to be declared for the coefficients 0 and 1.
	 * <p>
	 * @param <C>             The coefficient type of the resulting polynomial
	 * @param coefficients    The map containing the coefficients
	 * @param zeroCoefficient The value for the coefficient 0
	 * @param oneCoefficient  The value for the coefficient 1
	 * @return The new polynomial
	 */
	public static <C> Polynomial<C> getInstance(Map<Integer, C> coefficients, C zeroCoefficient, C oneCoefficient) {
		if (coefficients == null || zeroCoefficient == null || oneCoefficient == null
			   || zeroCoefficient.equals(oneCoefficient)) {
			throw new IllegalArgumentException();
		}
		SparseArray<C> sparseArray = SparseArray.getInstance(zeroCoefficient, coefficients);
		return new Polynomial<>(sparseArray.removeSuffix(), zeroCoefficient, oneCoefficient);
	}

	/**
	 * Constructs a new polynomial from the entries of a given array of type {@code C[]}. Corresponding values of type
	 * {@code C} need to be declared for the coefficients 0 and 1.
	 * <p>
	 * @param <C>             The coefficient type of the resulting polynomial
	 * @param coefficients    The array containing the coefficients
	 * @param zeroCoefficient The value for the coefficient 0
	 * @param oneCoefficient  The value for the coefficient 1
	 * @return The new polynomial
	 */
	public static <C> Polynomial<C> getInstance(C[] coefficients, C zeroCoefficient, C oneCoefficient) {
		if (coefficients == null || zeroCoefficient == null || oneCoefficient == null
			   || zeroCoefficient.equals(oneCoefficient)) {
			throw new IllegalArgumentException();
		}
		SparseArray<C> sparseArray = SparseArray.getInstance(zeroCoefficient, coefficients);
		return new Polynomial<>(sparseArray.removeSuffix(), zeroCoefficient, oneCoefficient);
	}

	/**
	 * Constructs a new polynomial for a given {@code BitArray} containing coefficients 0 and 1. Corresponding values of
	 * type {@code C} need to be declared for the coefficients 0 and 1.
	 * <p>
	 * @param <C>             The coefficient type of the resulting polynomial
	 * @param coefficients    The bit array containing the coefficients
	 * @param zeroCoefficient The value for the coefficient 0
	 * @param oneCoefficient  The value for the coefficient 1
	 * @return The new polynomial
	 */
	public static <C> Polynomial<C> getInstance(BitArray coefficients, C zeroCoefficient, C oneCoefficient) {
		if (coefficients == null || zeroCoefficient == null || oneCoefficient == null
			   || zeroCoefficient.equals(oneCoefficient)) {
			throw new IllegalArgumentException();
		}
		return new Polynomial<>(coefficients.removeSuffix(), zeroCoefficient, oneCoefficient);
	}

	/**
	 * Returns the degree of the polynomial, or {@link Polynomial#ZERO_POLYNOMIAL_DEGREE} for {@code P(x)=0}.
	 * <p>
	 * @return The degree of the polynomial.
	 */
	public int getDegree() {
		return this.degree;
	}

	/**
	 * Checks if all coefficients of the polynomial are 0, which is the case for {@code P(x)=0}.
	 * <p>
	 * @return {@code true} for {@code P(x)=0}, {@code false} otherwise
	 */
	public boolean isZero() {
		return this.degree == ZERO_POLYNOMIAL_DEGREE;
	}

	/**
	 * Checks the polynomial is monic, i.e., if the highest order coefficient of the polynomial is 1.
	 * <p>
	 * @return {@code true} if the the polynomial is monic, {@code false} otherwise
	 */
	public boolean isMonic() {
		return !this.isZero() && this.oneCoefficient.equals(this.getCoefficient(this.degree));
	}

	/**
	 * Counts and returns the number of coefficients different from 0.
	 * <p>
	 * @return The number of coefficients different from 0
	 */
	public int countCoefficients() {
		if (this.isBinary()) {
			return this.binaryCoefficients.countExcept();
		} else {
			return this.coefficients.countExcept();
		}
	}

	/**
	 * Returns the coefficient at some given index.
	 * <p>
	 * @param index The given index
	 * @return The corresponding coefficient
	 */
	public C getCoefficient(int index) {
		if (index < 0) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_INDEX, this, index);
		}
		if (this.isBinary()) {
			if (index < this.binaryCoefficients.getLength() && this.binaryCoefficients.getAt(index)) {
				return this.oneCoefficient;
			}
		} else {
			if (index < this.coefficients.getLength()) {
				return this.coefficients.getAt(index);
			}
		}
		return this.zeroCoefficient;
	}

	/**
	 * Returns the bit array representing the coefficients of a polynomial consisting only of coefficients 0 and 1.
	 * <p>
	 * @return The bit array representing the coefficients.
	 */
	public BitArray getCoefficients() {
		if (this.isBinary()) {
			return this.binaryCoefficients;
		}
		throw new UniCryptRuntimeException(ErrorCode.UNSUPPORTED_OPERATION, this);
	}

	/**
	 * Returns the sequence of indices with a coefficients different from 0.
	 * <p>
	 * @return The sequence of indices
	 */
	public final Sequence<Integer> getCoefficientIndices() {
		if (this.isBinary()) {
			return binaryCoefficients.getIndicesExcept();
		} else {
			return coefficients.getIndicesExcept();
		}
	}

	/**
	 * Creates and returns a new polynomial containing only a single coefficient from the given polynomial.
	 * <p>
	 * @param index The index of the coefficient
	 * @return The new polynomial
	 */
	public final Polynomial<C> getTerm(int index) {
		if (index < 0) {
			throw new IllegalArgumentException();
		}
		SparseArray sparseArray = SparseArray.getInstance(this.zeroCoefficient, index, this.getCoefficient(index));
		return new Polynomial(sparseArray, this.zeroCoefficient, this.oneCoefficient);
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 79 * hash + (this.coefficients != null ? this.coefficients.hashCode()
			   : this.binaryCoefficients.hashCode());
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
		final Polynomial<C> other = (Polynomial<C>) obj;

		if (this.getDegree() == ZERO_POLYNOMIAL_DEGREE && other.getDegree() == ZERO_POLYNOMIAL_DEGREE) {
			return true;
		}

		if (this.isBinary()) {
			return this.binaryCoefficients == other.binaryCoefficients
				   || this.binaryCoefficients.equals(other.binaryCoefficients);
		} else {
			return this.coefficients == other.coefficients || this.coefficients.equals(other.coefficients);
		}
	}

	@Override
	protected String defaultToStringContent() {
		String result = "f(x)=";
		String separator = "";
		Sequence<Integer> indices = this.getCoefficientIndices();
		if (indices.isEmpty()) {
			result += this.coefficientToString(this.zeroCoefficient);
		}
		for (Integer index : indices) {
			C coefficient = this.getCoefficient(index);
			if (coefficient != this.zeroCoefficient || this.getDegree() == 0) {
				result += separator;
				if (coefficient != this.oneCoefficient || index == 0) {
					result += this.coefficientToString(coefficient);
				}
				if (index > 0) {
					result += index == 1 ? "x" : "x^" + index;
				}
				separator = "+";
			}
		}

		return result;
	}

	private String coefficientToString(C coefficient) {
		if (coefficient.equals(this.zeroCoefficient)) {
			return "0";
		}
		if (coefficient.equals(this.oneCoefficient)) {
			return "1";
		}
		return coefficient.toString();
	}

	private boolean isBinary() {
		return this.binaryCoefficients != null;
	}

}
