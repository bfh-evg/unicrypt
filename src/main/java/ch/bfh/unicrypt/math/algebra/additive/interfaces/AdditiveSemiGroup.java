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
package ch.bfh.unicrypt.math.algebra.additive.interfaces;

import ch.bfh.unicrypt.UniCryptException;
import ch.bfh.unicrypt.helper.aggregator.interfaces.Aggregator;
import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.array.interfaces.ImmutableArray;
import ch.bfh.unicrypt.helper.converter.classes.ConvertMethod;
import ch.bfh.unicrypt.helper.converter.interfaces.Converter;
import ch.bfh.unicrypt.helper.random.RandomByteSequence;
import ch.bfh.unicrypt.helper.sequence.Sequence;
import ch.bfh.unicrypt.helper.tree.Tree;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import java.math.BigInteger;

/**
 * This interface provides the renaming of some methods for the case of a additively written commutative
 * {@link SemiGroup}. No functionality is added. Some return types are adjusted.
 * <p>
 * @param <V> The generic type of the values stored in the elements of this semigroup
 * <p>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public interface AdditiveSemiGroup<V>
	   extends SemiGroup<V> {

	/**
	 * This method is a synonym for {@link SemiGroup#apply(Element, Element)}. It computes the sum of the two input
	 * elements.
	 * <p>
	 * @param element1 The first input element
	 * @param element2 The second input element
	 * @return The sum of the two input elements
	 * @see SemiGroup#apply(Element, Element)
	 */
	public AdditiveElement<V> add(Element element1, Element element2);

	/**
	 * This method is a synonym for {@link SemiGroup#apply(Element...)}. It computes the sum of the input elements,
	 * which are given as an array.
	 * <p>
	 * @param elements The given array of input elements
	 * @return The sum of the input elements
	 * @see SemiGroup#apply(Element...)
	 */
	public AdditiveElement<V> add(Element... elements);

	/**
	 * This method is a synonym for {@link SemiGroup#apply(ImmutableArray)}. It computes the sum of the input elements,
	 * which are given as a immutable array.
	 * <p>
	 * @param elements The given immutable array of input elements
	 * @return The sum of the input elements
	 * @see SemiGroup#apply(ImmutableArray)
	 */
	public AdditiveElement<V> add(ImmutableArray<Element> elements);

	/**
	 * This method is a synonym for {@link SemiGroup#apply(Sequence)}. It computes the sum of the input elements, which
	 * are given as a sequence.
	 * <p>
	 * @param elements The given sequence of input elements
	 * @return The sum of the input elements
	 * @see SemiGroup#apply(Sequence)
	 */
	public AdditiveElement<V> add(Sequence<Element> elements);

	/**
	 * This method is a synonym for {@link SemiGroup#selfApply(Element, long)}. It multiplies the given element by some
	 * factor. This is a convenient method for {@link AdditiveSemiGroup#times(Element, BigInteger)}.
	 * <p>
	 * @param element The given input element
	 * @param factor  The given factor
	 * @return The input element multiplied by the factor
	 * @see SemiGroup#selfApply(Element, long)
	 */
	public AdditiveElement<V> times(Element element, long factor);

	/**
	 * This method is a synonym for {@link SemiGroup#selfApply(Element, BigInteger)}. It multiplies the given element by
	 * some factor.
	 * <p>
	 * @param element The given input element
	 * @param factor  The given factor
	 * @return The input element multiplied by the factor
	 * @see SemiGroup#selfApply(Element, BigInteger)
	 */
	public AdditiveElement<V> times(Element element, BigInteger factor);

	/**
	 * This method is a synonym for {@link SemiGroup#selfApply(Element, Element)} and the same as
	 * {@link AdditiveSemiGroup#times(Element, BigInteger)}, except that the factor is given as an instance of
	 * {@code Element<BigInteger>}, from which a {@code BigInteger} factor can be extracted using
	 * {@link Element#getValue()}.
	 * <p>
	 * @param element The given input element
	 * @param factor  The given factor
	 * @return The input element multiplied with the factor
	 * @see SemiGroup#selfApply(Element, Element)
	 */
	public AdditiveElement<V> times(Element element, Element<BigInteger> factor);

	/**
	 * This method is a synonym for {@link SemiGroup#selfApply(Element)}. It doubles the given input element.
	 * <p>
	 * @param element A given input element
	 * @return The given input element multiplied by 2
	 * @see SemiGroup#selfApply(Element)
	 */
	public AdditiveElement<V> timesTwo(Element element);

	/**
	 * This method is a synonym for {@link SemiGroup#multiSelfApply(Element[], BigInteger[])}. It computes the
	 * 'sum-of-products' for given input elements and factors. If the two input arrays are not of equal length, an
	 * exception is thrown.
	 * <p>
	 * @param elements A given array of elements
	 * @param factors  Corresponding factors
	 * @return The resulting 'sum-of-factors'
	 * @see SemiGroup#multiSelfApply(Element[], BigInteger[])
	 */
	public AdditiveElement<V> sumOfProducts(Element[] elements, BigInteger[] factors);

	@Override
	public <W> AdditiveElement<V> getElementFrom(W value, Converter<V, W> converter) throws UniCryptException;

	@Override
	public <W> AdditiveElement<V> getElementFrom(W value, ConvertMethod<W> convertMethod, Aggregator<W> aggregator)
		   throws UniCryptException;

	@Override
	public <W, X> AdditiveElement<V> getElementFrom(X value, ConvertMethod<W> convertMethod, Aggregator<W> aggregator,
		   Converter<W, X> finalConverter) throws UniCryptException;

	@Override
	public <W> AdditiveElement<V> getElementFrom(Tree<W> tree, ConvertMethod<W> convertMethod) throws UniCryptException;

	@Override
	public AdditiveElement<V> getElementFrom(long integer) throws UniCryptException;

	@Override
	public AdditiveElement<V> getElementFrom(BigInteger bigInteger) throws UniCryptException;

	@Override
	public AdditiveElement<V> getElementFrom(ByteArray byteArray) throws UniCryptException;

	@Override
	public AdditiveElement<V> getElementFrom(String string) throws UniCryptException;

	@Override
	public AdditiveElement<V> getRandomElement();

	@Override
	public AdditiveElement<V> getRandomElement(RandomByteSequence randomByteSequence);

	@Override
	public Sequence<? extends AdditiveElement<V>> getRandomElements();

	@Override
	public Sequence<? extends AdditiveElement<V>> getRandomElements(RandomByteSequence randomByteSequence);

	@Override
	public Sequence<? extends AdditiveElement<V>> getElements();

	@Override
	public AdditiveElement<V> apply(Element element1, Element element2);

	@Override
	public AdditiveElement<V> apply(Element... elements);

	@Override
	public AdditiveElement<V> apply(ImmutableArray<Element> elements);

	@Override
	public AdditiveElement<V> apply(Sequence<Element> elements);

	@Override
	public AdditiveElement<V> selfApply(Element element, long factor);

	@Override
	public AdditiveElement<V> selfApply(Element element, BigInteger factor);

	@Override
	public AdditiveElement<V> selfApply(Element element, Element<BigInteger> factor);

	@Override
	public AdditiveElement<V> selfApply(Element element);

	@Override
	public AdditiveElement<V> multiSelfApply(Element[] elements, BigInteger[] factors);

}
