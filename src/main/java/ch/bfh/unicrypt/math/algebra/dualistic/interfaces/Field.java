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
package ch.bfh.unicrypt.math.algebra.dualistic.interfaces;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeGroup;
import java.math.BigInteger;

/**
 * This interface represents the mathematical concept of a field. A field is a commutative ring with the additional
 * property that the set of non-zero elements together with multiplication forms a second commutative group. Therefore,
 * both operations form a commutative group with corresponding additive and multiplicative inverses.
 * <p>
 * The field interface is implemented as a specialization of {@link Ring} with additional methods for returning the
 * multiplicative group and for computing multiplicative inverses and divisions.
 * <p>
 * @param <V> The generic type of the values representing the elements of a field
 * <p>
 * @author R. Haenni
 * <p>
 * @see "Handbook of Applied Cryptography, Definition 2.181"
 */
public interface Field<V>
	   extends Ring<V> {

	/**
	 * Returns the multiplicative group of this field. The set of the multiplicative group contains all the elements of
	 * the field except the zero element.
	 * <p>
	 * @return The multiplicative group of this field
	 */
	public MultiplicativeGroup<V> getMultiplicativeGroup();

	/**
	 * Returns the multiplicative inverse of the given element. Throws an exception if the given element is the zero
	 * element.
	 * <p>
	 * @param element The given element
	 * @return The multiplicative inverse of the given element
	 */
	public DualisticElement<V> oneOver(Element element);

	/**
	 * Divides the first element over the second element.
	 * <p>
	 * @param element1 The first given element
	 * @param element2 The second given element
	 * @return The first element divided over the second element
	 */
	public DualisticElement<V> divide(Element element1, Element element2);

	public DualisticElement<V> nthRoot(Element element, long n);

	public DualisticElement<V> nthRoot(Element element, Element<BigInteger> n);

	public DualisticElement<V> nthRoot(Element element, BigInteger n);

	public DualisticElement<V> squareRoot(Element element);

}
