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
package ch.bfh.unicrypt.math.algebra.concatenative.interfaces;

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
 * This interface provides the renaming of some methods for a (non-commutative) {@link SemiGroup} with concatenation as
 * binary operation. Elements of such semigroups have a length. In some concatenative semigroups, the length of the
 * elements is restricted to a multiple of a fixed block length. The default block length is 1. Some additional methods
 * are added to deal with the length of the elements and the block length of the semigroup. Some return types are
 * adjusted.
 * <p>
 * @param <V> The generic type of the values stored in the elements of this semigroup
 * <p>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public interface ConcatenativeSemiGroup<V>
	   extends SemiGroup<V> {

	/**
	 * Returns the block length of this concatenative semigroup.
	 * <p>
	 * @return The block length of this concatenative semigroup
	 */
	public int getBlockLength();

	/**
	 * Selects and returns a random semiring element of a given length. The element is selected uniformly at random
	 * using the library's default random byte sequence.
	 * <p>
	 * @param length The given length
	 * @return A random element of the given length
	 */
	public ConcatenativeElement<V> getRandomElement(int length);

	/**
	 * Selects and returns a random semiring element of a given length. The element is selected uniformly at random
	 * using a given random byte sequence.
	 * <p>
	 * @param length             The given length
	 * @param randomByteSequence The given random byte sequence
	 * @return A random element of the given length
	 */
	public ConcatenativeElement<V> getRandomElement(int length, RandomByteSequence randomByteSequence);

	/**
	 * This method is a synonym for {@link SemiGroup#apply(Element, Element)}. It concatenates the two input elements in
	 * the given order .
	 * <p>
	 * @param element1 The first input element
	 * @param element2 The second input element
	 * @return The concatenation of the two input elements
	 * @see SemiGroup#apply(Element, Element)
	 */
	public ConcatenativeElement<V> concatenate(Element element1, Element element2);

	/**
	 * This method is a synonym for {@link SemiGroup#apply(Element...)}. It concatenates the input elements in the given
	 * order from left to right. The input elements are given as an array.
	 * <p>
	 * @param elements The given array of input elements
	 * @return The concatenation of the input elements
	 * @see SemiGroup#apply(Element...)
	 */
	public ConcatenativeElement<V> concatenate(Element... elements);

	/**
	 * This method is a synonym for {@link SemiGroup#apply(ImmutableArray)}. It concatenates the input elements in the
	 * given order from left to right. The input elements are given as a immutable array.
	 * <p>
	 * @param elements The given array of input elements
	 * @return The concatenation of the input elements
	 * @see SemiGroup#apply(ImmutableArray)
	 */
	public ConcatenativeElement<V> concatenate(ImmutableArray<Element> elements);

	/**
	 * This method is a synonym for {@link SemiGroup#apply(Sequence)}. It concatenates the input elements in the given
	 * order from left to right. The input elements are given as a sequence.
	 * <p>
	 * @param elements The given array of input elements
	 * @return The concatenation of the input elements
	 * @see SemiGroup#apply(Sequence)
	 */
	public ConcatenativeElement<V> concatenate(Sequence<Element> elements);

	/**
	 * Applies the concatenation operation repeatedly to {@code amount} many instances of a given input element. This is
	 * a convenient method for {@link ConcatenativeSemiGroup#selfConcatenate(Element, BigInteger)}.
	 * <p>
	 * @param element The given input element
	 * @param amount  The number of instances of the input element
	 * @return The result of applying concatenation multiple times to the input element
	 */
	public ConcatenativeElement<V> selfConcatenate(Element element, long amount);

	/**
	 * Applies the concatenation operation repeatedly to {@code amount} many instances of a given input element.
	 * <p>
	 * @param element The given input element
	 * @param amount  The number of instances of the input element
	 * @return The result of applying concatenation multiple times to the input element
	 */
	public ConcatenativeElement<V> selfConcatenate(Element element, BigInteger amount);

	/**
	 * Same as {@link ConcatenativeSemiGroup#selfConcatenate(Element, BigInteger)}, except that the amount is given as
	 * an instance of {@code Element<BigInteger>}, from which a {@code BigInteger} value can be extracted using
	 * {@link Element#getValue()}.
	 * <p>
	 * @param element The given input element
	 * @param amount  The number of instances of the input element
	 * @return The result of applying concatenation multiple times to the input element
	 */
	public ConcatenativeElement<V> selfConcatenate(Element element, Element<BigInteger> amount);

	/**
	 * Applies the concatenation operation to two instances of a given element. This is equivalent to
	 * {@link ConcatenativeSemiGroup#selfConcatenate(Element, long)} for {@code amount=2}.
	 * <p>
	 * @param element The given input element
	 * @return The result of applying the concatenation operation to two instances of the input element
	 */
	public ConcatenativeElement<V> selfConcatenate(Element element);

	/**
	 * Applies the concatenation operation sequentially to the results of computing
	 * {@link #selfConcatenate(Element, BigInteger)} multiple times. If the two input arrays are not of equal length, an
	 * exception is thrown.
	 * <p>
	 * @param elements The given array of elements
	 * @param amounts  Corresponding amounts
	 * @return The result of this operation
	 */
	public ConcatenativeElement<V> multiSelfConcatenate(Element[] elements, BigInteger[] amounts);

	@Override
	public <W> ConcatenativeElement<V> getElementFrom(W value, Converter<V, W> converter) throws UniCryptException;

	@Override
	public <W> ConcatenativeElement<V> getElementFrom(W value, ConvertMethod<W> convertMethod, Aggregator<W> aggregator)
		   throws UniCryptException;

	@Override
	public <W, X> ConcatenativeElement<V> getElementFrom(X value, ConvertMethod<W> convertMethod,
		   Aggregator<W> aggregator, Converter<W, X> finalConverter) throws UniCryptException;

	@Override
	public <W> ConcatenativeElement<V> getElementFrom(Tree<W> tree, ConvertMethod<W> convertMethod) throws
		   UniCryptException;

	@Override
	public ConcatenativeElement<V> getElementFrom(long integer) throws UniCryptException;

	@Override
	public ConcatenativeElement<V> getElementFrom(BigInteger bigInteger) throws UniCryptException;

	@Override
	public ConcatenativeElement<V> getElementFrom(ByteArray byteArray) throws UniCryptException;

	@Override
	public ConcatenativeElement<V> getElementFrom(String string) throws UniCryptException;

	@Override
	public ConcatenativeElement<V> getRandomElement();

	@Override
	public ConcatenativeElement<V> getRandomElement(RandomByteSequence randomByteSequence);

	@Override
	public Sequence<? extends ConcatenativeElement<V>> getRandomElements();

	@Override
	public Sequence<? extends ConcatenativeElement<V>> getRandomElements(RandomByteSequence randomByteSequence);

	@Override
	public Sequence<? extends ConcatenativeElement<V>> getElements();

	@Override
	public ConcatenativeElement<V> apply(Element element1, Element element2);

	@Override
	public ConcatenativeElement<V> apply(Element... elements);

	@Override
	public ConcatenativeElement<V> apply(ImmutableArray<Element> elements);

	@Override
	public ConcatenativeElement<V> apply(Sequence<Element> elements);

	@Override
	public ConcatenativeElement<V> selfApply(Element element, BigInteger amount);

	@Override
	public ConcatenativeElement<V> selfApply(Element element, Element<BigInteger> amount);

	@Override
	public ConcatenativeElement<V> selfApply(Element element, long amount);

	@Override
	public ConcatenativeElement<V> selfApply(Element element);

	@Override
	public ConcatenativeElement<V> multiSelfApply(Element[] elements, BigInteger[] amounts);

}
