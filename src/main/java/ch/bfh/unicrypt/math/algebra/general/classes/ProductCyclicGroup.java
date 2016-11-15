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
package ch.bfh.unicrypt.math.algebra.general.classes;

import ch.bfh.unicrypt.ErrorCode;
import ch.bfh.unicrypt.UniCryptRuntimeException;
import ch.bfh.unicrypt.helper.array.classes.DenseArray;
import ch.bfh.unicrypt.helper.random.RandomByteSequence;
import ch.bfh.unicrypt.helper.random.RandomByteSequenceIterator;
import ch.bfh.unicrypt.helper.random.deterministic.DeterministicRandomByteSequence;
import ch.bfh.unicrypt.helper.random.hybrid.HybridRandomByteSequence;
import ch.bfh.unicrypt.helper.sequence.Sequence;
import ch.bfh.unicrypt.helper.sequence.SequenceIterator;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;

/**
 *
 * @author R. Haenni
 */
public class ProductCyclicGroup
	   extends ProductGroup
	   implements CyclicGroup<DenseArray<Element>> {

	private static final long serialVersionUID = 1L;

	private Tuple defaultGenerator;

	protected ProductCyclicGroup(DenseArray<Set> sets) {
		super(sets);
	}

	@Override
	public CyclicGroup getFirst() {
		return (CyclicGroup) super.getFirst();
	}

	@Override
	public CyclicGroup getLast() {
		return (CyclicGroup) super.getLast();
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
	public ProductCyclicGroup removeAt(final int index) {
		return (ProductCyclicGroup) super.removeAt(index);
	}

	@Override
	public ProductCyclicGroup extract(int offset, int length) {
		return (ProductCyclicGroup) super.extract(offset, length);
	}

	@Override
	public ProductCyclicGroup extractPrefix(int length) {
		return (ProductCyclicGroup) super.extractPrefix(length);
	}

	@Override
	public ProductCyclicGroup extractSuffix(int length) {
		return (ProductCyclicGroup) super.extractSuffix(length);
	}

	@Override
	public ProductCyclicGroup extractRange(int fromIndex, int toIndex) {
		return (ProductCyclicGroup) super.extractRange(fromIndex, toIndex);
	}

	@Override
	public ProductCyclicGroup remove(int offset, int length) {
		return (ProductCyclicGroup) super.remove(offset, length);
	}

	@Override
	public ProductCyclicGroup removePrefix(int length) {
		return (ProductCyclicGroup) super.removePrefix(length);
	}

	@Override
	public ProductCyclicGroup removeSuffix(int length) {
		return (ProductCyclicGroup) super.removeSuffix(length);
	}

	@Override
	public ProductCyclicGroup removeRange(int fromIndex, int toIndex) {
		return (ProductCyclicGroup) super.removeRange(fromIndex, toIndex);
	}

	@Override
	public ProductCyclicGroup reverse() {
		return (ProductCyclicGroup) super.reverse();
	}

	@Override
	public ProductCyclicGroup[] split(int... indices) {
		return (ProductCyclicGroup[]) super.split(indices);
	}

	@Override
	public final Tuple getDefaultGenerator() {
		if (this.defaultGenerator == null) {
			Element[] defaultGenerators = new Element[this.getArity()];
			for (int i : this.getAllIndices()) {
				defaultGenerators[i] = this.getAt(i).getDefaultGenerator();
			}
			this.defaultGenerator = this.abstractGetElement(DenseArray.getInstance(defaultGenerators));
		}
		return this.defaultGenerator;
	}

	@Override
	public final Sequence<Tuple> getIndependentGenerators() {
		return this.getIndependentGenerators(DeterministicRandomByteSequence.getInstance());
	}

	@Override
	public final Sequence<Tuple> getIndependentGenerators(DeterministicRandomByteSequence randomByteSequence) {
		if (randomByteSequence == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this);
		}
		return this.defaultGetRandomGenerators(randomByteSequence);
	}

	@Override
	public final Tuple getRandomGenerator() {
		return this.getRandomGenerators().get();
	}

	@Override
	public final Tuple getRandomGenerator(RandomByteSequence randomByteSequence) {
		return this.getRandomGenerators(randomByteSequence).get();
	}

	@Override
	public final Sequence<Tuple> getRandomGenerators() {
		return this.getRandomGenerators(HybridRandomByteSequence.getInstance());
	}

	@Override
	public final Sequence<Tuple> getRandomGenerators(RandomByteSequence randomByteSequence) {
		if (randomByteSequence == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this);
		}
		return this.defaultGetRandomGenerators(randomByteSequence);
	}

	protected Sequence<Tuple> defaultGetRandomGenerators(RandomByteSequence randomByteSequence) {
		final RandomByteSequenceIterator iterator = randomByteSequence.iterator();
		final int tupleLenght = this.getLength();
		return new Sequence<Tuple>() {

			@Override
			public SequenceIterator<Tuple> iterator() {
				return new SequenceIterator<Tuple>() {

					@Override
					protected Tuple abstractNext() {
						Element[] elements = new Element[tupleLenght];
						for (int i = 0; i < tupleLenght; i++) {
							// the following lines are necessary to use the existing random integer generation on the
							// same iterator
							DeterministicRandomByteSequence rbs = new DeterministicRandomByteSequence() {

								@Override
								public RandomByteSequenceIterator iterator() {
									return iterator;
								}
							};
							elements[i] = (Element) getAt(i).getIndependentGenerators(rbs).get();
						}
						return abstractGetElement(DenseArray.getInstance(elements));
					}

					@Override
					public boolean hasNext() {
						return true;
					}

				};
			}

		};
	}

	@Override
	public final boolean isGenerator(Element element) {
		if (!this.contains(element)) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_ELEMENT, this, element);
		}
		Tuple tuple = (Tuple) element;
		for (int i : this.getAllIndices()) {
			if (!this.getAt(i).isGenerator(tuple.getAt(i))) {
				return false;
			}
		}
		return true;
	}

}
