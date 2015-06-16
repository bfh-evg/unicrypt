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

import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.bytetree.ByteTree;
import ch.bfh.unicrypt.helper.converter.classes.ConvertMethod;
import ch.bfh.unicrypt.helper.converter.interfaces.BigIntegerConverter;
import ch.bfh.unicrypt.helper.converter.interfaces.ByteArrayConverter;
import ch.bfh.unicrypt.helper.converter.interfaces.Converter;
import ch.bfh.unicrypt.helper.converter.interfaces.StringConverter;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.ZStarMod;
import ch.bfh.unicrypt.random.interfaces.RandomByteSequence;
import java.math.BigInteger;

/**
 * This interface represents the concept of a non-empty mathematical set of elements. The number of elements in the set
 * is called order. The order may be infinite or unknown. The main functionality of this interface is constructing new
 * elements, using {@link Set#getElement(Object)}, and checking whether a given element belongs to the set or not, using
 * {@link Set#contains(Element)}. Further functionalities such as operations between elements are added in
 * sub-interfaces such as {@link Monoid}, {@link Group}, {@link Ring}, or {@link Field}.
 * <p>
 * Elements of a set are created by constructing instances of {@link Element}{@code <V>}. To construct a new element, an
 * object of the generic type {@code V} representing the element needs to be provided. The object is called the
 * element's value. The concrete type of these values depends on the actual set, to which the element belongs to.
 * <p>
 * Independently of the generic type {@code V} of a set, it is assumed that each element of this set can be converted
 * into a unique {@link BigInteger}, {@link String}, or {@link ByteArray} value, and vice versa, that corresponding
 * elements can be constructed from such {@link BigInteger}, {@link String}, or {@link ByteArray} values. For this, each
 * set provides default converters (instances of {@link BigIntegerConverter}, {@link StringConverter}, and
 * {@link ByteArrayConverter}), which handle the conversion.
 * <p>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 * @param <V> Generic type of the values representing the elements of this set
 * @see Element
 */
public interface Set<V extends Object> {

	/**
	 * A constant value representing an infinite order.
	 */
	public static final BigInteger INFINITE_ORDER = BigInteger.valueOf(-1);

	/**
	 * A constant value representing an unknown order.
	 */
	public static final BigInteger UNKNOWN_ORDER = BigInteger.valueOf(-2);

	/**
	 * Returns {@literal true}, if this set is an instance of {@link SemiGroup}.
	 * <p>
	 * @return {@literal true}, if this set is a semigroup, {@literal false} otherwise
	 */
	public boolean isSemiGroup();

	/**
	 * Returns {@literal true}, if this set is an instance of {@link Monoid}.
	 * <p>
	 * @return {@literal true}, if this set is a monoid, {@literal false} otherwise
	 */
	public boolean isMonoid();

	/**
	 * Returns {@literal true}, if this set is an instance of {@link Group}.
	 * <p>
	 * @return {@literal true}, if this set is a group, {@literal false} otherwise
	 */
	public boolean isGroup();

	/**
	 * Returns {@literal true}, if this set is an instance of {@link SemiRing}.
	 * <p>
	 * @return {@literal true}, if this set is a semiring, {@literal false} otherwise
	 */
	public boolean isSemiRing();

	/**
	 * Returns {@literal true}, if this set is an instance of {@link Ring}.
	 * <p>
	 * @return {@literal true}, if this set is a ring, {@literal false} otherwise
	 */
	public boolean isRing();

	/**
	 * Returns {@literal true}, if this set is an instance of {@link Field}.
	 * <p>
	 * @return {@literal true}, if this set is a field, {@literal false} otherwise
	 */
	public boolean isField();

	/**
	 * Returns {@literal true}, if this set is an instance of {@link CyclicGroup}.
	 * <p>
	 * @return {@literal true}, if this set is a cyclic group, {@literal false} otherwise
	 */
	public boolean isCyclic();

	/**
	 * Returns {@literal true}, if this set is an instance of {@link AdditiveSemiGroup}.
	 * <p>
	 * @return {@literal true}, if this set has an additive operator, {@literal false} otherwise
	 */
	public boolean isAdditive();

	/**
	 * Returns {@literal true}, if this set is an instance of {@link MultiplicativeSemiGroup}.
	 * <p>
	 * @return {@literal true}, if this set has a multiplicative operator, {@literal false} otherwise
	 */
	public boolean isMultiplicative();

