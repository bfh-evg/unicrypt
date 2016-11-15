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
package ch.bfh.unicrypt.math.function.classes;

import ch.bfh.unicrypt.ErrorCode;
import ch.bfh.unicrypt.UniCryptRuntimeException;
import ch.bfh.unicrypt.helper.math.Permutation;
import ch.bfh.unicrypt.helper.random.RandomByteSequence;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.PermutationElement;
import ch.bfh.unicrypt.math.algebra.general.classes.PermutationGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;

/**
 * This interface represents the concept of a function f:X^n x Z->X^n, where Z is a permutation group of size n. Calling
 * the function permutes the given input tuple element of X^n according to the permutation element given as a second
 * argument. The output of the function is the permuted tuple element.
 * <p>
 * @see PermutationGroup
 * <p>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 1.0
 */
public class PermutationFunction
	   extends AbstractFunction<PermutationFunction, ProductSet, Pair, ProductSet, Tuple> {

	private static final long serialVersionUID = 1L;

	private PermutationFunction(final ProductSet domain, final ProductSet coDomain) {
		super(domain, coDomain);
	}

	/**
	 * Returns the permutation group of size n, which is need to conduct the actual permutation.
	 * <p>
	 * @return The permutation group
	 */
	public PermutationGroup getPermutationGroup() {
		return (PermutationGroup) this.getDomain().getAt(1);
	}

	@Override
	protected Tuple abstractApply(final Pair element, final RandomByteSequence randomByteSequence) {
		final Tuple elements = (Tuple) element.getFirst();
		final Permutation permutation = ((PermutationElement) element.getSecond()).getValue();
		final Element[] result = new Element[elements.getArity()];
		for (int i = 0; i < elements.getArity(); i++) {
			result[i] = elements.getAt(permutation.permute(i));
		}
		return this.getCoDomain().getElement(result);
	}

	/**
	 * This is the general constructor of this class, which construct a permutation function from a given group and for
	 * the specified arity.
	 * <p>
	 * @param set   The given group
	 * @param arity The arity of the tuple elements to permute
	 * @return Returns an instance of this class
	 */
	public static PermutationFunction getInstance(final Set set, final int arity) {
		if (set == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER);
		}
		if (arity < 0) {
			throw new UniCryptRuntimeException(ErrorCode.NEGATIVE_VALUE, arity);
		}
		return PermutationFunction.getInstance(ProductSet.getInstance(set, arity));
	}

	/**
	 * This is a special constructor of this class, which deals with the particular case, where a product group is given
	 * from the beginning.
	 * <p>
	 * @param productSet The given power group
	 * @return Returns an instance of this class
	 */
	public static PermutationFunction getInstance(final ProductSet productSet) {
		if (productSet == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER);
		}
		if (!productSet.isUniform()) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_ARGUMENT, productSet);
		}
		return new PermutationFunction(ProductSet.getInstance(productSet,
															  PermutationGroup.getInstance(productSet.getArity())),
									   productSet);
	}

}
