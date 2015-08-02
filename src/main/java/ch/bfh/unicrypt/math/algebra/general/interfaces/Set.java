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

import ch.bfh.unicrypt.helper.aggregator.interfaces.Aggregator;
import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.converter.classes.ConvertMethod;
import ch.bfh.unicrypt.helper.converter.interfaces.Converter;
import ch.bfh.unicrypt.helper.sequence.Sequence;
import ch.bfh.unicrypt.helper.tree.Tree;
import ch.bfh.unicrypt.math.algebra.additive.interfaces.AdditiveSemiGroup;
import ch.bfh.unicrypt.math.algebra.concatenative.interfaces.ConcatenativeSemiGroup;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.Field;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.Ring;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.SemiRing;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.ZStarMod;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeSemiGroup;
import ch.bfh.unicrypt.random.interfaces.RandomByteSequence;
import java.math.BigInteger;

/**
 * This interface represents the concept of a non-empty mathematical set of elements. The number of elements in the set
 * is called order. The order may be infinite or unknown. The main functionality of this interface is the construction
 * of new elements, using {@link Set#getElement(Object)}, and checking whether a given element belongs to the set or
 * not, using {@link Set#contains(Element)}. Further functionalities such as applying set operations to elements are
 * added in sub-interfaces such as {@link Monoid}, {@link Group}, {@link Ring}, or {@link Field}.
 * <p>
 * Elements of a set are created by constructing instances of {@link Element}{@code <V>}. To construct a new element, an
 * object of the generic type {@code V} representing the element needs to be provided. This object is called the
 * element's value. The concrete type of these values depends on the actual set, to which the element belongs to. The
 * most common types are {@link BigInteger}, {@link String}, and {@link ByteArray}.
 * <p>
 * Independently of the generic type {@code V} of a set, it is assumed that each element of the set can be converted
 * into unique {@link BigInteger}, {@link String}, or {@link ByteArray} values, and that corresponding elements can be
 * constructed from such {@link BigInteger}, {@link String}, or {@link ByteArray} values. This is the recommended way of
 * converting the mathematical objects of the library into common Java objects.
 * <p>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 * @param <V> Generic type of the values representing the elements of a set
 * @see Element
 */
public interface Set<V extends Object> {

	/**
	 * A constant value representing an infinite order.
	 */
	public static final BigInteger INFINITE = BigInteger.valueOf(-1);

	/**
	 * A constant value representing an unknown order.
	 */
	public static final BigInteger UNKNOWN = BigInteger.valueOf(-2);

	/**
	 * Returns {@code true}, if this set is an instance of {@link SemiGroup}.
	 * <p>
	 * @return {@code true}, if this set is a semigroup, {@code false} otherwise
	 */
	public boolean isSemiGroup();

	/**
	 * Returns {@code true}, if this set is an instance of {@link Monoid}.
	 * <p>
	 * @return {@code true}, if this set is a monoid, {@code false} otherwise
	 */
	public boolean isMonoid();

	/**
	 * Returns {@code true}, if this set is an instance of {@link Group}.
	 * <p>
	 * @return {@code true}, if this set is a group, {@code false} otherwise
	 */
	public boolean isGroup();

	/**
	 * Returns {@code true}, if this set is an instance of {@link SemiRing}.
	 * <p>
	 * @return {@code true}, if this set is a semiring, {@code false} otherwise
	 */
	public boolean isSemiRing();

	/**
	 * Returns {@code true}, if this set is an instance of {@link Ring}.
	 * <p>
	 * @return {@code true}, if this set is a ring, {@code false} otherwise
	 */
	public boolean isRing();

	/**
	 * Returns {@code true}, if this set is an instance of {@link Field}.
	 * <p>
	 * @return {@code true}, if this set is a field, {@code false} otherwise
	 */
	public boolean isField();

	/**
	 * Returns {@code true}, if this set is an instance of {@link CyclicGroup}.
	 * <p>
	 * @return {@code true}, if this set is a cyclic group, {@code false} otherwise
	 */
	public boolean isCyclic();

	/**
	 * Returns {@code true}, if this set is an instance of {@link AdditiveSemiGroup}.
	 * <p>
	 * @return {@code true}, if this set has an additive operator, {@code false} otherwise
	 */
	public boolean isAdditive();