	/**
	 * Returns {@literal true}, if this set is an instance of {@link ConcatenativeSemiGroup}.
	 * <p>
	 * @return {@literal true}, if this set has a concatenative operator, {@literal false} otherwise
	 */
	public boolean isConcatenative();

	/**
	 * Returns {@literal true}, if this set is an instance of {@link ProductSet}, i.e., if this set is a Cartesian
	 * product of other sets.
	 * <p>
	 * @return {@literal true}, if this set is a product set, {@literal false} otherwise
	 */
	public boolean isProduct();

	/**
	 * Returns {@literal true}, if this set is of finite order.
	 * <p>
	 * @return {@literal true}, if this set is finite, {@literal false} otherwise
	 * @see getOrder()
	 */
	public boolean isFinite();

	/**
	 * Returns {@literal true}, if the order of this set is known.
	 * <p>
	 * @return {@literal true}, if if the order is known, {@literal false} otherwise
	 * @see getOrder()
	 */
	public boolean hasKnownOrder();

	/**
	 * Returns the set order. Since only non-empty sets are considered, the order is always greater than 0. If the set
	 * order is unknown, {@link #UNKNOWN_ORDER} is returned. If the set order is infinite, {@link #INFINITE_ORDER} is
	 * returned.
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
	 * {@link #INFINITE_ORDER}. If the exact set order is known (or infinite), the exact set order is returned.
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
	 * @return {@literal true}, if the order is 1, {@literal false} otherwise
	 */
	public boolean isSingleton();

	/**
	 * Returns the ring of integers modulo the set order, i.e., an instance of {@link ZMod} with the same set order. For
	 * this to work, the set order must be finite and known.
	 * <p>
	 * @return The resulting ring of integers modulo the set order
	 * @throws UnsupportedOperationException if the set order is infinite or unknown
	 */
	public ZMod getZModOrder();

	/**
	 * Returns the multiplicative group of integers modulo the set order, i.e., an instance of {@link ZStarMod} of order
	 * {@code phi(n)}. For this to work, the set order must be finite and known.
	 * <p>
	 * @return The resulting multiplicative group
	 * @throws UnsupportedOperationException if the set order is infinite or unknown
	 */
	public ZStarMod getZStarModOrder();

	/**
	 * Checks if a given element belongs to this set.
	 * <p>
	 * @param element The given element
	 * @return {@literal true}, if {@literal element} belongs to this set, {@literal false} otherwise
	 */
	public boolean contains(Element<?> element);

	/**
	 * Checks if a given value represents an element of this set. If this is the case, the value can be used to
	 * construct the element using {@link getElement(Object)}.
	 * <p>
	 * @param value The given value
	 * @return {@literal true}, if the value represents an element of this set, {@literal false} otherwise
	 * @see getElement(Object)
	 */
	public boolean contains(V value);

	/**
	 * Returns the corresponding element for a given value (if such an element exists).
	 * <p>
	 * @param value The given value
	 * @return The element represented by the value
	 * @throws IllegalArgumentException if the value does not represent an element of this set
	 * @see contains(Object)
	 */
	public Element<V> getElement(V value);

	/**
	 * Returns a random element using the library's default random byte sequence. For sets of finite order, the element
	 * is selected uniformly at random. For sets of infinite order, an {@link UnsupportedOperationException} is thrown.
	 * <p>
	 * @return A random element from the set
	 * @throws UnsupportedOperationException
	 */
	public Element<V> getRandomElement();

	/**
	 * Selects and returns a random set element using a given random byte sequence. For sets of finite order, the
	 * element is selected uniformly at random. For sets of infinite order, an {@link UnsupportedOperationException} is
	 * thrown.
	 * <p>
	 * @param randomByteSequence The given random byte sequence
	 * @return A random element from the set
	 * @throws UnsupportedOperationException
	 */
	public Element<V> getRandomElement(RandomByteSequence randomByteSequence);

	/**
	 * Checks if two sets are mathematically equivalent.
	 * <p>
	 * @true} if this set is equal to a given Set.
	 * <p>
	 * @param set The given Set.
	 * @return {@literal true} if this set is equal
	 */
	public boolean isEquivalent(Set<?> set);

	/**
	 * Returns an iterable collection of all elements from this set. The size of this collection may be infinite. The
	 * order in which the elements appear in the iterable collection is unspecified.
	 * <p>
	 * @return An iterable collection
	 */
	public Iterable<? extends Element<V>> getElements();

	/**
	 * Returns an iterable collection of some elements from this set. The number of element in the iterable collection
	 * is given as parameter. Their order is unspecified.
	 * <p>
	 * @param n The number of element in the resulting iterable collection
	 * @return An iterable collection
	 */
	public Iterable<? extends Element<V>> getElements(int n);

