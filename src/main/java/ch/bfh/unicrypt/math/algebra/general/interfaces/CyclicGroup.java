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
package ch.bfh.unicrypt.math.algebra.general.interfaces;

import ch.bfh.unicrypt.helper.random.RandomByteSequence;
import ch.bfh.unicrypt.helper.random.deterministic.DeterministicRandomByteSequence;
import ch.bfh.unicrypt.helper.sequence.Sequence;

/**
 * This interface represents the mathematical concept a cyclic group. Every element of a cyclic group can be written as
 * a power of some particular element in multiplicative notation, or as a multiple of the element in additive notation.
 * Such an element is called generator of the group. For every positive integer there is exactly one cyclic group (up to
 * isomorphism) with that order, and there is exactly one infinite cyclic group. This interface extends {@link Group}
 * with additional methods for dealing with generators. Each implementing class must provide a default generator.
 * <p>
 * @param <V> The generic type of the values representing the elements of a cyclic group
 * @see "Handbook of Applied Cryptography, Definition 2.167"
 * @see Element
 * <p>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public interface CyclicGroup<V>
	   extends Group<V> {

	/**
	 * Returns a default generator of the cyclic group.
	 * <p>
	 * @return The default generator
	 */
	public Element<V> getDefaultGenerator();

	/**
	 * Derives and returns a sequence of independent generators from the library's default deterministic random byte
	 * sequence.
	 * <p>
	 * @return A sequence of independent generators
	 */
	public Sequence<? extends Element<V>> getIndependentGenerators();

	/**
	 * Derives and returns a sequence of independent generators from a given deterministic random byte sequence.
	 * <p>
	 * @param randomByteSequence The given deterministic random byte sequence.
	 * @return A sequence of independent generators
	 */
	public Sequence<? extends Element<V>> getIndependentGenerators(DeterministicRandomByteSequence randomByteSequence);

	/**
	 * Derives and returns a random generator from the library's default random byte sequence. It is selected uniformly
	 * at random from the set of all generators.
	 * <p>
	 * @return A random generator
	 */
	public Element<V> getRandomGenerator();

	/**
	 * Derives and returns a random generator from the given random byte sequence. It is selected uniformly at random
	 * from the set of all generators.
	 * <p>
	 * @param randomByteSequence The given random byte sequence
	 * @return A random generator
	 */
	public Element<V> getRandomGenerator(RandomByteSequence randomByteSequence);

	/**
	 * Derives and returns a sequence of random generators from the library's default random byte sequence. The random
	 * generators are selected uniformly at random from the set of all generators.
	 * <p>
	 * @return The sequence of random generators
	 */
	public Sequence<? extends Element<V>> getRandomGenerators();

	/**
	 * Derives and returns a sequence of random generators from the given random byte sequence. The random generators
	 * are selected uniformly at random from the set of all generators.
	 * <p>
	 * @param randomByteSequence The given random byte sequence
	 * @return The sequence of random generators
	 */
	public Sequence<? extends Element<V>> getRandomGenerators(RandomByteSequence randomByteSequence);

	/**
	 * Checks if the given element is a generator of the cyclic group.
	 * <p>
	 * @param element The given element
	 * @return {@code true} if the element is a generator, {@code false} otherwise
	 */
	public boolean isGenerator(Element element);

}
