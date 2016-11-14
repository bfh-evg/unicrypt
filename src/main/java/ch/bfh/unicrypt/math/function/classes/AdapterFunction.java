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
import ch.bfh.unicrypt.helper.random.RandomByteSequence;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import java.util.Arrays;

/**
 * This class represents an restricted identity function, which selects multiple elements from an input tuple element.
 * <p>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 1.0
 */
public class AdapterFunction
	   extends AbstractFunction<AdapterFunction, ProductSet, Tuple, ProductSet, Tuple> {

	private static final long serialVersionUID = 1L;

	private final int[] indices;

	private AdapterFunction(final ProductSet domain, final ProductSet coDomain, int[] indices) {
		super(domain, coDomain);
		this.indices = indices;
	}

	public int[] getIndices() {
		return Arrays.copyOf(this.indices, this.indices.length);
	}

	@Override
	protected boolean defaultIsEquivalent(AdapterFunction function) {
		return Arrays.equals(this.getIndices(), function.getIndices());
	}

	@Override
	protected Tuple abstractApply(final Tuple element, final RandomByteSequence randomByteSequence) {
		Element[] elements = new Element[this.getIndices().length];
		for (int i = 0; i < this.getIndices().length; i++) {
			elements[i] = element.getAt(this.getIndices()[i]);
		}
		return this.getCoDomain().getElement(elements);
	}

	/**
	 * This is the general constructor of this class. The resulting function selects and returns in a hierarchy of tuple
	 * elements the element that corresponds to a given sequence of indices (e.g., 0,3,2 for the third element in the
	 * fourth tuple element of the first tuple element).
	 * <p>
	 * @param productSet The product group that defines the domain of the function
	 * @param indices    The given sequence of indices
	 * @return The new function
	 */
	public static AdapterFunction getInstance(final ProductSet productSet, final int... indices) {
		if (productSet == null || indices == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, productSet, indices);
		}
		Set[] sets = new Set[indices.length];
		for (int i = 0; i < indices.length; i++) {
			sets[i] = productSet.getAt(indices[i]);
		}
		return new AdapterFunction(productSet, ProductSet.getInstance(sets), indices);
	}

}