	/**
	 * Returns the class of the values representing the elements of this set.
	 * <p>
	 * @return The corresponding class
	 */
	public Class<?> getValueClass();

	/**
	 * Returns the default {@link BigIntegerConverter} of this class. It is used to convert elements of this class into
	 * {@link BigInteger} and, vice versa, to construct elements of this class from {@link BigInteger} values.
	 * <p>
	 * @return The default {@link BigInteger} converter
	 * @see Set#getElementFrom(BigInteger)
	 * @see Element#getBigInteger()
	 */
	public Converter<V, BigInteger> getBigIntegerConverter();

	/**
	 * Returns the default {@link StringConverter} of this class. It is used to convert elements of this class into
	 * {@link String} and, vice versa, to construct elements of this class from {@link String} values.
	 * <p>
	 * @return The default {@link String} converter
	 * @see Set#getElementFrom(String)
	 * @see Element#getString()
	 */
	public Converter<V, String> getStringConverter();

	/**
	 * Returns the default {@link ByteArrayConverter} of this class. It is used to convert elements of this class into
	 * {@link ByteArray} and, vice versa, to construct elements of this class from {@link ByteArray} values.
	 * <p>
	 * @return The default {@link ByteArray} converter
	 * @see Set#getElementFrom(ByteArray)
	 * @see Element#getByteArray()
	 */
	public Converter<V, ByteArray> getByteArrayConverter();

	/**
	 * Creates and returns the element that corresponds to a given integer (if one exists). Returns {@literal null}
	 * otherwise.
	 * <p>
	 * @param integer The given integer value
	 * @return The corresponding element, or {@literal null} if no such element exists
	 */
	public Element<V> getElementFrom(int integer);

	/**
	 * Creates and returns the element that corresponds to a given BigInteger value (if one exists). Returns
	 * {@literal null} otherwise.
	 * <p>
	 * @param bigInteger The given BigInteger value
	 * @return The corresponding element, or {@literal null} if no such element exists
	 */
	public Element<V> getElementFrom(BigInteger bigInteger);

	/**
	 *
	 * @param bigInteger
	 * @param converter
	 * @return
	 */
	public Element<V> getElementFrom(BigInteger bigInteger, Converter<V, BigInteger> converter);

	/**
	 *
	 * @param bigInteger
	 * @param convertMethod
	 * @return
	 */
	public Element<V> getElementFrom(BigInteger bigInteger, ConvertMethod<BigInteger> convertMethod);

	/**
	 *
	 * @param string
	 * @return
	 */
	public Element<V> getElementFrom(String string);

	/**
	 *
	 * @param string
	 * @param converter
	 * @return
	 */
	public Element<V> getElementFrom(String string, Converter<V, String> converter);

	/**
	 *
	 * @param string
	 * @param convertMethod
	 * @return
	 */
	public Element<V> getElementFrom(String string, ConvertMethod<String> convertMethod);

	/**
	 * TODO Returns the corresponding {@link Element} for the given {@link ByteArray} using the default converter.
	 * <p>
	 * @param byteArray The given ByteArray
	 * @return the corresponding element
	 */
	public Element<V> getElementFrom(ByteArray byteArray);

	/**
	 *
	 * @param byteArray
	 * @param converter
	 * @return
	 */
	public Element<V> getElementFrom(ByteArray byteArray, Converter<V, ByteArray> converter);

	/**
	 * TODO Returns the corresponding {@link Element} for the given {@link ByteArray} using the
	 * {@link BigIntegerToByteArray}.
	 * <p>
	 * @param byteArray
	 * @param convertMethod
	 * @return
	 */
	public Element<V> getElementFrom(ByteArray byteArray, ConvertMethod<ByteArray> convertMethod);

	/**
	 * TODO Returns the corresponding {@link Element} for the given {@link ByteTree}.
	 * <p>
	 * @param byteTree The given ByteTree
	 * @return the corresponding element
	 */
	public Element<V> getElementFrom(ByteTree byteTree);

	/**
	 *
	 * @param byteTree
	 * @param converter
	 * @return
	 */
	public Element<V> getElementFrom(ByteTree byteTree, Converter<V, ByteArray> converter);

	/**
	 * <p>
	 * <p>
	 * @param byteTree
	 * @param convertMethod
	 * @return
	 */
	public Element<V> getElementFrom(ByteTree byteTree, ConvertMethod<ByteArray> convertMethod);

}
