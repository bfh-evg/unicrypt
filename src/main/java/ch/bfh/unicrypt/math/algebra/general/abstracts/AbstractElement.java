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

import ch.bfh.unicrypt.math.algebra.additive.interfaces.AdditiveElement;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.ByteArrayElement;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.ByteArrayMonoid;
import ch.bfh.unicrypt.math.algebra.concatenative.interfaces.ConcatenativeElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.general.classes.FiniteByteArrayElement;
import ch.bfh.unicrypt.math.algebra.general.classes.FixedByteArraySet;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Monoid;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeElement;
import ch.bfh.unicrypt.math.helper.HashMethod;
import ch.bfh.unicrypt.math.helper.UniCrypt;
import ch.bfh.unicrypt.math.helper.bytetree.ByteTree;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.HashMap;

/**
 * This abstract class represents the concept an element in a mathematical group. It allows applying the group operation
 * and other methods from a {@link Group} in a convenient way. Most methods provided by {@link AbstractElement} have an
 * equivalent method in {@link Group}.
 * <p>
 * @param <S>
 * @param <E>
 * @param <V>
 * @see Group
 * <p>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public abstract class AbstractElement<S extends Set, E extends Element, V extends Object>
	   extends UniCrypt
	   implements Element {

	private final S set;
	private final V value;

	// the following fields are needed for optimizations
	private BigInteger bigInteger;
	private ByteTree byteTree;
	private final HashMap<HashMethod, FiniteByteArrayElement> hashValues;

	protected AbstractElement(final S set, V value) {
		this.set = set;
		this.value = value;
		this.hashValues = new HashMap<HashMethod, FiniteByteArrayElement>();
	}

	@Override
	public boolean isAdditive() {
		return this instanceof AdditiveElement;
	}

	@Override
	public boolean isMultiplicative() {
		return this instanceof MultiplicativeElement;
	}

	@Override
	public boolean isConcatenative() {
		return this instanceof ConcatenativeElement;
	}

	@Override
	public boolean isDualistic() {
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
		return this.set;
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
	public BigInteger getBigInteger() {
		if (this.bigInteger == null) {
			this.bigInteger = this.abstractGetBigInteger();
		}
		return this.bigInteger;
	}

	@Override
	public ByteTree getByteTree() {
		if (this.byteTree == null) {
			this.byteTree = this.abstractGetByteTree();
		}
		return this.byteTree;
	}

	@Override
	public final FiniteByteArrayElement getHashValue() {
		return this.getHashValue(HashMethod.DEFAULT);
	}

	@Override
	public final FiniteByteArrayElement getHashValue(HashMethod hashMethod) {
		//TODO: This is a memory-hog! But a super speed-up
		//TODO: If this HashMap would become static, it would speed things up again... But would it leak (cryptographically)?
		if (!this.hashValues.containsKey(hashMethod)) {
			if (this.isTuple() && hashMethod.isRecursive()) {
				Tuple tuple = (Tuple) this;
				int arity = tuple.getArity();
				ByteArrayElement[] hashes = new ByteArrayElement[arity];
				for (int i = 0; i < arity; i++) {
					hashes[i] = tuple.getAt(i).getHashValue(hashMethod).getByteArrayElement();
				}
				this.hashValues.put(hashMethod, ByteArrayMonoid.getInstance().apply(hashes).getHashValue(hashMethod));
			} else {
				MessageDigest messageDigest = hashMethod.getMessageDigest();
				messageDigest.reset();
				this.hashValues.put(hashMethod, FixedByteArraySet.getInstance(hashMethod.getLength()).getElement(messageDigest.digest(this.getBigInteger().toByteArray())));
			}
		}
		return this.hashValues.get(hashMethod);
	}

	//
	// The following methods are equivalent to corresponding Set methods
	//
	/**
	 * @see Group#apply(Element, Element)
	 */
	@Override
	public final E apply(final Element element) {
		if (this.getSet().isSemiGroup()) {
			SemiGroup semiGroup = ((SemiGroup) this.getSet());
			return (E) semiGroup.apply(this, element);
		}
		throw new UnsupportedOperationException();
	}

	/**
	 * @see Group#applyInverse(Element, Element)
	 */
	@Override
	public final E applyInverse(final Element element) {
		if (this.getSet().isGroup()) {
			Group group = ((Group) this.getSet());
			return (E) group.applyInverse(this, element);
		}
		throw new UnsupportedOperationException();
	}

	/**
	 * @see Group#selfApply(Element, BigInteger)
	 */
	@Override
	public final E selfApply(final BigInteger amount) {
		if (this.getSet().isSemiGroup()) {
			SemiGroup semiGroup = ((SemiGroup) this.getSet());
			return (E) semiGroup.selfApply(this, amount);
		}
		throw new UnsupportedOperationException();
	}

	/**
	 * @see Group#selfApply(Element, Element)
	 */
	@Override
	public final E selfApply(final Element amount) {
		if (this.getSet().isSemiGroup()) {
			SemiGroup semiGroup = ((SemiGroup) this.getSet());
			return (E) semiGroup.selfApply(this, amount);
		}
		throw new UnsupportedOperationException();
	}

	/**
	 * @see Group#selfApply(Element, int)
	 */
	@Override
	public final E selfApply(final int amount) {
		if (this.getSet().isSemiGroup()) {
			SemiGroup semiGroup = ((SemiGroup) this.getSet());
			return (E) semiGroup.selfApply(this, amount);
		}
		throw new UnsupportedOperationException();
	}

	/**
	 * @see Group#selfApply(Element)
	 */
	@Override
	public final E selfApply() {
		if (this.getSet().isSemiGroup()) {
			SemiGroup semiGroup = ((SemiGroup) this.getSet());
			return (E) semiGroup.selfApply(this);
		}
		throw new UnsupportedOperationException();
	}

	/**
	 * @see Group#invert(Element)
	 */
	@Override
	public final E invert() {
		if (this.getSet().isGroup()) {
			Group group = ((Group) this.getSet());
			return (E) group.invert(this);
		}
		throw new UnsupportedOperationException();
	}

	/**
	 * @see Group#isIdentityElement(Element)
	 */
	@Override
	public final boolean isIdentity() {
		if (this.getSet().isMonoid()) {
			Monoid monoid = ((Monoid) this.getSet());
			return monoid.isIdentityElement(this);
		}
		throw new UnsupportedOperationException();
	}

	/**
	 * @see CyclicGroup#isGenerator(Element)
	 */
	@Override
	public final boolean isGenerator() {
		if (this.getSet().isCyclic()) {
			CyclicGroup cyclicGroup = ((CyclicGroup) this.getSet());
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
		if (!this.getSet().isEquivalent(other.getSet())) {
			return false;
		}
		return this.getValue().equals(other.getValue());
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
		if (!this.getSet().equals(other.getSet())) {
			return false;
		}
		return this.getValue().equals(other.getValue());
	}

	protected abstract BigInteger abstractGetBigInteger();

	protected abstract ByteTree abstractGetByteTree();

	@Override
	protected String defaultToStringName() {
		return this.getClass().getSimpleName();
	}

	@Override
	protected String defaultToStringValue() {
		return this.getValue().toString();
	}

}
