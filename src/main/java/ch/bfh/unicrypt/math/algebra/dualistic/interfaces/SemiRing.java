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
package ch.bfh.unicrypt.math.algebra.dualistic.interfaces;

import ch.bfh.unicrypt.helper.aggregator.interfaces.Aggregator;
import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.array.interfaces.ImmutableArray;
import ch.bfh.unicrypt.helper.converter.classes.ConvertMethod;
import ch.bfh.unicrypt.helper.converter.interfaces.Converter;
import ch.bfh.unicrypt.helper.sequence.Sequence;
import ch.bfh.unicrypt.helper.tree.Tree;
import ch.bfh.unicrypt.math.algebra.additive.interfaces.AdditiveElement;
import ch.bfh.unicrypt.math.algebra.additive.interfaces.AdditiveMonoid;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Monoid;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeMonoid;
import ch.bfh.unicrypt.random.interfaces.RandomByteSequence;
import java.math.BigInteger;

/**
 * TODO This interface represents the mathematical concept of a semiring. A semiring is a monoid with a second
 * associative compositions: the first binary operation is called "addition" and is commutative and the second binary
 * operation is called "multiplication". It is implemented as a specialization of {@link Monoid}. Some return types are
 * updated.
 * <p>
 * @author rolfhaenni
 * @param <V> Generic type of values stored in the elements of this semiring
 */
public interface SemiRing<V extends Object>
	   extends AdditiveMonoid<V>, MultiplicativeMonoid<V> {

	@Override
	public <W> DualisticElement<V> getElementFrom(W value, Converter<V, W> converter);

	@Override
	public <W> DualisticElement<V> getElementFrom(W value, ConvertMethod<W> convertMethod, Aggregator<W> aggregator);

	@Override
	public <W> DualisticElement<V> getElementFrom(Tree<W> tree, ConvertMethod<W> convertMethod);

	@Override
	public DualisticElement<V> getElementFrom(long integer);

	@Override
	public DualisticElement<V> getElementFrom(BigInteger bigInteger);

	@Override
	public DualisticElement<V> getElementFrom(ByteArray byteArray);

	@Override
	public DualisticElement<V> getElementFrom(String string);

	@Override
	public DualisticElement<V> getRandomElement();

	@Override
	public DualisticElement<V> getRandomElement(RandomByteSequence randomByteSequence);

	@Override
	public Sequence<? extends DualisticElement<V>> getRandomElements();

	@Override
	public Sequence<? extends DualisticElement<V>> getRandomElements(long n);

	@Override
	public Sequence<? extends DualisticElement<V>> getRandomElements(RandomByteSequence randomByteSequence);

	@Override
	public Sequence<? extends DualisticElement<V>> getRandomElements(long n, RandomByteSequence randomByteSequence);

	@Override
	public DualisticElement<V> apply(Element element1, Element element2);

	@Override
	public DualisticElement<V> apply(Element... elements);

	@Override
	public DualisticElement<V> apply(ImmutableArray<Element> elements);

	@Override
	public DualisticElement<V> apply(Sequence<Element> elements);

	@Override
	public DualisticElement<V> selfApply(Element element, BigInteger amount);

	@Override
	public DualisticElement<V> selfApply(Element element, Element<BigInteger> amount);

	@Override
	public DualisticElement<V> selfApply(Element element, long amount);

	@Override
	public DualisticElement<V> selfApply(Element element);

	@Override
	public DualisticElement<V> multiSelfApply(Element[] elements, BigInteger[] amounts);

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
	public DualisticElement<V> times(Element element, BigInteger amount);

	@Override
	public DualisticElement<V> times(Element element, Element<BigInteger> amount);

	@Override
	public DualisticElement<V> times(Element element, long amount);

	@Override
	public DualisticElement<V> timesTwo(Element element);

	@Override
	public DualisticElement<V> sumOfProducts(Element[] elements, BigInteger[] amounts);

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
	public DualisticElement<V> power(Element element, BigInteger amount);

	@Override
	public DualisticElement<V> power(Element element, Element<BigInteger> amount);

	@Override
	public DualisticElement<V> power(Element element, long amount);

	@Override
	public DualisticElement<V> square(Element element);

	@Override
	public DualisticElement<V> productOfPowers(Element[] elements, BigInteger[] amounts);

	@Override
	public DualisticElement<V> getOneElement();

}
