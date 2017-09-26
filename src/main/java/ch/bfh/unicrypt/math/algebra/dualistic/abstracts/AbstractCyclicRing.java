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
package ch.bfh.unicrypt.math.algebra.dualistic.abstracts;

import ch.bfh.unicrypt.ErrorCode;
import ch.bfh.unicrypt.UniCryptRuntimeException;
import ch.bfh.unicrypt.helper.random.RandomByteSequence;
import ch.bfh.unicrypt.helper.random.deterministic.DeterministicRandomByteSequence;
import ch.bfh.unicrypt.helper.random.hybrid.HybridRandomByteSequence;
import ch.bfh.unicrypt.helper.sequence.Sequence;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.CyclicRing;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.general.abstracts.AbstractCyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;

/**
 * This abstract class provides a basis implementation for objects of type {@link CyclicRing}. It inherits from
 * {@link AbstractRing}. Some methods from {@link AbstractCyclicGroup} must be re-implemented.
 * <p>
 * @param <E> The generic type of the elements of this cyclic ring
 * @param <V> The generic type of values stored in the elements of this cyclic ring
 * <p>
 * @author R. Haenni
 */
public abstract class AbstractCyclicRing<E extends DualisticElement<V>, V>
	   extends AbstractRing<E, V>
	   implements CyclicRing<V> {

	private static final long serialVersionUID = 1L;

	private E defaultGenerator;

	protected AbstractCyclicRing(Class<?> valueClass) {
		super(valueClass);
	}

	@Override
	public final E getDefaultGenerator() {
		if (this.defaultGenerator == null) {
			this.defaultGenerator = this.abstractGetDefaultGenerator();
		}
		return this.defaultGenerator;
	}

	@Override
	public final Sequence<E> getIndependentGenerators() {
		return this.getIndependentGenerators(DeterministicRandomByteSequence.getInstance());
	}

	@Override
	public final Sequence<E> getIndependentGenerators(DeterministicRandomByteSequence randomByteSequence) {
		if (randomByteSequence == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this);
		}
		return this.defaultGetRandomGenerators(randomByteSequence);
	}

	@Override
	public final E getRandomGenerator() {
		return this.getRandomGenerators().get();
	}

	@Override
	public final E getRandomGenerator(RandomByteSequence randomByteSequence) {
		return this.getRandomGenerators(randomByteSequence).get();
	}

	@Override
	public final Sequence<E> getRandomGenerators() {
		return this.getRandomGenerators(HybridRandomByteSequence.getInstance());
	}

	@Override
	public final Sequence<E> getRandomGenerators(RandomByteSequence randomByteSequence) {
		if (randomByteSequence == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this);
		}
		return this.defaultGetRandomGenerators(randomByteSequence);
	}

	@Override
	public final boolean isGenerator(Element element) {
		if (!this.contains(element)) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_ELEMENT, this, element);
		}
		return this.abstractIsGenerator((E) element);
	}

	// see Handbook of Applied Cryptography, Algorithm 4.80 and Note 4.81
	protected Sequence<E> defaultGetRandomGenerators(RandomByteSequence randomByteSequence) {
		return this.abstractGetRandomElements(randomByteSequence).filter(value -> isGenerator(value));
	}

	@Override
	protected Sequence<E> defaultGetElements() {
		final AbstractCyclicRing<E, V> ring = this;
		return Sequence.getInstance(this.getDefaultGenerator(), element -> ring.apply(ring.getDefaultGenerator(), element))
			   .limit(element -> ring.getIdentityElement().equals(element));
	}

	protected abstract E abstractGetDefaultGenerator();

	protected abstract boolean abstractIsGenerator(E element);

}
