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
package ch.bfh.unicrypt.math.helper.compound;

/**
 *
 * @param <C>
 * @param <T>
 * @author rolfhaenni
 */
public interface Compound<C extends Compound<C, T>, T> {

	/**
	 * The arity of a function is the number of functions applied in parallel or in series when calling the function.
	 * <p>
	 * @return The function's arity
	 */
	public int getArity();

	public boolean isEmpty();

	/**
	 * Checks if a compound function consists of multiple copies of the same function.
	 * <p>
	 * @return {@code true}, if the function is a power function, {@code false} otherwise
	 */
	public boolean isUniform();

	/**
	 * Returns the function at index 0.
	 * <p>
	 * @return The function at index 0
	 * @throws UnsupportedOperationException for functions of arity 0
	 */
	public T getFirst();

	public T getLast();

	/**
	 * Returns the function at the given index.
	 * <p>
	 * @param index The given index
	 * @return The corresponding function
	 * @throws IndexOutOfBoundsException if {
	 * @ode index} is an invalid index
	 */
	public T getAt(int index);

	/**
	 * Returns an array containing all the functions of which {@code this} function is composed of.
	 * <p>
	 * @return The corresponding array of functions
	 */
	public Object[] getAll();

	/**
	 * Creates a new product set which contains one set less than the given product set.
	 * <p>
	 * @param index The index of the set to remove
	 * @return The resulting product set.
	 * @throws IndexOutOfBoundsException if {@code index<0} or {@code index>arity-1}
	 */
	public Compound<C, T> removeAt(final int index);

	public Compound<C, T> insertAt(final int index, final T object);

	public Compound<C, T> replaceAt(final int index, final T object);

	public Compound<C, T> add(final T object);

	public Compound<C, T> append(final Compound<C, T> other);

}
