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
import ch.bfh.unicrypt.helper.math.Point;
import ch.bfh.unicrypt.helper.random.RandomByteSequence;
import ch.bfh.unicrypt.helper.random.deterministic.DeterministicRandomByteSequence;
import ch.bfh.unicrypt.helper.sequence.Sequence;
import ch.bfh.unicrypt.helper.tree.Tree;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.FiniteField;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import java.math.BigInteger;

/**
 * This interface represents an subgroup of an elliptic curve (EC) of prime order. The set of points of the curve
 * creates an additive group so that adding two points creates another point on the curve. The points of the curve
 * consist of an x-value and an y-value, which are elements of the curve's underlying finite field.
 * <p>
 * The elliptic curve interface is implemented as a specialization of {@link AdditiveCyclicGroup} with additional
 * methods for dealing with the curve points, the curve's parameters, and the underlying finite field. Some return types
 * are adjusted.
 * <p>
 * @param <V>  The generic type of the values stored in the elements of the underlying finite field
 * @param <DE> The generic type of the dualistic elements of the underlying finite field
 * <p>
 * @author C. Lutz
 * @author R. Haenni
 * @see ECElement
 */
public interface EC<V, DE extends DualisticElement<V>>
	   extends AdditiveCyclicGroup<Point<DE>> {

	/**
	 * Returns the underlying finite field of this elliptic curve.
	 * <p>
	 * @return The finite field of this elliptic curve
	 */
	public FiniteField<V> getFiniteField();

	/**
	 * Returns the first coefficient of this elliptic curve (usually denoted by {@code a}).
	 * <p>
	 * @return The first coefficient {@code a}
	 */
	public DE getA();

	/**
	 * Returns the first coefficient of this elliptic curve (usually denoted by {@code b}).
	 * <p>
	 * @return The second coefficient {@code b}
	 */
	public DE getB();

	/**
	 * Returns the cofactor of this EC group. This is the fraction of the total number of points on the curve and the
	 * number of points in the actual subgroup.
	 * <p>
	 * @return The cofactor of this EC group
	 */
	public BigInteger getCoFactor();

	/**
	 * Checks if this elliptic curve contains a point for the given x-coordinate.
	 * <p>
	 * @param x The given x-coordinate
	 * @return {@code true} if the elliptic contains a point for the given x-coordinate, {@code false} otherwise
	 */
	public boolean contains(DE x);

	/**
	 * Checks if this elliptic curve contains a point for the given x- and y-coordinates.
	 * <p>
	 * @param x The given x-coordinate
	 * @param y The given y-coordinate
	 * @return {@code true} if the elliptic contains a point for the given x- and y-coordinates, {@code false} otherwise
	 */
	public boolean contains(DE x, DE y);

	/**
	 * Returns one of the two points on the elliptic curve that corresponds to the given x-coordinate. Which of the two
	 * points is selected is unspecified. Throws an exception if no such point exists.
	 * <p>
	 * @param x The given x-coordinate
	 * @return One of the two points that corresponds to the given x-coordinate
	 */
	public ECElement<V, DE> getElement(DE x);

	/**
	 * Returns the point on the elliptic curve that corresponds to the given x- and y-coordinates. Throws an exception
	 * if no such point exists.
	 * <p>
	 * @param x The given x-coordinate
	 * @param y The given y-coordinate
	 * @return The point that corresponds to the given x- and y-coordinates
	 */
	public ECElement<V, DE> getElement(DE x, DE y);

	@Override
	public ECElement<V, DE> add(Element element1, Element element2);

	@Override
	public ECElement<V, DE> add(Element... elements);

	@Override
	public ECElement<V, DE> add(ImmutableArray<Element> elements);

	@Override
	public ECElement<V, DE> add(Sequence<Element> elements);

	@Override
	public ECElement<V, DE> times(Element element, long factor);

	@Override
	public ECElement<V, DE> times(Element element, BigInteger factor);

	@Override
	public ECElement<V, DE> times(Element element, Element<BigInteger> factor);

	@Override
	public ECElement<V, DE> timesTwo(Element element);

	@Override
	public <W> ECElement<V, DE> getElementFrom(W value, Converter<Point<DE>, W> converter) throws UniCryptException;

	@Override
	public <W> ECElement<V, DE> getElementFrom(W value, ConvertMethod<W> convertMethod, Aggregator<W> aggregator) throws
		   UniCryptException;

	@Override
	public <W, X> ECElement<V, DE> getElementFrom(X value, ConvertMethod<W> convertMethod, Aggregator<W> aggregator,
		   Converter<W, X> finalConverter) throws UniCryptException;

	@Override
	public <W> ECElement<V, DE> getElementFrom(Tree<W> tree, ConvertMethod<W> convertMethod) throws UniCryptException;

	@Override
	public ECElement<V, DE> getElementFrom(long integer) throws UniCryptException;

	@Override
	public ECElement<V, DE> getElementFrom(BigInteger bigInteger) throws UniCryptException;

	@Override
	public ECElement<V, DE> getElementFrom(ByteArray byteArray) throws UniCryptException;

	@Override
	public ECElement<V, DE> getElementFrom(String string) throws UniCryptException;

	@Override
	public ECElement<V, DE> getRandomElement();

	@Override
	public ECElement<V, DE> getRandomElement(RandomByteSequence randomByteSequence);

	@Override
	public Sequence<? extends ECElement<V, DE>> getRandomElements();

	@Override
	public Sequence<? extends ECElement<V, DE>> getRandomElements(RandomByteSequence randomByteSequence);

	@Override
	public Sequence<? extends ECElement<V, DE>> getElements();

	@Override
	public ECElement<V, DE> apply(Element element1, Element element2);

	@Override
	public ECElement<V, DE> apply(Element... elements);

	@Override
	public ECElement<V, DE> apply(ImmutableArray<Element> elements);

	@Override
	public ECElement<V, DE> apply(Sequence<Element> elements);

	@Override
	public ECElement<V, DE> selfApply(Element element, long factor);

	@Override
	public ECElement<V, DE> selfApply(Element element, BigInteger factor);

	@Override
	public ECElement<V, DE> selfApply(Element element, Element<BigInteger> factor);

	@Override
	public ECElement<V, DE> selfApply(Element element);

	@Override
	public ECElement<V, DE> multiSelfApply(Element[] elements, BigInteger[] factors);

	@Override
	public ECElement<V, DE> getIdentityElement();

	@Override
	public ECElement<V, DE> invert(Element element);

	@Override
	public ECElement<V, DE> applyInverse(Element element1, Element element2);

	@Override
	public ECElement<V, DE> getDefaultGenerator();

	@Override
	public Sequence<? extends ECElement<V, DE>> getIndependentGenerators();

	@Override
	public Sequence<? extends ECElement<V, DE>> getIndependentGenerators(
		   DeterministicRandomByteSequence randomByteSequence);

}
