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
package ch.bfh.unicrypt.math.algebra.dualistic.abstracts;

import ch.bfh.unicrypt.crypto.random.classes.PseudoRandomGeneratorCounterMode;
import ch.bfh.unicrypt.crypto.random.classes.PseudoRandomReferenceString;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomGenerator;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomReferenceString;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.CyclicRing;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author rolfhaenni
 * @param <E>
 */
public abstract class AbstractCyclicRing<E extends DualisticElement>
	   extends AbstractRing<E>
	   implements CyclicRing, Iterable<E> {

	private E defaultGenerator;

	@Override
	public final E getDefaultGenerator() {
		if (this.defaultGenerator == null) {
			this.defaultGenerator = this.abstractGetDefaultGenerator();
		}
		return this.defaultGenerator;
	}

	@Override
	public final E getRandomGenerator() {
		return this.getRandomGenerator(null);
	}

	@Override
	public final E getRandomGenerator(RandomGenerator randomGenerator) {
		if (randomGenerator == null) {
			randomGenerator = PseudoRandomGeneratorCounterMode.DEFAULT;
		}
		return this.standardGetRandomGenerator(randomGenerator);
	}

	@Override
	public final E getIndependentGenerator(int index) {
		return this.getIndependentGenerator(index, (RandomReferenceString) null);
	}

	@Override
	public final E getIndependentGenerator(int index, RandomReferenceString randomReferenceString) {
		return this.getIndependentGenerators(index, randomReferenceString)[index];
	}

	@Override
	public final E[] getIndependentGenerators(int maxIndex) {
		return this.getIndependentGenerators(maxIndex, (RandomReferenceString) null);
	}

	@Override
	public final E[] getIndependentGenerators(int maxIndex, RandomReferenceString randomReferenceString) {
		return this.getIndependentGenerators(0, maxIndex, randomReferenceString);
	}

	@Override
	public final E[] getIndependentGenerators(int minIndex, int maxIndex) {
		return this.getIndependentGenerators(minIndex, maxIndex, (RandomReferenceString) null);
	}

	@Override
	public final E[] getIndependentGenerators(int minIndex, int maxIndex, RandomReferenceString randomReferenceString) {
		if (minIndex < 0 || maxIndex < minIndex) {
			throw new IndexOutOfBoundsException();
		}
		if (randomReferenceString == null) {
			randomReferenceString = PseudoRandomReferenceString.getInstance();
		}
		// The following line is necessary for creating a generic array
		E[] generators = (E[]) Array.newInstance(this.getIdentityElement().getClass(), maxIndex - minIndex + 1);
		for (int i = 0; i <= maxIndex; i++) {
			E generator = this.standardGetIndependentGenerator(randomReferenceString);
			if (i >= minIndex) {
				generators[i - minIndex] = generator;
			}
		}
		return generators;
	}

	@Override
	public final boolean isGenerator(Element element) {
		if (!this.contains(element)) {
			throw new IllegalArgumentException();
		}
		return this.abstractIsGenerator((E) element);
	}

	// see Handbook of Applied Cryptography, Algorithm 4.80 and Note 4.81
	protected E standardGetRandomGenerator(RandomGenerator randomGenerator) {
		E element;
		do {
			element = this.getRandomElement(randomGenerator);
		} while (!this.isGenerator(element));
		return element;
	}

	protected E standardGetIndependentGenerator(RandomReferenceString randomReferenceString) {
		return this.standardGetRandomGenerator(randomReferenceString);
	}

	@Override
	protected Iterator<E> standardIterator() {
		final AbstractCyclicRing<E> cyclicRing = this;
		return new Iterator<E>() {
			BigInteger counter = BigInteger.ZERO;
			E currentElement = cyclicRing.getIdentityElement();

			@Override
			public boolean hasNext() {
				return counter.compareTo(cyclicRing.getOrder()) < 0;
			}

			@Override
			public E next() {
				if (this.hasNext()) {
					this.counter = this.counter.add(BigInteger.ONE);
					E nextElement = currentElement;
					currentElement = cyclicRing.apply(currentElement, cyclicRing.getDefaultGenerator());
					return nextElement;
				}
				throw new NoSuchElementException();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException("Not supported yet.");
			}
		};
	}

	//
	// The following protected abstract method must be implemented in every direct sub-class
	//
	protected abstract E abstractGetDefaultGenerator();

	protected abstract boolean abstractIsGenerator(E element);

}
