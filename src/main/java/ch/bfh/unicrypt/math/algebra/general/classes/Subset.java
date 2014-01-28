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
package ch.bfh.unicrypt.math.algebra.general.classes;

import ch.bfh.unicrypt.crypto.random.interfaces.RandomByteSequence;
import ch.bfh.unicrypt.math.algebra.general.abstracts.AbstractSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class Subset
	   extends AbstractSet<Element, Element>
	   implements Iterable<Element> {

	private final Set superSet;
	private final HashSet<Element> hashSet;

	protected Subset(Set superSet, HashSet<Element> elementSet) {
		super(Element.class);
		this.superSet = superSet;
		this.hashSet = elementSet;
	}

	public Set getSuperset() {
		return this.superSet;
	}

	public Element[] getElements() {
		return this.hashSet.toArray(new Element[]{});
	}

	@Override
	protected Iterator<Element> standardIterator() {
		return this.hashSet.iterator();
	}

	@Override
	protected boolean standardContains(final Element element) {
		return this.hashSet.contains(element);
	}

	@Override
	protected boolean abstractContains(BigInteger bigInteger) {
		for (Element element : this.hashSet) {
			if (element.getValue().equals(bigInteger)) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected boolean abstractContains(Element value) {
		return this.hashSet.contains(value);
	}

	@Override
	protected Element abstractGetElement(BigInteger bigInteger) {
		return this.getSuperset().getElement(bigInteger);
	}

	@Override
	protected Element abstractGetElement(Element value) {
		return value;
	}

	@Override
	protected BigInteger abstractGetOrder() {
		return BigInteger.valueOf(this.hashSet.size());
	}

	@Override
	protected Element abstractGetRandomElement(RandomByteSequence randomByteSequence) {
		return this.getElements()[randomByteSequence.getRandomNumberGenerator().nextInteger(this.hashSet.size() - 1)];
	}

	@Override
	protected boolean abstractIsEquivalent(Set set) {
		Subset other = (Subset) set;
		return this.superSet.isEquivalent(other.superSet) && this.hashSet.equals(other.hashSet);
	}

	@Override
	protected int abstractHashCode() {
		int hash = 7;
		hash = 47 * hash + this.superSet.hashCode();
		hash = 47 * hash + this.hashSet.hashCode();
		return hash;
	}

	public static Subset getInstance(Set superSet, Element... elements) {
		if (superSet == null || elements == null) {
			throw new IllegalArgumentException();
		}
		// A LinkedHashSet retains the order
		HashSet<Element> hashSet = new LinkedHashSet<Element>();
		for (Element element : elements) {
			if (element == null || !superSet.contains(element)) {
				throw new IllegalArgumentException();
			}
			hashSet.add(element);
		}
		return new Subset(superSet, hashSet);
	}

}
