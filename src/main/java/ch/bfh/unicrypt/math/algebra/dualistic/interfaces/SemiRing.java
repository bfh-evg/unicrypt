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

import ch.bfh.unicrypt.UniCryptException;
import ch.bfh.unicrypt.helper.aggregator.interfaces.Aggregator;
import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.array.interfaces.ImmutableArray;
import ch.bfh.unicrypt.helper.converter.classes.ConvertMethod;
import ch.bfh.unicrypt.helper.converter.interfaces.Converter;
import ch.bfh.unicrypt.helper.random.RandomByteSequence;
import ch.bfh.unicrypt.helper.sequence.Sequence;
import ch.bfh.unicrypt.helper.tree.Tree;
import ch.bfh.unicrypt.math.algebra.additive.interfaces.AdditiveMonoid;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeMonoid;
import java.math.BigInteger;

/**
 * This interface represents the mathematical concept of a mathematical semiring. A semiring is a set of elements
 * equipped with two binary operations. The first operation is called "addition" and the second "multiplication". The
 * set together with addition forms a commutative monoid, whereas the set together with multiplication forms a (not
 * necessarily commutative) monoid. Additionally, multiplication is distributive with respect to addition. The identity
 * element of the addition is called zero, and the identity element of the multiplication is called one.
 * <p>
 * The semiring interface is implemented as a specialization of {@link AdditiveMonoid} and {@link MultiplicativeMonoid}.
 * The neutral method names inherited from {@link SemiGroup}, for example {@code apply} or {@code selfApply}, are
 * interpreted additively. No functionality is added.
 * <p>
 * The elements of a semiring are called "dualistic" (something conceptually divided into two contrasted aspects), see
 * {@link DualisticElement}. The return types are adjusted accordingly.
 * <p>
 * @param <V> The generic type of the values representing the elements of a ring
 * <p>
 * @author R. Haenni
 * <p>
 * @see AdditiveMonoid
 * @see MultiplicativeMonoid
 * @see DualisticElement
 */
public interface SemiRing<V>
	   extends AdditiveMonoid<V>, MultiplicativeMonoid<V> {

	@Override
	public <W> DualisticElement<V> getElementFrom(W value, Converter<V, W> converter) throws UniCryptException;

	@Override
	public <W> DualisticElement<V> getElementFrom(W value, ConvertMethod<W> convertMethod, Aggregator<W> aggregator)
		   throws UniCryptException;

	@Override
	public <W, X> DualisticElement<V> getElementFrom(X value, ConvertMethod<W> convertMethod, Aggregator<W> aggregator,
		   Converter<W, X> finalConverter) throws UniCryptException;

	@Override
	public <W> DualisticElement<V> getElementFrom(Tree<W> tree, ConvertMethod<W> convertMethod)
		   throws UniCryptException;

	@Override
	public DualisticElement<V> getElementFrom(long integer) throws UniCryptException;

	@Override
	public DualisticElement<V> getElementFrom(BigInteger bigInteger) throws UniCryptException;

	@Override
	public DualisticElement<V> getElementFrom(ByteArray byteArray) throws UniCryptException;

	@Override
	public DualisticElement<V> getElementFrom(String string) throws UniCryptException;

	@Override
	public DualisticElement<V> getRandomElement();

	@Override
	public DualisticElement<V> getRandomElement(RandomByteSequence randomByteSequence);

	@Override
	public Sequence<? extends DualisticElement<V>> getRandomElements();

	@Override
	public Sequence<? extends DualisticElement<V>> getRandomElements(RandomByteSequence randomByteSequence);

	@Override
	public Sequence<? extends DualisticElement<V>> getElements();

	@Override
	public DualisticElement<V> apply(Element element1, Element element2);

	@Override
	public DualisticElement<V> apply(Element... elements);

	@Override
	public DualisticElement<V> apply(ImmutableArray<Element> elements);

	@Override
	public DualisticElement<V> apply(Sequence<Element> elements);

	@Override
	public DualisticElement<V> selfApply(Element element, long factor);

	@Override
	public DualisticElement<V> selfApply(Element element, BigInteger factor);

	@Override
	public DualisticElement<V> selfApply(Element element, Element<BigInteger> factor);

	@Override
	public DualisticElement<V> selfApply(Element element);

	@Override
	public DualisticElement<V> multiSelfApply(Element[] elements, BigInteger[] factors);

	@Override
	public DualisticElement<V> getIdentityElement();

	@Override
	public DualisticElement<V> add(Element element1, Element element2);

	@Override
	public DualisticElement<V> add(Element... elements);

	@Override
	public DualisticElement<V> add(ImmutableArray<Element> elements);

	@Override
	public DualisticElement<V> add(Sequence<Element> elements);

	@Override
	public DualisticElement<V> times(Element element, long factor);

	@Override
	public DualisticElement<V> times(Element element, BigInteger factor);

	@Override
	public DualisticElement<V> times(Element element, Element<BigInteger> factor);

	@Override
	public DualisticElement<V> timesTwo(Element element);

	@Override
	public DualisticElement<V> sumOfProducts(Element[] elements, BigInteger[] factors);

	@Override
	public DualisticElement<V> getZeroElement();

	@Override
	public DualisticElement<V> multiply(Element element1, Element element2);

	@Override
	public DualisticElement<V> multiply(Element... elements);

	@Override
	public DualisticElement<V> multiply(ImmutableArray<Element> elements);

	@Override
	public DualisticElement<V> multiply(Sequence<Element> elements);

	@Override
	public DualisticElement<V> power(Element element, long exponent);

	@Override
	public DualisticElement<V> power(Element element, BigInteger exponent);

	@Override
	public DualisticElement<V> power(Element element, Element<BigInteger> exponent);

	@Override
	public DualisticElement<V> square(Element element);

	@Override
	public DualisticElement<V> productOfPowers(Element[] elements, BigInteger[] exponents);

	@Override
	public DualisticElement<V> getOneElement();

}
