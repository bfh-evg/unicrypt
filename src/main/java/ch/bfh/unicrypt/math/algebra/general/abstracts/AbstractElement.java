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
 * @see Group
 * <p>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public abstract class AbstractElement<S extends Set, E extends Element>
	   extends UniCrypt
	   implements Element {

	private final S set;
	private BigInteger value;
	private int javaHashValue;

	protected AbstractElement(final S set) {
		if (set == null) {
			throw new IllegalArgumentException();
		}
		this.set = set;
		hashMap = new HashMap<HashMethod, FiniteByteArrayElement>();
	}

	protected AbstractElement(final S set, final BigInteger value) {
		this(set);
		this.value = value;
		//Frage: Kann es sein, dass ein Element einen Wert und eine gefüllte HashMap hat?
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
	public final BigInteger getValue() {
		if (this.value == null) {
			this.value = standardGetValue();
		}
		return this.value;
	}

	@Override
	public final FiniteByteArrayElement getHashValue() {
		return this.getHashValue(HashMethod.DEFAULT);
	}

	private HashMap<HashMethod, FiniteByteArrayElement> hashMap;

	@Override
	public final FiniteByteArrayElement getHashValue(HashMethod hashMethod) {
		//TODO: This is a memory-hog! But a super speed-up
		//TODO: If this HashMap would become static, it would speed things up again... But would it leak (cryptographically)?
		if (!hashMap.containsKey(hashMethod)) {
			if (this.isTuple() && hashMethod.isRecursive()) {
				Tuple tuple = (Tuple) this;
				int arity = tuple.getArity();
				ByteArrayElement[] hashValues = new ByteArrayElement[arity];
				for (int i = 0; i < arity; i++) {
					hashValues[i] = tuple.getAt(i).getHashValue(hashMethod).getByteArrayElement();
				}
				hashMap.put(hashMethod, ByteArrayMonoid.getInstance().apply(hashValues).getHashValue(hashMethod));
			} else {
				MessageDigest messageDigest = hashMethod.getMessageDigest();
				messageDigest.reset();
				hashMap.put(hashMethod, FixedByteArraySet.getInstance(hashMethod.getLength()).getElement(messageDigest.digest(this.getValue().toByteArray())));
			}
		}
		return hashMap.get(hashMethod);
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

	//
	// The standard implementations of the following three methods are
	// insufficient for elements.
	//
	@Override
	public final boolean isEquivalent(final Element element) {
		if (element == null) {
			throw new IllegalArgumentException();
		}
		if (this == element) {
			return true;
		}
		if (this.getClass() != element.getClass()) {
			return false;
		}
		if (!this.getSet().isEquivalent(element.getSet())) {
			return false;
		}
		return this.standardIsEquivalent((E) element);
	}

	//
	// The following protected methods are standard implementations, which may change in sub-classes
	//
	//TODO: Das darf nicht sein, dass ein standard eine UnsupportedOperation zurückwirft!
	//Das muss wohl abstract werden oder aber hier implementiert werden!
	protected BigInteger standardGetValue() {
		throw new UnsupportedOperationException();
	}

	protected boolean standardIsEquivalent(E element) {
		return this.getValue().equals(element.getValue());
	}

	@Override
	protected String standardToStringName() {
		return this.getClass().getSimpleName();
	}

	@Override
	protected String standardToStringContent() {
		return this.getValue().toString();
	}

	@Override
	public int hashCode() {
		if (this.value != null) {
			if (this.javaHashValue == 0) {
				this.javaHashValue = this.getValue().intValue();
			}
			return javaHashValue;
		}
		return 0; //hash;
	}

	// THIS METHOD IS NOT THE FINAL IMPLEMENTATION
	@Override
	public boolean equals(Object obj) {
		return this.isEquivalent((Element) obj);
	}

}
