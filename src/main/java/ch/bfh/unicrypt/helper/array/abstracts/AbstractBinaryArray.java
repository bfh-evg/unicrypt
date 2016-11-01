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
package ch.bfh.unicrypt.helper.array.abstracts;

import ch.bfh.unicrypt.helper.array.interfaces.BinaryArray;

/**
 * This abstract class serves as a base implementation of the {@link BinaryArray} interface.
 * <p>
 * @author R. Haenni
 * @version 2.0
 * @param <A> The type of a potential non-generic sub-class
 * @param <V> The generic type of the values in the binary array
 */
public abstract class AbstractBinaryArray<A extends AbstractBinaryArray<A, V>, V>
	   extends AbstractDefaultValueArray<A, V>
	   implements BinaryArray<V> {

	private static final long serialVersionUID = 1L;

	protected enum Operator {

		AND, OR, XOR

	};

	protected AbstractBinaryArray(Class valueClass, int length, int rangeOffset, boolean reverse, V defaultValue,
		   int trailer, int header, int rangeLength) {
		super(valueClass, defaultValue, length, rangeOffset, rangeLength, trailer, header, reverse);
	}

	@Override
	public A and(BinaryArray<V> other) {
		return this.andOrXor(Operator.AND, other, false, false);
	}

	@Override
	public A or(BinaryArray<V> other) {
		return this.andOrXor(Operator.OR, other, false, false);
	}

	@Override
	public A xor(BinaryArray<V> other) {
		return this.andOrXor(Operator.XOR, other, false, false);
	}

	@Override
	public A and(BinaryArray<V> other, boolean fillBit) {
		return this.andOrXor(Operator.AND, other, true, fillBit);
	}

	@Override
	public A or(BinaryArray<V> other, boolean fillBit) {
		return this.andOrXor(Operator.OR, other, true, fillBit);
	}

	@Override
	public A xor(BinaryArray<V> other, boolean fillBit) {
		return this.andOrXor(Operator.XOR, other, true, fillBit);
	}

	@Override
	public A not() {
		return this.abstractNot();
	}

	protected A andOrXor(Operator operator, BinaryArray<V> other, boolean maxLength, boolean fillBit) {
		if (other == null || this.getBaseClass() != other.getBaseClass()) {
			throw new IllegalArgumentException();
		}
		return this.abstractAndOrXor(operator, (A) other, maxLength, fillBit);

	}

	abstract protected A abstractAndOrXor(Operator operator, A other, boolean maxLength, boolean fillBit);

	abstract protected A abstractNot();

}
