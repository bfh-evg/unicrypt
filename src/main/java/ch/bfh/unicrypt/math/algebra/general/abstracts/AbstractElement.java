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
package ch.bfh.unicrypt.math.algebra.general.abstracts;

import ch.bfh.unicrypt.UniCrypt;
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
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Monoid;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeElement;
import java.math.BigInteger;

/**
 * This abstract class represents the concept an element in a mathematical group. It allows applying the group operation
 * and other methods from a {@link Group} in a convenient way. Most methods provided by {@link AbstractElement} have an
 * equivalent method in {@link Group}.
 * <p>
 * @param <S> Generic type of {@link Set} of this element
 * @param <E> Generic type of this element
 * @param <V> Generic type of value stored in this element
 * @see Group
 * <p>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public abstract class AbstractElement<S extends Set<V>, E extends Element<V>, V extends Object>
	   extends UniCrypt
	   implements Element<V> {

	private static final long serialVersionUID = 1L;

	protected final AbstractSet<E, V> set;
	protected final V value;

	protected AbstractElement(final AbstractSet<E, V> set, V value) {
		this.set = set;
		this.value = value;
	}

	@Override
	public final boolean isAdditive() {
		return this instanceof AdditiveElement;
	}

	@Override
	public final boolean isMultiplicative() {
		return this instanceof MultiplicativeElement;
	}

	@Override
	public final boolean isConcatenative() {
		return this instanceof ConcatenativeElement;
	}

	@Override
	public final boolean isDualistic() {
		return this instanceof DualisticElement;
	}

	@Override
	public final boolean isTuple() {
		return this instanceof Tuple;
	}

	/**
	 * Returns the unique {@link Set} to which this element belongs
	 * <p>
	 * @return The element's set
	 */
	@Override
	public final S getSet() {
		return (S) this.set;
	}

	/**
	 * Returns the positive BigInteger value that corresponds the element.
	 * <p>
	 * @return The corresponding BigInteger value
	 */
	@Override
	public final V getValue() {
		return this.value;
	}

	@Override
	public final <W> W convertTo(Converter<V, W> converter) {
		if (converter == null) {
			throw new IllegalArgumentException();
		}
		return converter.convert(this.value);
	}

	@Override
	public final <W> W convertTo(ConvertMethod<W> convertMethod, Aggregator<W> aggregator) {
		if (convertMethod == null || aggregator == null) {
			throw new IllegalArgumentException();
		}
		return this.defaultConvertTo(convertMethod).aggregate(aggregator);
	}

	@Override
	public final <W> Tree<W> convertTo(final ConvertMethod<W> convertMethod) {
		if (convertMethod == null) {
			throw new IllegalArgumentException();
		}
		return this.defaultConvertTo(convertMethod);
	}

	@Override
	public final BigInteger convertToBigInteger() {
		return this.defaultConvertToBigInteger();
	}

	@Override
	public final ByteArray convertToByteArray() {
		return this.defaultConvertToByteArray();
	}

	@Override
	public final String convertToString() {
		return this.defaultConvertToString();
	}

	@Override
	public final ByteArray getHashValue() {
		return this.getHashValue(ConvertMethod.getInstance(), HashMethod.getInstance());
	}

	@Override
	public <W> ByteArray getHashValue(ConvertMethod<W> convertMethod, HashMethod<W> hashMethod) {
		if (convertMethod == null || hashMethod == null) {
			throw new IllegalArgumentException();
		}
		return hashMethod.getHashValue(this.convertTo(convertMethod));
	}

	//
	// The following methods are equivalent to corresponding Set methods
	//
	/**
	 * @see Group#apply(Element, Element)
	 */
	@Override
	public final E apply(final Element element) {
		if (this.set.isSemiGroup()) {
			SemiGroup semiGroup = ((SemiGroup) this.set);
			return (E) semiGroup.apply(this, element);
		}
		throw new UnsupportedOperationException();
	}

	/**
	 * @see Group#applyInverse(Element, Element)
	 */
	@Override
	public final E applyInverse(final Element element) {
		if (this.set.isGroup()) {
			Group group = ((Group) this.set);
			return (E) group.applyInverse(this, element);
		}
		throw new UnsupportedOperationException();
	}

	/**
	 * @see Group#selfApply(Element, BigInteger)
	 */
	@Override
	public final E selfApply(final BigInteger amount) {
		if (this.set.isSemiGroup()) {
			SemiGroup semiGroup = ((SemiGroup) this.set);
			return (E) semiGroup.selfApply(this, amount);
		}
		throw new UnsupportedOperationException();
	}

	/**
	 * @see Group#selfApply(Element, Element)
	 */
	@Override
	public final E selfApply(final Element<BigInteger> amount) {
		if (this.set.isSemiGroup()) {
			SemiGroup semiGroup = ((SemiGroup) this.set);
			return (E) semiGroup.selfApply(this, amount);
		}
		throw new UnsupportedOperationException();
	}

	/**
	 * @see Group#selfApply(Element, int)
	 */
	@Override
	public final E selfApply(final int amount) {
		if (this.set.isSemiGroup()) {
			SemiGroup semiGroup = ((SemiGroup) this.set);
			return (E) semiGroup.selfApply(this, amount);
		}
		throw new UnsupportedOperationException();
	}

	/**
	 * @see Group#selfApply(Element)
	 */
	@Override
	public final E selfApply() {
		if (this.set.isSemiGroup()) {
			SemiGroup semiGroup = ((SemiGroup) this.set);
			return (E) semiGroup.selfApply(this);
		}
		throw new UnsupportedOperationException();
	}

	/**
	 * @see Group#invert(Element)
	 */
	@Override
	public final E invert() {
		if (this.set.isGroup()) {
			Group group = ((Group) this.set);
			return (E) group.invert(this);
		}
		throw new UnsupportedOperationException();
	}

	/**
	 * @see Group#isIdentityElement(Element)
	 */
	@Override
	public final boolean isIdentity() {
		if (this.set.isMonoid()) {
			Monoid monoid = ((Monoid) this.set);
			return monoid.isIdentityElement(this);
		}
		throw new UnsupportedOperationException();
	}

	/**
	 * @see CyclicGroup#isGenerator(Element)
	 */
	@Override
	public final boolean isGenerator() {
		if (this.set.isCyclic()) {
			CyclicGroup cyclicGroup = ((CyclicGroup) this.set);
			return cyclicGroup.isGenerator(this);
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public final boolean isEquivalent(final Element other) {
		if (other == null) {
			throw new IllegalArgumentException();
		}
		if (this == other) {
			return true;
		}
		if (!this.set.isEquivalent(other.getSet())) {
			return false;
		}
		return this.value.equals(other.getValue());
	}

	@Override
	public int hashCode() {
		int hashCode = 7;
		hashCode = 13 * hashCode + this.set.hashCode();
		hashCode = 13 * hashCode + this.value.hashCode();
		return hashCode;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (object == null || this.getClass() != object.getClass()) {
			return false;
		}
		final Element other = (Element) object;
		if (!this.set.equals(other.getSet())) {
			return false;
		}
		return this.value.equals(other.getValue());
	}

	// this method is overriden in Tuple
	protected <W> Tree<W> defaultConvertTo(final ConvertMethod<W> convertMethod) {
		Converter<V, W> converter = this.set.getConverter(convertMethod);
		return Tree.getInstance(converter.convert(this.value));
	}

	// this method is overriden in Tuple
	protected BigInteger defaultConvertToBigInteger() {
		return this.convertTo(this.set.getBigIntegerConverter());
	}

	// this method is overriden in Tuple
	protected ByteArray defaultConvertToByteArray() {
		return this.convertTo(this.set.getByteArrayConverter());
	}

	// this method is overriden in Tuple
	protected String defaultConvertToString() {
		return this.convertTo(this.set.getStringConverter());
	}

	@Override
	protected String defaultToStringType() {
		return this.getClass().getSimpleName();
	}

	@Override
	protected String defaultToStringContent() {
		return this.value.toString();
	}

}