	/**
	 * Returns {@code true}, if this set is an instance of {@link MultiplicativeSemiGroup}.
	 * <p>
	 * @return {@code true}, if this set has a multiplicative operator, {@code false} otherwise
	 */
	public boolean isMultiplicative();

	/**
	 * Returns {@code true}, if this set is an instance of {@link ConcatenativeSemiGroup}.
	 * <p>
	 * @return {@code true}, if this set has a concatenative operator, {@code false} otherwise
	 */
	public boolean isConcatenative();

	/**
	 * Returns {@code true}, if this set represents a Cartesian product of other sets.
	 * <p>
	 * @return {@code true}, if this set is a product set, {@code false} otherwise
	 */
	public boolean isProduct();

	/**
	 * Returns {@code true}, if this set is of finite order.
	 * <p>
	 * @return {@code true}, if this set is finite, {@code false} otherwise
	 * @see getOrder()
	 */
	public boolean isFinite();

	/**
	 * Returns {@code true}, if the order of this set is known.
	 * <p>
	 * @return {@code true}, if if the order is known, {@code false} otherwise
	 * @see getOrder()
	 */
	public boolean hasKnownOrder();

	/**
	 * Returns the set order. Since only non-empty sets are considered, the order is always greater than 0. If the set
	 * order is unknown, {@link #UNKNOWN} is returned. If the set order is infinite, {@link #INFINITE} is returned.
	 * <p>
	 * @see "Handbook of Applied Cryptography, Definition 2.163"
	 * @return The set order
	 * @see getOrderLowerBound()
	 * @see getOrderUpperBound()
	 */
	public BigInteger getOrder();

	/**
	 * Returns a lower bound for the set order in case the exact set order is unknown. The least return value is 1. If
	 * the exact set order is known (or infinite), the exact set order is returned.
	 * <p>
	 * @return A lower bound for the set order
	 * @see getOrder()
	 * @see getOrderUpperBound()
	 */
	public BigInteger getOrderLowerBound();

	/**
	 * Returns an upper bound for the set order in case the exact set order is unknown. The highest return value is
	 * {@link #INFINITE}. If the exact set order is known (or infinite), the exact set order is returned.
	 * <p>
	 * @return An upper bound for the set order
	 * @see getOrder()
	 * @see getOrderLowerBound()
	 */
	public BigInteger getOrderUpperBound();

	/**
	 * If this set is a Cartesian product of sets, the order of the smallest of its sets is returned. Otherwise, the
	 * result of calling this method is equivalent to {@link Set#getOrderLowerBound()}.
	 * <p>
	 * @return The minimal order of this set
	 * @see getOrderLowerBound()
	 */
	public BigInteger getMinimalOrder();

	/**
	 * Checks if the set is of order 1.
	 * <p>
	 * @return {@code true}, if the order is 1, {@code false} otherwise
	 */
	public boolean isSingleton();

	/**
	 * Returns the ring of integers modulo the set order, i.e., an instance of {@link ZMod} with the same order. For
	 * this to work, the set order must be finite and known. For sets of infinite or unknown order, an exception is
	 * thrown.
	 * <p>
	 * @return The resulting ring of integers modulo the set order
	 */
	public ZMod getZModOrder();

	/**
	 * Returns the multiplicative group of integers modulo the set order, i.e., an instance of {@link ZStarMod} of order
	 * {@code phi(n)}. For this to work, the set order must be finite and known. For sets of infinite or unknown order,
	 * an exception is thrown.
	 * <p>
	 * @return The resulting multiplicative group
	 */
	public ZStarMod getZStarModOrder();

	/**
	 * Checks if a given element belongs to this set.
	 * <p>
	 * @param element The given element
	 * @return {@code true}, if {@code element} belongs to this set, {@code false} otherwise
	 */
	public boolean contains(Element<?> element);

	/**
	 * Checks if a given value represents an element of this set. If this is the case, the value can be used to
	 * construct the element using {@link getElement(Object)}.
	 * <p>
	 * @param value The given value
	 * @return {@code true}, if the value represents an element of this set, {@code false} otherwise
	 * @see getElement(Object)
	 */
	public boolean contains(V value);

	/**
	 * Returns the corresponding element for a given value, if such an element exists.
	 * <p>
	 * @param value The given value
	 * @return The element represented by the value
	 * @see contains(Object)
	 */
	public Element<V> getElement(V value);

