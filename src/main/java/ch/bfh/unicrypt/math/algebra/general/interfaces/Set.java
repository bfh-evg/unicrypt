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
package ch.bfh.unicrypt.math.algebra.general.interfaces;

import ch.bfh.unicrypt.UniCryptException;
import ch.bfh.unicrypt.helper.aggregator.interfaces.Aggregator;
import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.converter.classes.ConvertMethod;
import ch.bfh.unicrypt.helper.converter.interfaces.Converter;
import ch.bfh.unicrypt.helper.random.RandomByteSequence;
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
 * element's value. The concrete type of these values depends on the actual set to which the element belongs. The most
 * common types are {@link BigInteger}, {@link String}, and {@link ByteArray}.
 * <p>
 * Independently of the generic type {@code V} of a set, it is assumed that each element of the set can be converted
 * into unique {@link BigInteger}, {@link String}, or {@link ByteArray} values, and that corresponding elements can be
 * constructed from such {@link BigInteger}, {@link String}, or {@link ByteArray} values. This is the recommended way of
 * converting the mathematical objects of the library into common Java objects.
 * <p>
 * @param <V> The generic type of the values representing the elements of a set
 * @see Element
 * <p>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public interface Set<V> {

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
	 * @see #getOrder()
	 */
	public boolean isFinite();

	/**
	 * Returns {@code true}, if the order of this set is known.
	 * <p>
	 * @return {@code true}, if if the order is known, {@code false} otherwise
	 * @see #getOrder()
	 */
	public boolean hasKnownOrder();

	/**
	 * Returns the set order. Since only non-empty sets are considered, the order is always greater or equals to 1. If
	 * the set order is unknown, {@link #UNKNOWN} is returned. If the set order is infinite, {@link #INFINITE} is
	 * returned.
	 * <p>
	 * @return The set order
	 * @see "Handbook of Applied Cryptography, Definition 2.163"
	 * @see #getOrderLowerBound()
	 * @see #getOrderUpperBound()
	 */
	public BigInteger getOrder();

	/**
	 * Returns a lower bound for the set order in case the exact set order is unknown. The smallest possible returned
	 * value is 1. If the exact set order is known (or infinite), the exact set order is returned.
	 * <p>
	 * @return A lower bound for the set order
	 * @see #getOrder()
	 * @see #getOrderUpperBound()
	 */
	public BigInteger getOrderLowerBound();

	/**
	 * Returns an upper bound for the set order in case the exact set order is unknown. The highest possible return
	 * value is {@link #INFINITE}. If the exact set order is known (or infinite), the exact set order is returned.
	 * <p>
	 * @return An upper bound for the set order
	 * @see #getOrder()
	 * @see #getOrderLowerBound()
	 */
	public BigInteger getOrderUpperBound();

	/**
	 * If this set is a Cartesian product of sets, the order of the smallest of its sets is returned. Otherwise, the
	 * result of calling this method is equivalent to {@link Set#getOrderLowerBound()}.
	 * <p>
	 * @return The minimal order of this set getOrderLowerBound()
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
	 * construct the element using {@link #getElement(Object)}.
	 * <p>
	 * @param value The given value
	 * @return {@code true}, if the value represents an element of this set, {@code false} otherwise getElement(Object)
	 */
	public boolean contains(V value);

	/**
	 * Returns the corresponding element for a given value, if such an element exists. Otherwise, an exception is
	 * thrown.
	 * <p>
	 * @param value The given value
	 * @return The element represented by the value
	 * @see #contains(Object)
	 */
	public Element<V> getElement(V value);

	/**
	 * Selects and returns a random set element using the library's default random byte sequence. For sets of finite
	 * order, the element is selected uniformly at random. For sets of infinite order, an exception is thrown.
	 * <p>
	 * @return A random element
	 */
	public Element<V> getRandomElement();

	/**
	 * Selects and returns a random set element using a given random byte sequence. For sets of finite order, the
	 * element is selected uniformly at random. For sets of infinite order, an exception is thrown.
	 * <p>
	 * @param randomByteSequence The given random byte sequence
	 * @return A random element
	 */
	public Element<V> getRandomElement(RandomByteSequence randomByteSequence);

	/**
	 * Returns a sequence of random elements using the library's default random byte sequence. For sets of finite order,
	 * the elements are selected uniformly at random. For sets of infinite order, an exception is thrown.
	 * <p>
	 * @return A sequence of random elements
	 */
	public Sequence<? extends Element<V>> getRandomElements();

	/**
	 * Returns a sequence of random elements using a given random byte sequence. For sets of finite order, the elements
	 * are selected uniformly at random. For sets of infinite order, an exception is thrown.
	 * <p>
	 * @param randomByteSequence The given random byte sequence
	 * @return A sequence of random elements
	 */
	public Sequence<? extends Element<V>> getRandomElements(RandomByteSequence randomByteSequence);

	/**
	 * Returns the sequence of all elements from this set. The size of the sequence may be infinite. The order in which
	 * the elements appear in the sequence is unspecified.
	 * <p>
	 * @return The sequence of all elements
	 */
	public Sequence<? extends Element<V>> getElements();

	/**
	 * Checks if two sets are mathematically equivalent. In most cases, this is equivalent to testing two sets for
	 * equality using {@link Object#equals(Object)}, but some mathematically equivalent sets are instances of different
	 * classes and therefore are not passing the standard Java equality test. Calling this method is the recommended way
	 * of checking the equality of two sets.
	 * <p>
	 * @param set The given Set
	 * @return {@code true}, if the two sets are mathematically equivalent, {@code false} otherwise
	 */
	public boolean isEquivalent(Set<?> set);

	/**
	 * Returns the class of the values representing the elements of this set.
	 * <p>
	 * @return The corresponding class
	 */
	public Class<?> getValueClass();

	/**
	 * This method is the counter-part of the method {@link Element#convertTo(Converter)}. It re-constructs an element
	 * of type {@code V} from a given value of type {@code W}. First, the given converter is used to re-constructs a
	 * value of type {@code V}, and this value is then used to re-construct the element. A checked exception is thrown
	 * if the conversion fails. This method is mostly used to re-construct atomic elements, but it contains the
	 * conversion of tuples as a special case.
	 * <p>
	 * @param <W>       The type of the given value
	 * @param value     The given value
	 * @param converter The given converter
	 * @return The re-constructed element
	 * @throws ch.bfh.unicrypt.UniCryptException if no such element exists
	 * @see Element#convertTo(Converter)
	 */
	public <W> Element<V> getElementFrom(W value, Converter<V, W> converter) throws UniCryptException;

	/**
	 * This method is the counter-part of the method {@link Element#convertTo(ConvertMethod, Aggregator)}. It
	 * re-constructs an element of type {@code V} from a given value of type {@code W}. First, the given aggregator is
	 * used to re-construct a tree of type {@code W}. This tree together with the given convert method is then used to
	 * re-construct the element. A checked exception is thrown if the conversion fails. This method is mostly used to
	 * re-construct tuples, but it contains the conversion of atomic elements as a special case.
	 * <p>
	 * <p>
	 * @param <W>           The type of the given value
	 * @param value         The given value
	 * @param convertMethod The given convert method
	 * @param aggregator    The given aggregator
	 * @return The re-constructed element
	 * @throws ch.bfh.unicrypt.UniCryptException if no such element exists
	 * @see Element#convertTo(ConvertMethod, Aggregator)
	 */
	public <W> Element<V> getElementFrom(W value, ConvertMethod<W> convertMethod, Aggregator<W> aggregator) throws
		   UniCryptException;

	/**
	 * This is the most general method for re-constructing elements of type {@code V} from a value of type {@code W}. It
	 * is the counter-part of the method {@link Element#convertTo(ConvertMethod, Aggregator, Converter)}. First, the
	 * value is converted into another value of an intermediate type {@code X}. Using the given aggregator, this value
	 * is then converted into a tree of type {@code X}. Finally, this tree together with the given convert method is
	 * used to re-construct the element. A checked exception is thrown if the conversion fails. This method is mostly
	 * used to re-construct tuples, but it contains the conversion of atomic elements as a special case.
	 * <p>
	 * @param <W>            The type of the given value
	 * @param <X>            The type of the intermediate value
	 * @param value	         The given value
	 * @param convertMethod  The given convert method
	 * @param aggregator     The given aggregator
	 * @param finalConverter The given converter
	 * @return The re-constructed element
	 * @throws ch.bfh.unicrypt.UniCryptException if no such element exists
	 * @see Element#convertTo(ConvertMethod, Aggregator, Converter)
	 */
	public <W, X> Element<V> getElementFrom(X value, ConvertMethod<W> convertMethod, Aggregator<W> aggregator,
		   Converter<W, X> finalConverter) throws UniCryptException;

	/**
	 * This method is the counter-part of the method {@link Element#convertTo(ConvertMethod)}. It can be used to convert
	 * a tree of type {@code W} into an element of type {@code V}, using the given convert method. A checked exception
	 * is thrown if the conversion fails.
	 * <p>
	 * @param <W>           The type of the given tree
	 * @param tree          The given tree
	 * @param convertMethod The given convert method
	 * @return The re-constructed element
	 * @throws ch.bfh.unicrypt.UniCryptException if no such element exists
	 * @see Element#convertTo(ConvertMethod)
	 */
	public <W> Element<V> getElementFrom(Tree<W> tree, ConvertMethod<W> convertMethod) throws UniCryptException;

	/**
	 * Re-constructs the element that corresponds to a given {@code long} value using the default converter. A checked
	 * exception is thrown if the conversion fails. This method is a convenient method for
	 * {@link Set#getElementFrom(BigInteger)}.
	 * <p>
	 * @param value The given {@code long} value
	 * @return The re-constructed element
	 * @throws ch.bfh.unicrypt.UniCryptException if no such element exists
	 */
	public Element<V> getElementFrom(long value) throws UniCryptException;

	/**
	 * Re-constructs the element that corresponds to a given {@code BigInteger} value using the default converter. A
	 * checked exception is thrown if the conversion fails.
	 * <p>
	 * @param value The given {@code BigInteger} value
	 * @return The re-constructed element
	 * @throws ch.bfh.unicrypt.UniCryptException if no such element exists
	 */
	public Element<V> getElementFrom(BigInteger value) throws UniCryptException;

	/**
	 * Re-constructs the element that corresponds to a given {@code ByteArray} value using the default converter. A
	 * checked exception is thrown if the conversion fails.
	 * <p>
	 * @param value The given {@code ByteArray} value
	 * @return The re-constructed element
	 * @throws ch.bfh.unicrypt.UniCryptException if no such element exists
	 */
	public Element<V> getElementFrom(ByteArray value) throws UniCryptException;

	/**
	 * CRe-constructs the element that corresponds to a given {@code String} value using the default conversion method.
	 * A checked exception is thrown if the conversion fails.
	 * <p>
	 * @param value The given {@code String} value
	 * @return The re-constructed element
	 * @throws ch.bfh.unicrypt.UniCryptException if no such element exists
	 */
	public Element<V> getElementFrom(String value) throws UniCryptException;

	/**
	 * Returns the default {@code BigInteger} converter.
	 * <p>
	 * @return The default converter
	 */
	public Converter<V, BigInteger> getBigIntegerConverter();

	/**
	 * Returns the default {@code ByteArray} converter.
	 * <p>
	 * @return The default converter
	 */
	public Converter<V, ByteArray> getByteArrayConverter();

	/**
	 * Returns the default {@code String} converter.
	 * <p>
	 * @return The default converter
	 */
	public Converter<V, String> getStringConverter();

}
