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

import ch.bfh.unicrypt.helper.sequence.Predicate;
import ch.bfh.unicrypt.helper.sequence.Sequence;
import ch.bfh.unicrypt.helper.sequence.UnaryOperator;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import ch.bfh.unicrypt.random.classes.HybridRandomByteSequence;
import ch.bfh.unicrypt.random.classes.ReferenceRandomByteSequence;
import ch.bfh.unicrypt.random.interfaces.RandomByteSequence;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This abstract class provides a basis implementation for additive objects of type {@link CyclicGroup}.
 * <p>
 * @param <E> Generic type of elements of this cyclic group
 * @param <V> Generic type of values stored in the elements of this cyclic group
 * @see Group
 * <p>
 * TODO
 * @author
 */
public abstract class AbstractCyclicGroup<E extends Element<V>, V extends Object>
	   extends AbstractGroup<E, V>
	   implements CyclicGroup<V> {

	private static final long serialVersionUID = 1L;

	private E defaultGenerator;
	private final Map<ReferenceRandomByteSequence, ArrayList<E>> generatorLists;

	protected AbstractCyclicGroup(Class<?> valueClass) {
		super(valueClass);
		this.generatorLists = new HashMap<>();
	}

	@Override
	public final E getDefaultGenerator() {
		if (this.defaultGenerator == null) {
			this.defaultGenerator = this.abstractGetDefaultGenerator();
		}
		return this.defaultGenerator;
	}

	@Override
	public final E getRandomGenerator() {
		return this.defaultGetRandomGenerator(HybridRandomByteSequence.getInstance());
	}

	@Override
	public final E getRandomGenerator(RandomByteSequence randomByteSequence) {
		if (randomByteSequence == null) {
			throw new IllegalArgumentException();
		}
		return this.defaultGetRandomGenerator(randomByteSequence);
	}

	@Override
	public final E getIndependentGenerator(int index) {
		return this.getIndependentGenerator(index, (ReferenceRandomByteSequence) null);
	}

	@Override
	public final E getIndependentGenerator(int index, ReferenceRandomByteSequence referenceRandomByteSequence) {
		return (E) this.getIndependentGenerators(index, referenceRandomByteSequence).getAt(index);
	}

	@Override
	public final Tuple getIndependentGenerators(int maxIndex) {
		return this.getIndependentGenerators(maxIndex, (ReferenceRandomByteSequence) null);
	}

	@Override
	public final Tuple getIndependentGenerators(int maxIndex, ReferenceRandomByteSequence referenceRandomByteSequence) {
		return this.getIndependentGenerators(0, maxIndex, referenceRandomByteSequence);
	}

	@Override
	public final Tuple getIndependentGenerators(int minIndex, int maxIndex) {
		return this.getIndependentGenerators(minIndex, maxIndex, (ReferenceRandomByteSequence) null);
	}

	@Override
	public final Tuple getIndependentGenerators(int minIndex, int maxIndex,
		   ReferenceRandomByteSequence referenceRandomByteSequence) {
		if (minIndex < 0 || maxIndex < minIndex) {
			throw new IndexOutOfBoundsException();
		}
		if (referenceRandomByteSequence == null) {
			referenceRandomByteSequence = ReferenceRandomByteSequence.getInstance();
		}
		ArrayList<E> generatorList = this.generatorLists.get(referenceRandomByteSequence);
		if (generatorList == null) {
			generatorList = new ArrayList<>();
			this.generatorLists.put(referenceRandomByteSequence, generatorList);
		}
		if (maxIndex >= generatorList.size()) {
			referenceRandomByteSequence.reset();
			for (int i = 0; i <= maxIndex; i++) {
				E generator = this.defaultGetIndependentGenerator(referenceRandomByteSequence);
				if (i >= generatorList.size()) {
					generatorList.add(generator);
				}
			}
		}
		Element[] generators = new Element[maxIndex - minIndex + 1];
		for (int i = minIndex; i <= maxIndex; i++) {
			generators[i - minIndex] = generatorList.get(i);
		}
		return ProductSet.getInstance(this, generators.length).getElement(generators);
	}

	@Override
	public final boolean isGenerator(Element element) {
		if (!this.contains(element)) {
			throw new IllegalArgumentException();
		}
		return this.abstractIsGenerator((E) element);
	}

	// see Handbook of Applied Cryptography, Algorithm 4.80 and Note 4.81
	protected E defaultGetRandomGenerator(RandomByteSequence randomByteSequence) {
		E element;
		do {
			element = this.getRandomElement(randomByteSequence);
		} while (!this.isGenerator(element));
		return element;
	}

	protected E defaultGetIndependentGenerator(ReferenceRandomByteSequence referenceRandomByteSequence) {
		return this.defaultGetRandomGenerator(referenceRandomByteSequence);
	}

	@Override
	protected Sequence<E> defaultGetElements() {
		final AbstractCyclicGroup<E, V> group = this;
		return Sequence.getInstance(this.getDefaultGenerator(), new UnaryOperator<E>() {

			@Override
			public E apply(E element) {
				return group.apply(group.getDefaultGenerator(), element);
			}

		}).limit(new Predicate<E>() {

			@Override
			public boolean test(E element) {
				return group.getIdentityElement().equals(element);
			}

		});
	}

	protected abstract E abstractGetDefaultGenerator();

	protected abstract boolean abstractIsGenerator(E element);

}
