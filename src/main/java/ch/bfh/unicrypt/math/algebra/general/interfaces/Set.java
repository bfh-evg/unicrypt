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

import ch.bfh.unicrypt.crypto.random.interfaces.RandomByteSequence;
import ch.bfh.unicrypt.math.algebra.additive.interfaces.AdditiveSemiGroup;
import ch.bfh.unicrypt.math.algebra.concatenative.interfaces.ConcatenativeSemiGroup;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.Field;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.Ring;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.SemiRing;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.ZStarMod;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeSemiGroup;
import ch.bfh.unicrypt.math.helper.bytetree.ByteTree;
import ch.bfh.unicrypt.math.helper.compound.Compound;
import java.math.BigInteger;

/**
 * This interface represents the concept a non-empty mathematical set of elements. The number of elements in the set is
 * called order. The order may be infinite or unknown. It is assumed that each element of a set corresponds to a unique
 * BigInteger value. Therefore, the interface provides methods for converting elements into corresponding BigInteger
 * values and back.
 * <p>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public interface Set {

	/**
	 * A constant value that represents an infinite order.
	 */
	public static final BigInteger INFINITE_ORDER = BigInteger.valueOf(-1);
	/**
	 * A constant value that represents an unknown order.
	 */
	public static final BigInteger UNKNOWN_ORDER = BigInteger.valueOf(-2);

	/**
	 * Returns {@code true} if this set is an instance of {@link SemiGroup}.
	 * <p>
	 * @return {@code true} if this set is a semigroup
	 */
	public boolean isSemiGroup();

	/**
	 * Returns {@code true} if this set is an instance of {@link Monoid}.
	 * <p>
	 * @return {@code true} if this set is a monoid
	 */
	public boolean isMonoid();

	/**
	 * Returns {@code true} if this set is an instance of {@link Group}.
	 * <p>
	 * @return {@code true} if this set is a group
	 */
	public boolean isGroup();

	/**
	 * Returns {@code true} if this set is an instance of {@link SemiRing}.
	 * <p>
	 * @return {@code true} if this set is a semiring
	 */
	public boolean isSemiRing();

	/**
	 * Returns {@code true} if this set is an instance of {@link Ring}.
	 * <p>
	 * @return {@code true} if this set is a ring
	 */
	public boolean isRing();

	/**
	 * Returns {@code true} if this set is an instance of {@link Field}.
	 * <p>
	 * @return {@code true} if this set is a field
	 */
	public boolean isField();

	/**
	 * Returns {@code true} if this set is an instance of {@link CyclicGroup}.
	 * <p>
	 * @return {@code true} if this set is cyclic
	 */
	public boolean isCyclic();

	/**
	 * Returns {@code true} if this set is an instance of {@link AdditiveSemiGroup}.
	 * <p>
	 * @return {@code true} if this set is additive
	 */
	public boolean isAdditive();

	/**
	 * Returns {@code true} if this set is an instance of {@link MultiplicativeSemiGroup}.
	 * <p>
	 * @return {@code true} if this set is multiplicative
	 */
	public boolean isMultiplicative();

	/**
	 * Returns {@code true} if this set is an instance of {@link ConcatenativeSemiGroup}.
	 * <p>
	 * @return {@code true} if this set is concatenative
	 */
	public boolean isConcatenative();

	/**
	 * TODO Returns {@code true} if this set is an instance of {@link Compound}. This set is a cartesian product
	 * <p>
	 * @return {@code true} if this set is compound
	 */
	public boolean isProduct();

	/**
	 * Returns {@code true} if this set is of finite order.
	 * <p>
	 * @return {@code true} if this set is finite
	 */
	public boolean isFinite();

	/**
	 * Returns {@code true} if this set has a known order.
	 * <p>
	 * @return {@code true} if this set has a known order
	 */
	public boolean hasKnownOrder();

	/**
	 * Returns the group order. If the group order is unknown, {@link #UNKNOWN_ORDER} is returned. If the group order is
	 * infinite, {@link #INFINITE_ORDER} is returned.
	 * <p>
	 * @see "Handbook of Applied Cryptography, Definition 2.163"
	 * @return The group order
	 */
	public BigInteger getOrder();

	/**
	 * TODO Returns a lower bound for the group (?)order in case the exact group order is unknown. The least return
	 * value is 0 (?). Otherwise, if the exact group order is known (or infinite), the exact group order is returned.
	 * <p>
	 * @return A lower bound for the group order
	 */
	public BigInteger getOrderLowerBound();

	/**
	 * TODO Returns an upper bound for the group (?) order in case the exact group order is unknown. The heighest return
	 * value is -1(?). Otherwise, if the exact group order is known (or infinite), the exact group order is returned.
	 * <p>
	 * @return A upper bound for the group order
	 */
	public BigInteger getOrderUpperBound();

	/**
	 * TODO Returns the minimal order of this set(?). entweder order oder min. coumpound (recursive)
	 * <p>
	 * @return The minimal order of this set
	 */
	public BigInteger getMinimalOrder();

	/**
	 * Checks if the set is of order 1.
	 * <p>
	 * @return {@code true} if the order is 1
	 */
	public boolean isSingleton();

	/**
	 * Returns an additive integer group of type {@link ZPlusMod} with the same group order. For this to work, the group
	 * order must be finite and known.
	 * <p>
	 * @return The resulting additive group.
	 * @throws UnsupportedOperationException if the group order is infinite or unknown
	 */
	public ZMod getZModOrder();

	/**
	 * Returns an multiplicative integer group of type {@link ZTimesMod} with the same group order. For this to work,
	 * the group order must be finite and known. TODO teilerfremd
	 * <p>
	 * @return The resulting multiplicative group.
	 * @throws UnsupportedOperationException if the group order is infinite or unknown
	 */
	public ZStarMod getZStarModOrder();

	/**
	 * Checks if this set contains an element that corresponds to a given integer value.
	 * <p>
	 * @param integer The given integer value
	 * @return {@code true} if such an element exists
	 */
	public boolean contains(int integer);

	/**
	 * Checks if this set contains an element that corresponds to a given BigInteger value.
	 * <p>
	 * @param bigInteger The given BigInteger value
	 * @return {@code true} if such an element exists
	 * @throws IllegalArgumentException if {@literal value} is null
	 */
	public boolean contains(BigInteger bigInteger);

	public boolean contains(ByteTree byteTree);

	public boolean contains(Object value);

	/**
	 * Checks if a given element belongs to the group.
	 * <p>
	 * @param element The given element
	 * @return {@code true} if {@literal element} belongs to the group
	 * @throws IllegalArgumentException if {@literal element} is null
	 */
	public boolean contains(Element element);

	/**
	 * Creates and returns the element that corresponds to a given integer (if one exists).
	 * <p>
	 * @param integer The given integer
	 * @return The corresponding element
	 * @throws IllegalArgumentException if no such element exists
	 */
	public Element getElement(int integer);

	/**
	 * Creates and returns the element that corresponds to a given BigInteger value (if one exists).
	 * <p>
	 * @param bigInteger The given BigInteger value
	 * @return The corresponding group element
	 * @throws IllegalArgumentException if {@literal value} is null or if no such element exists in this group
	 */
	public Element getElement(BigInteger bigInteger);

	public Element getElement(ByteTree byteTree);

	public Element getElement(Object value);

	/**
	 * Creates and returns the element that corresponds to the integer value of or some other group element (if one
	 * exists).
	 * <p>
	 * @param element The given group element
	 * @return The corresponding group element of this set
	 * @throws IllegalArgumentException if {@literal element} is null or if no such element exists in this group
	 */
	public Element getElement(Element element);

	/**
	 * Selects and returns a random group element using the default random generator. For finite order group, it is
	 * selected uniformly at random. For groups of infinite or unknown order, the underlying probability distribution is
	 * not further specified.
	 * <p>
	 * @return A random element from the set
	 */
	public Element getRandomElement();

	/**
	 * Selects and returns a random group element using a given random generator. If no random generator is specified,
	 * i.e., if {@literal random} is null, then the system-wide random generator is taken. For finite order group, it is
	 * selected uniformly at random. For groups of infinite or unknown order, the underlying probability distribution is
	 * not generally specified.
	 * <p>
	 * @param randomByteSequence Either {@literal null} or a given random generator
	 * @return A random element from the set
	 */
	public Element getRandomElement(RandomByteSequence randomByteSequence);

	/**
	 * Checks if two given elements of this group are equal.
	 * <p>
	 * @param element1 The first element
	 * @param element2 The second element
	 * @return {@code true} if the elements are equal and belong to the group
	 * @throws IllegalArgumentException if {@literal element1} or {@literal element2} is null
	 */
	public boolean areEquivalent(Element element1, Element element2);

	/**
	 * TODO
	 * <p>
	 * @param set
	 * @return
	 */
	public boolean isEquivalent(Set set);

}
