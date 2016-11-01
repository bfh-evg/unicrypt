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

import ch.bfh.unicrypt.helper.aggregator.interfaces.Aggregator;
import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.converter.classes.ConvertMethod;
import ch.bfh.unicrypt.helper.converter.interfaces.Converter;
import ch.bfh.unicrypt.helper.hash.HashMethod;
import ch.bfh.unicrypt.helper.tree.Tree;
import ch.bfh.unicrypt.math.algebra.additive.interfaces.AdditiveElement;
import ch.bfh.unicrypt.math.algebra.concatenative.interfaces.ConcatenativeElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeElement;
import java.math.BigInteger;

/**
 * This interface represents the concept of an element of a mathematical set. Each instance of {@link Element} is linked
 * to a (unique) instance of {@link Set}. In case the same element is contained in more than one set, multiple instances
 * need to be created, one for each set membership. In other words, sets in UniCrypt are treated as being disjoint,
 * which is not true in a strict mathematical sense.
 * <p>
 * Internally, each element is represented by value of the generic type {@code V}. Elements are usually constructed by
 * specifying this value.
 * <p>
 * For improved convenience, several pairs of equivalent methods exist for {@link Set} and {@link Element} and for
 * corresponding sub-interfaces. This allows both set-oriented and element-oriented writing of code.
 * <p>
 * @param <V> The generic type of values stored in this element
 * @see Set
 * <p>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public interface Element<V> {

	/**
	 * Returns {@code true} if this element is an instance of {@link AdditiveElement}.
	 * <p>
	 * @return {@code true} if this element is an instance of {@link AdditiveElement}, {@code false} otherwise
	 */
	public boolean isAdditive();

	/**
	 * Returns {@code true} if this element is an instance of {@link MultiplicativeElement}.
	 * <p>
	 * @return {@code true} if this element is an instance of {@link MultiplicativeElement}, {@code false} otherwise
	 */
	public boolean isMultiplicative();

	/**
	 * Returns {@code true} if this element is an instance of {@link ConcatenativeElement}.
	 * <p>
	 * @return {@code true} if this element is an instance of {@link ConcatenativeElement}, {@code false} otherwise
	 */
	public boolean isConcatenative();

	/**
	 * Returns {@code true} if this element is an instance of {@link DualisticElement}.
	 * <p>
	 * @return {@code true} if this element is an instance of {@link DualisticElement}, {@code false} otherwise
	 */
	public boolean isDualistic();

	/**
	 * Returns {@code true} if this element is an instance of {@link Tuple}.
	 * <p>
	 * @return {@code true} if this element a tuple, {@code false} otherwise
	 */
	public boolean isTuple();

	/**
	 * Returns the unique {@link Set} to which this element belongs.
	 * <p>
	 * @return The element's set
	 */
	public Set<V> getSet();

	/**
	 * Returns the value of the generic type {@code V} that represents this element.
	 * <p>
	 * @return The value representing the element
	 */
	public V getValue();

	/**
	 * Converts the element into an object of type {@code W} using the given converter and applying it to the element's
	 * value. This method is the counter-part of the method {@link Set#getElementFrom(Object, Converter)}. It is mostly
	 * used to convert atomic elements, but it contains the conversion of tuples as a special case.
	 * <p>
	 * @param <W>       The type of the returned value
	 * @param converter The given converter
	 * @return The resulting value
	 * @see Set#getElementFrom(Object, Converter)
	 */
	public <W> W convertTo(Converter<V, W> converter);

	/**
	 * Converts the element into an object of type {@code W} using the given convert method and aggregator. The
	 * element's value is first converted in tree of type {@code W}, which is then aggregated into a single value. This
	 * method is the counter-part of the method {@link Set#getElementFrom(Object, ConvertMethod, Aggregator)}. It is
	 * mostly used to convert tuples, but it contains the conversion of atomic elements as a special case.
	 * <p>
	 * @param <W>           The type of the returned value
	 * @param convertMethod The given convert method
	 * @param aggregator    The given aggregator
	 * @return The resulting value
	 * @see Set#getElementFrom(Object, ConvertMethod, Aggregator)
	 */
	public <W> W convertTo(ConvertMethod<W> convertMethod, Aggregator<W> aggregator);

	/**
	 * Converts the element into an object of type {@code W} using the given convert method, aggregator, and final
	 * converter. The element's value is first converted in tree of type {@code X}, which is then aggregated into a
	 * single value of type {@code X}, which is finally converted into a value of type {@code W}. This method is the
	 * counter-part of the method {@link Set#getElementFrom(Object, ConvertMethod, Aggregator, Converter)}. It is mostly
	 * used to convert tuples, but it contains the conversion of atomic elements as a special case.
	 * <p>
	 * @param <W>            The type of the returned value
	 * @param <X>            The type of the intermediate value
	 * @param convertMethod  The given convert method
	 * @param aggregator     The given aggregator
	 * @param finalConverter The given final converter
	 * @return The resulting value
	 * @see Set#getElementFrom(Object, ConvertMethod, Aggregator, Converter)
	 */
	public <W, X> X convertTo(ConvertMethod<W> convertMethod, Aggregator<W> aggregator, Converter<W, X> finalConverter);

	/**
	 * Converts the element into a tree of type {@code W} using the given convert method. This method is the
	 * counter-part of the method {@link Set#getElementFrom(Tree, ConvertMethod)}. It is mostly used to convert tuples,
	 * but it contains the conversion of atomic elements as a special case.
	 * <p>
	 * @param <W>           The type of the returned tree
	 * @param convertMethod The given convert method
	 * @return The resulting tree
	 * @see Set#getElementFrom(Tree, ConvertMethod)
	 */
	public <W> Tree<W> convertTo(ConvertMethod<W> convertMethod);

	/**
	 * Converts the element into a BigInteger value using the set's default converter. This method is the counter-part
	 * of the method {@link Set#getElementFrom(BigInteger)}.
	 * <p>
	 * @return The resulting BigInteger value
	 * @see Set#getStringConverter()
	 * @see Set#getElementFrom(BigInteger)
	 * <p>
	 */
	public BigInteger convertToBigInteger();

	/**
	 * Converts the element into a byte array using the set's default converter. This method is the counter-part of the
	 * method {@link Set#getElementFrom(ByteArray)}.
	 * <p>
	 * @return The resulting byte array
	 * @see Set#getByteArrayConverter()
	 * @see Set#getElementFrom(ByteArray)
	 * <p>
	 */
	public ByteArray convertToByteArray();

	/**
	 * Converts the element into a string using the set's default converter. This method is the counter-part of the
	 * method {@link Set#getElementFrom(String)}.
	 * <p>
	 * @return The resulting byte array
	 * @see Set#getStringConverter()
	 * @see Set#getElementFrom(String)
	 * <p>
	 */
	public String convertToString();

	/**
	 * Computes the element's hash value using the default convert and hash methods.
	 * <p>
	 * @return The hash value of the element
	 */
	public ByteArray getHashValue();

	/**
	 * Computes the element's hash value using the given convert and hash methods.
	 * <p>
	 * @param <W>           The type of the intermediate tree
	 * @param convertMethod The given convert method
	 * @param hashMethod    The given hash method
	 * @return The hash value of the element
	 */
	public <W> ByteArray getHashValue(ConvertMethod<W> convertMethod, HashMethod<W> hashMethod);

	/**
	 * Checks if this element is mathematically equivalent to another element. For this, they need to belong to
	 * equivalent sets and their values must be equal.
	 * <p>
	 * @param element The other element
	 * @return {@code true} if the element is equivalent to the given element, {@code false} otherwise
	 */
	public boolean isEquivalent(Element element);

	/**
	 * This is a convenience method for {@link SemiGroup#apply(Element, Element)}. It applies the binary operation of a
	 * semigroup to this and the other element. Throws an exception if the method is called for an element not belonging
	 * to a semigroup.
	 * <p>
	 * @param element The second input element
	 * @return The result of applying the binary operation to the two elements
	 * @see SemiGroup#apply(Element, Element)
	 */
	public Element<V> apply(Element element);

	/**
	 * This is a convenience method for {@link SemiGroup#selfApply(Element, long)}. It applies the binary operation of a
	 * semigroup repeatedly to {@code amount} many instances of a the element. Throws an exception if the method is
	 * called for an element not belonging to a semigroup.
	 * <p>
	 * @param amount The number of instances of the element
	 * @return The result of applying the operation multiple times to the element
	 * @see SemiGroup#selfApply(Element, long)
	 */
	public Element<V> selfApply(long amount);

	/**
	 * This is a convenience method for {@link SemiGroup#selfApply(Element, BigInteger)}. It applies the binary
	 * operation of a semigroup repeatedly to {@code amount} many instances of a the element. Throws an exception if the
	 * method is called for an element not belonging to a semigroup.
	 * <p>
	 * @param amount The number of instances of the element
	 * @return The result of applying the operation multiple times to the element
	 * @see SemiGroup#selfApply(Element, BigInteger)
	 */
	public Element<V> selfApply(BigInteger amount);

	/**
	 * This is a convenience method for {@link SemiGroup#selfApply(Element, Element)}. It is the same as
	 * {@link Element#selfApply(BigInteger)}, except that the amount is given as an instance of
	 * {@code Element<BigInteger>}, from which a {@code BigInteger} value can be extracted using
	 * {@link Element#getValue()}.
	 * <p>
	 * @param amount The number of instances of the element
	 * @return The result of applying the operation multiple times to the element
	 * @see SemiGroup#selfApply(Element, Element)
	 */
	public Element<V> selfApply(Element<BigInteger> amount);

	/**
	 * This is a convenience method for {@link SemiGroup#selfApply(Element)} and equivalent to
	 * {@link Element#selfApply(long)} for {@code amount=2}. The group operation is applied to the element itself.
	 * Throws an exception if the method is called for an element not belonging to a semigroup.
	 * <p>
	 * @return The result of applying the operation to the element itself
	 * @see SemiGroup#selfApply(Element)
	 */
	public Element<V> selfApply();

	public Element<V> invertSelfApply(long amount);

	public Element<V> invertSelfApply(BigInteger amount);

	public Element<V> invertSelfApply(Element<BigInteger> amount);

	public Element<V> invertSelfApply();

	/**
	 * This is a convenience method for {@link Monoid#isIdentityElement(Element)}. Checks if the element is the monoid's
	 * identity element. Throws an exception if the method is called for an element not belonging to a monoid.
	 * <p>
	 * @return @code true} if this is the monoid's identity element, {@code false} otherwise
	 * @see Monoid#isIdentityElement(Element)
	 */
	public boolean isIdentity();

	/**
	 * This is a convenience method for {@link Group#invert(Element)}. Computes and returns the inverse of the element.
	 * Throws an exception if the method is called for an element not belonging to a group.
	 * <p>
	 * @return The inverse of this element
	 * @see Group#invert(Element)
	 */
	public Element<V> invert();

	/**
	 * This is a convenience method for {@link Group#applyInverse(Element, Element)}. Applies the binary operation to
	 * this and the inverse of the given element. Throws an exception if the method is called for an element not
	 * belonging to a group.
	 * <p>
	 * @param element The given element
	 * @return The result of applying the group operation to this element and the inverse of the given element
	 * @see Group#applyInverse(Element, Element)
	 */
	public Element<V> applyInverse(Element element);

	/**
	 * This is a convenience method for {@link CyclicGroup#isGenerator(Element)}. Checks if the element is a generator
	 * of the cyclic group. Throws an exception if the method is called for an element not belonging to a cyclic group.
	 * <p>
	 * @return {@code true} if the element is a generator, {@code false} otherwise
	 * @see CyclicGroup#isGenerator(Element)
	 */
	public boolean isGenerator();

}