	/**
	 * Selects and returns a random set element using the library's default random byte sequence. For sets of finite
	 * order, the element is selected uniformly at random. For sets of infinite order, an exception is thrown.
	 * <p>
	 * @return A random element from the set
	 */
	public Element<V> getRandomElement();

	/**
	 * Selects and returns a random set element using a given random byte sequence. For sets of finite order, the
	 * element is selected uniformly at random. For sets of infinite order, an exception is thrown.
	 * <p>
	 * @param randomByteSequence The given random byte sequence
	 * @return A random element from the set
	 */
	public Element<V> getRandomElement(RandomByteSequence randomByteSequence);

	public Sequence<? extends Element<V>> getRandomElements();

	public Sequence<? extends Element<V>> getRandomElements(long n);

	public Sequence<? extends Element<V>> getRandomElements(RandomByteSequence randomByteSequence);

	public Sequence<? extends Element<V>> getRandomElements(long n, RandomByteSequence randomByteSequence);

	/**
	 * Checks if two sets are mathematically equivalent. In most cases, this is equivalent to testing two sets for
	 * equality using {@link Set#equals(java.lang.Object)}, but some mathematically equivalent sets are instances of
	 * different classes and therefore are not passing the standard Java equality test. Calling this method is the
	 * recommended way of checking the equality of two sets.
	 * <p>
	 * @param set The given Set
	 * @return {@code true}, if the two sets are mathematically equivalent, {@code false} otherwise
	 */
	public boolean isEquivalent(Set<?> set);

	/**
	 * Returns the sequence of all elements from this set. The size of this collection may be infinite. The order in
	 * which the elements appear in the sequence is unspecified.
	 * <p>
	 * @return The sequence of all elements
	 */
	public Sequence<? extends Element<V>> getElements();

	/**
	 * Returns a sequence of some elements from this set. The maximal number of elements in the sequence is given as
	 * parameter. Their order is unspecified.
	 * <p>
	 * @param n The maximal number of element in the resulting sequence
	 * @return A sequence of elements
	 */
	public Sequence<? extends Element<V>> getElements(long n);

	/**
	 * Returns the class of the values representing the elements of this set.
	 * <p>
	 * @return The corresponding class
	 */
	public Class<?> getValueClass();

	/**
	 *
	 * @param <W>
	 * @param value
	 * @param converter
	 * @return
	 */
	public <W> Element<V> getElementFrom(W value, Converter<V, W> converter);

	/**
	 *
	 * @param <W>
	 * @param value
	 * @param convertMethod
	 * @param aggregator
	 * @return
	 */
	public <W> Element<V> getElementFrom(W value, ConvertMethod<W> convertMethod, Aggregator<W> aggregator);

	/**
	 *
	 * @param <W>
	 * @param tree
	 * @param convertMethod
	 * @return
	 */
	public <W> Element<V> getElementFrom(Tree<W> tree, ConvertMethod<W> convertMethod);

	/**
	 * Creates and returns the element that corresponds to a given {@code int} value using the default conversion method
	 * (if one exists). Returns {@code null} if no such element exists.
	 * <p>
	 * @param value The given {@code int} value
	 * @return The corresponding element, or {@code null} if no such element exists
	 */
	public Element<V> getElementFrom(long value);

	/**
	 * Creates and returns the element that corresponds to a given {@code BigInteger} value using the default conversion
	 * method(if one exists). Returns {@code null} if no such element exists.
	 * <p>
	 * @param value The given {@code BigInteger} value
	 * @return The corresponding element, or {@code null} if no such element exists
	 */
	public Element<V> getElementFrom(BigInteger value);

	/**
	 * Creates and returns the element that corresponds to a given {@code ByteArray} value using the default conversion
	 * method(if one exists). Returns {@code null} if no such element exists.
	 * <p>
	 * @param value The given {@code ByteArray} value
	 * @return The corresponding element, or {@code null} if no such element exists
	 */
	public Element<V> getElementFrom(ByteArray value);

	/**
	 * Creates and returns the element that corresponds to a given {@code String} value using the default conversion
	 * method. Returns {@code null} if no such element exists.
	 * <p>
	 * @param value The given {@code String} value
	 * @return The corresponding element, or {@code null} if no such element exists
	 */
	public Element<V> getElementFrom(String value);

}
