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
package ch.bfh.unicrypt.math.algebra.general.interfaces;

import ch.bfh.unicrypt.math.algebra.general.classes.FiniteByteArrayElement;
import ch.bfh.unicrypt.math.helper.HashMethod;
import java.math.BigInteger;

/**
 * This abstract class represents the concept an element in a mathematical group. It allows applying the group operation
 * and other methods from a {@link Group} in a convenient way. Most methods provided by {@link Element} have an
 * equivalent method in {@link Group}.
 * <p>
 * @see Group
 * <p>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public interface Element {

	public boolean isAdditive();

	public boolean isMultiplicative();

	public boolean isConcatenative();

	public boolean isDualistic();

	public boolean isTuple();

	/**
	 *
	 * @return
	 */
	public Set getSet();

	/**
	 * Returns the positive BigInteger value that corresponds the element.
	 * <p>
	 * @return The corresponding BigInteger value
	 */
	public Object getValue();

	public BigInteger getIntegerValue();

	public FiniteByteArrayElement getHashValue();

	public FiniteByteArrayElement getHashValue(HashMethod hashMethod);

	/**
	 * Checks if this element is mathematically equivalent to the given element. For this, they need to belong to the
	 * same set.
	 * <p>
	 * @param element
	 * @return
	 */
	public boolean isEquivalent(Element element);

	// The following methods are equivalent to corresponding Set methods
	//
	/**
	 * @see Group#apply(Element, Element)
	 */
	public Element apply(Element element);

	/**
	 * @see Group#applyInverse(Element, Element)
	 */
	public Element applyInverse(Element element);

	/**
	 * @see Group#selfApply(Element, BigInteger)
	 */
	public Element selfApply(BigInteger amount);

	/**
	 * @see Group#selfApply(Element, Element)
	 */
	public Element selfApply(Element amount);

	/**
	 * @see Group#selfApply(Element, int)
	 */
	public Element selfApply(int amount);

	/**
	 * @see Group#selfApply(Element)
	 */
	public Element selfApply();

	/**
	 * @see Group#invert(Element)
	 */
	public Element invert();

	/**
	 * @see Group#isIdentityElement(Element)
	 */
	public boolean isIdentity();

	/**
	 * @see CyclicGroup#isGenerator(Element)
	 */
	public boolean isGenerator();

}
