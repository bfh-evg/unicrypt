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
package ch.bfh.unicrypt.math.function.classes;

import ch.bfh.unicrypt.crypto.random.classes.RandomNumberGenerator;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.abstracts.AbstractCompoundFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

/**
 * This class represents the concept of a product function f:(X_1x...xX_n)->(Y_1x...xY_n). It consists of multiple
 * individual functions f_i:X_i->Y_i, which are applied in parallel to respective input elements. To be compatible with
 * {@link Function}, these input elements must be given as a tuple element of the product domain X_1x...xX_n. In the
 * same way, the output elements are returned as a tuple element of the product domain Y_1x...xY_n.
 * <p>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 1.0
 */
public final class ProductFunction
	   extends AbstractCompoundFunction<ProductFunction, ProductSet, Tuple, ProductSet, Tuple> {

	/**
	 * This is the general constructor of this class. It takes a list of functions as input and produces the
	 * corresponding product function.
	 * <p>
	 * @param domain
	 * @param coDomain
	 * @param functions
	 * @throws IllegalArgumentException if {@literal functions} is null or contains null
	 */
	protected ProductFunction(final ProductSet domain, ProductSet coDomain, Function... functions) {
		super(domain, coDomain, functions);
	}

	/**
	 * This is the general constructor of this class. The first parameter specifies the function to be applied multiple
	 * times in parallel, and the second parameter defines the number of times it is applied in parallel.
	 * <p>
	 * @param function The given function
	 * @param arity    The number of times the function is applied in parallel
	 * @throws IllegalArgumentException if {@literal function} is null
	 * @throws IllegalArgumentException if {@literal arity} is negative
	 */
	protected ProductFunction(ProductSet domain, ProductSet coDomain, Function function, int arity) {
		super(domain, coDomain, function, arity);
	}

	//
	// The following protected method implements the abstract method from {@code AbstractFunction}
	//
	@Override
	protected Tuple abstractApply(final Tuple element, final RandomNumberGenerator randomGenerator) {
		int arity = this.getArity();
		final Element[] elements = new Element[arity];
		for (int i = 0; i < arity; i++) {
			elements[i] = this.getAt(i).apply(element.getAt(i), randomGenerator);
		}
		return this.getCoDomain().getElement(elements);
	}

	@Override
	protected ProductFunction abstractGetInstance(Function[] functions) {
		return ProductFunction.getInstance(functions);
	}

	//
	// STATIC FACTORY METHODS
	//
	/**
	 * This is the general constructor of this class. It takes a list of functions as input and produces the
	 * corresponding product function.
	 * <p>
	 * @param functions
	 * @return
	 * @throws IllegalArgumentException if {@literal functions} is null or contains null
	 */
	public static ProductFunction getInstance(final Function... functions) {
		if (functions == null) {
			throw new IllegalArgumentException();
		}
		int arity = functions.length;
		final Set[] domains = new Set[arity];
		final Set[] coDomains = new Set[arity];
		if (functions.length > 0) {
			boolean uniform = true;
			Function first = functions[0];
			for (int i = 0; i < arity; i++) {
				if (functions[i] == null) {
					throw new IllegalArgumentException();
				}
				domains[i] = functions[i].getDomain();
				coDomains[i] = functions[i].getCoDomain();
				if (!functions[i].isEquivalent(first)) {
					uniform = false;
				}
			}
			if (uniform) {
				return ProductFunction.getInstance(first, arity);
			}
		}
		return new ProductFunction(ProductSet.getInstance(domains), ProductSet.getInstance(coDomains), functions);
	}

	/**
	 * This is the general constructor of this class. The first parameter specifies the function to be applied multiple
	 * times in parallel, and the second parameter defines the number of times it is applied in parallel.
	 * <p>
	 * @param function The given function
	 * @param arity    The number of times the function is applied in parallel
	 * @return
	 * @throws IllegalArgumentException if {@literal function} is null
	 * @throws IllegalArgumentException if {@literal arity} is negative
	 */
	public static ProductFunction getInstance(final Function function, final int arity) {
		if (function == null || arity < 0) {
			throw new IllegalArgumentException();
		}
		if (arity == 0) {
			return new ProductFunction(ProductSet.getInstance(), ProductSet.getInstance());
		}
		return new ProductFunction(ProductSet.getInstance(function.getDomain(), arity), ProductSet.getInstance(function.getCoDomain(), arity), function, arity);
	}

}
