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

import ch.bfh.unicrypt.helper.array.interfaces.DefaultValueArray;
import ch.bfh.unicrypt.helper.sequence.Sequence;

/**
 * This abstract class serves as a base implementation of the {@link DefaultValueArray} interface.
 * <p>
 * @author R. Haenni
 * @version 2.0
 * @param <A> The type of a potential non-generic sub-class
 * @param <V> The generic type of the values in the immutable array with a default value
 */
abstract public class AbstractDefaultValueArray<A extends AbstractDefaultValueArray<A, V>, V>
	   extends AbstractImmutableArray<A, V>
	   implements DefaultValueArray<V> {

	private static final long serialVersionUID = 1L;

	protected final V defaultValue;
	protected int trailer; // number of trailing zeros not included in byteArray
	protected int header; // number of leading zeros not included in byteArray
	protected int rangeLength; // rangeLength = length - header - trailer

	protected AbstractDefaultValueArray(Class valueClass, V defaultValue, int length, int rangeOffset, int rangeLength,
		   int trailer, int header, boolean reverse) {
		super(valueClass, length, rangeOffset, reverse);
		this.defaultValue = defaultValue;
		this.trailer = trailer;
		this.header = header;
		this.rangeLength = rangeLength;
		if (rangeLength == 0) {
			this.uniform = true;
		}
	}

	@Override
	public final V getDefault() {
		return this.defaultValue;
	}

	@Override
	public final Sequence<Integer> getIndices() {
		return this.getIndices(this.defaultValue);
	}

	@Override
	public final Sequence<Integer> getIndicesExcept() {
		return this.getIndicesExcept(this.defaultValue);
	}

	@Override
	public final int count() {
		return this.count(this.defaultValue);
	}

	@Override
	public final int countExcept() {
		return this.getLength() - this.count();
	}

	@Override
	public final int countPrefix() {
		return this.countPrefix(this.defaultValue);
	}

	@Override
	public final int countSuffix() {
		return this.countSuffix(this.defaultValue);
	}

	@Override
	public final A insertAt(int index) {
		return this.insertAt(index, this.defaultValue);
	}

	@Override
	public final A insert() {
		return this.insert(this.defaultValue);
	}

	@Override
	public final A add() {
		return this.add(this.defaultValue);
	}

	@Override
	public final A replaceAt(int index) {
		return this.replaceAt(index, this.defaultValue);
	}

	@Override
	public final A addPrefix(int n) {
		if (n < 0) {
			throw new IllegalArgumentException();
		}
		int newLength = this.length + n;
		int newTrailer = this.trailer + n;
		return this.abstractGetInstance(newLength, this.rangeOffset, this.rangeLength, newTrailer, this.header);
	}

	@Override
	public final A addSuffix(int n) {
		if (n < 0) {
			throw new IllegalArgumentException();
		}
		int newLength = this.length + n;
		int newHeader = this.header + n;
		return this.abstractGetInstance(newLength, this.rangeOffset, this.rangeLength, this.trailer, newHeader);
	}

	@Override
	public final A addPrefixAndSuffix(int n, int m) {
		if (n < 0 || m < 0) {
			throw new IllegalArgumentException();
		}
		int newLength = this.length + n + m;
		int newTrailer = this.trailer + n;
		int newHeader = this.header + m;
		return this.abstractGetInstance(newLength, this.rangeOffset, this.rangeLength, newTrailer, newHeader);
	}

	@Override
	public final A removePrefix() {
		return this.removePrefix(this.countPrefix());
	}

	@Override
	public final A removeSuffix() {
		return this.removeSuffix(this.countSuffix());
	}

	@Override
	public final A removePrefixAndSuffix() {
		return this.removePrefix().removeSuffix();
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
		return this.addPrefix(n);
	}

	@Override
	protected final V abstractGetAt(int index) {
		if (index < this.trailer || index >= this.length - this.header) {
			return this.defaultValue;
		}
		int rangeIndex = index - this.trailer;
		if (this.reverse) {
			rangeIndex = this.rangeLength - rangeIndex - 1;
		}
		return this.abstractGetValueAt(this.rangeOffset + rangeIndex);
	}

	@Override
	protected A abstractExtract(int index, int length) {
		// adjust header and trailer
		int newTrailer = Math.min(Math.max(0, this.trailer - index), length);
		int newHeader = Math.min(Math.max(0, this.header - (this.length - index - length)), length);
		int newRangeLength = length - newTrailer - newHeader;
		// adjust rangeOffset and rangeLength
		int rangeIndex = Math.max(0, index - this.trailer);
		if (this.reverse) {
			rangeIndex = this.rangeLength - rangeIndex - newRangeLength;
		}
		// construct instance
		return this.abstractGetInstance(length, this.rangeOffset + rangeIndex, newRangeLength, newTrailer, newHeader);
	}

	abstract protected V abstractGetValueAt(int index);

	protected abstract A abstractGetInstance(int length, int rangeOffset, int rangeLength, int trailer, int header);

}
