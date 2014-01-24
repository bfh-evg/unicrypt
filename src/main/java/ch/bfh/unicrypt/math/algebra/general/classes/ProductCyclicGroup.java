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

import ch.bfh.unicrypt.crypto.random.classes.PseudoRandomReferenceString;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomNumberGenerator;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomReferenceString;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.helper.ImmutableArray;
import ch.bfh.unicrypt.math.helper.compound.CompoundIterator;
import ch.bfh.unicrypt.math.utility.MathUtil;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author rolfhaenni
 */
public class ProductCyclicGroup
	   extends ProductGroup
	   implements CyclicGroup {

	private Tuple defaultGenerator;

	protected ProductCyclicGroup(final CyclicGroup[] cyclicGroups) {
		super(cyclicGroups);
	}

	protected ProductCyclicGroup(final CyclicGroup cyclicGroup, final int arity) {
		super(cyclicGroup, arity);
	}

	@Override
	public CyclicGroup getFirst() {
		return (CyclicGroup) super.getFirst();
	}

	@Override
	public CyclicGroup getAt(int index) {
		return (CyclicGroup) super.getAt(index);
	}

	@Override
	public CyclicGroup getAt(int... indices) {
		return (CyclicGroup) super.getAt(indices);
	}

	@Override
	public CyclicGroup[] getAll() {
		return (CyclicGroup[]) super.getAll();
	}

	@Override
	public ProductCyclicGroup removeAt(final int index) {
		return (ProductCyclicGroup) super.removeAt(index);
	}

	@Override
	public Iterable<? extends CyclicGroup> makeIterable() {
		final ProductSet productCyclicGroup = this;
		return new Iterable<CyclicGroup>() {
			@Override
			public Iterator<CyclicGroup> iterator() {
				return new CompoundIterator<CyclicGroup>(productCyclicGroup);
			}
		};
	}

	protected boolean standardCyclicGroup() {
		return true;
	}

	@Override
	protected Iterator<Tuple> standardIterator() {
		final ProductCyclicGroup productCyclicGroup = this;
		return new Iterator<Tuple>() {
			BigInteger counter = BigInteger.ZERO;
			Tuple currentTuple = productCyclicGroup.getIdentityElement();

			@Override
			public boolean hasNext() {
				return counter.compareTo(productCyclicGroup.getOrder()) < 0;
			}

			@Override
			public Tuple next() {
				if (this.hasNext()) {
					this.counter = this.counter.add(BigInteger.ONE);
					Tuple nextElement = currentTuple;
					currentTuple = productCyclicGroup.apply(currentTuple, productCyclicGroup.getDefaultGenerator());
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

	@Override
	public final Tuple getDefaultGenerator() {
		if (this.defaultGenerator == null) {
			int arity = this.getArity();
			Element[] defaultGenerators = new Element[arity];
			for (int i = 0; i < arity; i++) {
				defaultGenerators[i] = this.getAt(i).getDefaultGenerator();
			}
			this.defaultGenerator = this.abstractGetElement(ImmutableArray.getInstance(defaultGenerators));
		}
		return this.defaultGenerator;
	}

	@Override
	public final Tuple getRandomGenerator() {
		return this.getRandomGenerator(null);
	}

	@Override
	public final Tuple getRandomGenerator(RandomNumberGenerator randomGenerator) {
		int arity = this.getArity();
		Element[] randomGenerators = new Element[arity];
		for (int i = 0; i < arity; i++) {
			randomGenerators[i] = this.getAt(i).getRandomGenerator(randomGenerator);
		}
		return this.abstractGetElement(ImmutableArray.getInstance(randomGenerators));
	}

	@Override
	public final Tuple getIndependentGenerator(int index) {
		return this.getIndependentGenerator(index, (RandomReferenceString) null);
	}

	@Override
	public final Tuple getIndependentGenerator(int index, RandomReferenceString randomReferenceString) {
		return this.getIndependentGenerators(index, randomReferenceString)[index];
	}

	@Override
	public final Tuple[] getIndependentGenerators(int maxIndex) {
		return this.getIndependentGenerators(maxIndex, (RandomReferenceString) null);
	}

	@Override
	public final Tuple[] getIndependentGenerators(int maxIndex, RandomReferenceString randomReferenceString) {
		return this.getIndependentGenerators(0, maxIndex, randomReferenceString);
	}

	@Override
	public final Tuple[] getIndependentGenerators(int minIndex, int maxIndex) {
		return this.getIndependentGenerators(minIndex, maxIndex, (RandomReferenceString) null);
	}

	@Override
	public final Tuple[] getIndependentGenerators(int minIndex, int maxIndex, RandomReferenceString randomReferenceString) {
		if (minIndex < 0 || maxIndex < minIndex) {
			throw new IndexOutOfBoundsException();
		}
		if (randomReferenceString == null) {
			randomReferenceString = PseudoRandomReferenceString.getInstance();
		}
		// The following line is necessary for creating a generic array
		Tuple[] generators = (Tuple[]) Array.newInstance(this.getIdentityElement().getClass(), maxIndex - minIndex + 1);
		for (int i = 0; i <= maxIndex; i++) {
			Tuple generator = this.standardGetIndependentGenerator(randomReferenceString);
			if (i >= minIndex) {
				generators[i - minIndex] = generator;
			}
		}
		return generators;
	}

	protected Tuple standardGetIndependentGenerator(RandomReferenceString randomReferenceString) {
		return this.getRandomGenerator(randomReferenceString);
	}

	@Override
	public final boolean isGenerator(Element element) {
		if (!this.contains(element)) {
			throw new IllegalArgumentException();
		}
		Tuple tuple = (Tuple) element;
		for (int i = 0; i < this.getArity(); i++) {
			if (!this.getAt(i).isGenerator(tuple.getAt(i))) {
				return false;
			}
		}
		return true;
	}

	//
	// STATIC FACTORY METHODS
	//
	/**
	 * This is a static factory method to construct a composed cyclic group without calling respective constructors. The
	 * input groups are given as an array.
	 * <p/>
	 * @param cyclicGroups The array of cyclic groups
	 * @return The corresponding composed group
	 * @throws IllegalArgumentException if {@literal groups} is null or contains null
	 */
	public static ProductCyclicGroup getInstance(final CyclicGroup... cyclicGroups) {
		if (cyclicGroups == null) {
			throw new IllegalArgumentException();
		}
		if (ProductCyclicGroup.areRelativelyPrime(cyclicGroups)) {
			return new ProductCyclicGroup(cyclicGroups);
		}
		throw new IllegalArgumentException();
	}

	public static ProductCyclicGroup getInstance(final CyclicGroup group, int arity) {
		if ((group == null) || (arity < 0) || (arity > 1)) {
			throw new IllegalArgumentException();
		}
		if (arity == 0) {
			return new ProductCyclicGroup(new CyclicGroup[]{});
		}
		return new ProductCyclicGroup(new CyclicGroup[]{group});
	}

	//
	// STATIC HELPER METHODS
	//
	private static boolean areRelativelyPrime(CyclicGroup[] cyclicGroups) {
		BigInteger[] orders = new BigInteger[cyclicGroups.length];
		for (int i = 0; i < cyclicGroups.length; i++) {
			if (cyclicGroups[i] == null) {
				throw new IllegalArgumentException();
			}
			orders[i] = cyclicGroups[i].getOrder();
		}
		return MathUtil.areRelativelyPrime(orders);
	}

}
