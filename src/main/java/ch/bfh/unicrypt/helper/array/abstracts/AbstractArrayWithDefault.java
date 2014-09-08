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
package ch.bfh.unicrypt.helper.array.abstracts;

import ch.bfh.unicrypt.helper.array.interfaces.ArrayWithDefault;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 * @param <A>
 * @param <T>
 */
abstract public class AbstractArrayWithDefault<A extends AbstractArray<A, T>, T extends Object>
	   extends AbstractArray<A, T>
	   implements ArrayWithDefault<A, T> {

	protected final T defaultObject;
	protected int trailer; // number of trailing zeros not included in byteArray
	protected int header; // number of leading zeros not included in byteArray

	protected AbstractArrayWithDefault(T defaultObject, int trailer, int header, int length, int offset, boolean reverse) {
		super(length, offset, reverse);
		this.defaultObject = defaultObject;
		this.trailer = trailer;
		this.header = header;
		if (header + trailer == length) {
			this.uniform = true;
		}
	}

	@Override
	public final T getDefault() {
		return this.defaultObject;
	}

	@Override
	public Iterable<Integer> getIndices() {
		return this.getIndices(this.defaultObject);
	}

	@Override
	public Iterable<Integer> getIndicesExcept() {
		return this.getIndicesExcept(this.defaultObject);
	}

	@Override
	public int count() {
		return this.count(this.defaultObject);
	}

	@Override
	public int countPrefix() {
		return this.countPrefix(this.defaultObject);
	}

	@Override
	public int countSuffix() {
		return this.countSuffix(this.defaultObject);
	}

	@Override
	public A insertAt(int index) {
		return this.insertAt(index, this.defaultObject);
	}

	@Override
	public A replaceAt(int index) {
		return this.replaceAt(index, this.defaultObject);
	}

	@Override
	public A add() {
		return this.add(this.defaultObject);
	}

	@Override
	public A append(int n) {
		return this.abstractAppend(n);
	}

	@Override
	public final A shiftLeft(int n) {
		if (n < 0) {
			return this.shiftRight(-n);
		}
		return this.removePrefix(Math.min(this.getLength(), n));
	}

	@Override
	public final A shiftRight(int n) {
		if (n <= 0) {
			return this.shiftLeft(-n);
		}
		return this.abstractShiftRight(n);
	}

	protected abstract A abstractAppend(int n);

	protected abstract A abstractShiftRight(int n);

}
