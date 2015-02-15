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
package ch.bfh.unicrypt.helper.array.classes;

import ch.bfh.unicrypt.helper.MathUtil;
import ch.bfh.unicrypt.helper.array.abstracts.AbstractBinaryArray;
import ch.bfh.unicrypt.helper.array.interfaces.ImmutableArray;
import ch.bfh.unicrypt.helper.converter.classes.string.LongArrayToString;
import java.util.Collection;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class LongArray
	   extends AbstractBinaryArray<LongArray, Long> {

	private static final long ALL_ZERO = 0x0000000000000000L;
	private static final long ALL_ONE = 0xFFFFFFFFFFFFFFFFL;

	private final Long[] longs;
	private final boolean negated;
	private final boolean bitReverse;
	private final boolean byteReverse;

	protected LongArray(Long fillLong, int arrayLength) {
		this(new Long[]{fillLong}, arrayLength);
	}

	protected LongArray(Long[] longs) {
		this(longs, longs.length);
	}

	protected LongArray(Long[] longs, int length) {
		this(longs, length, 0, false, 0, 0, length, false, false, false);
	}

	protected LongArray(Long[] longs, int length, int rangeOffset, boolean reverse, int trailer, int header, int rangeLength, boolean negated, boolean bitReverse, boolean byteReverse) {
		super(LongArray.class, length, rangeOffset, reverse, LongArray.ALL_ZERO, trailer, header, rangeLength);
		this.longs = longs;
		this.negated = negated;
		this.bitReverse = bitReverse;
		this.byteReverse = byteReverse;
		if (longs.length == 1 && length == rangeLength) {
			this.uniform = true;
		}
	}

	public LongArray bitReverse() {
		return new LongArray(this.longs, this.length, this.rangeOffset, !this.reverse, this.header, this.trailer, this.rangeLength, this.negated, !this.bitReverse, this.byteReverse);
	}

	public LongArray byteReverse() {
		return new LongArray(this.longs, this.length, this.rangeOffset, !this.reverse, this.header, this.trailer, this.rangeLength, this.negated, this.bitReverse, !this.byteReverse);
	}

	@Override
	protected String defaultToStringValue() {
		String str = LongArrayToString.getInstance(LongArrayToString.Radix.HEX, "|").convert(this);
		return "\"" + str + "\"";
	}

	public static LongArray getInstance() {
		return new LongArray(new Long[0]);
	}

	public static LongArray getInstance(boolean fillBit, int length) {
		if (fillBit) {
			return LongArray.getInstance(ALL_ONE, length);
		} else {
			return LongArray.getInstance(ALL_ZERO, length);
		}
	}

	public static LongArray getInstance(long fillLong, int length) {
		if (length < 0) {
			throw new IllegalArgumentException();
		}
		return new LongArray(fillLong, length);
	}

	public static LongArray getInstance(long... longs) {
		if (longs == null) {
			throw new IllegalArgumentException();
		}
		Long[] result = new Long[longs.length];
		int i = 0;
		for (long value : longs) {
			result[i++] = value;
		}
		return new LongArray(result);
	}

	public static LongArray getInstance(ImmutableArray<Long> immutableArray) {
		if (immutableArray == null) {
			throw new IllegalArgumentException();
		}
		if (immutableArray instanceof LongArray) {
			return (LongArray) immutableArray;
		}
		Long[] result = new Long[immutableArray.getLength()];
		int i = 0;
		for (Long value : immutableArray) {
			result[i++] = value;
		}
		return new LongArray(result);
	}

	public static LongArray getInstance(Collection<Long> collection) {
		if (collection == null) {
			throw new IllegalArgumentException();
		}
		Long[] result = new Long[collection.size()];
		int i = 0;
		for (Long value : collection) {
			if (value == null) {
				throw new IllegalArgumentException();
			}
			result[i++] = value;
		}
		return new LongArray(result);
	}

	@Override
	protected Long abstractGetValueAt(int index) {
		long result = this.longs[index % this.longs.length];
		if (this.negated) {
			result = MathUtil.not(result);
		}
		if (this.bitReverse) {
			result = Long.reverse(result);
		}
		if (this.byteReverse) {
			result = Long.reverseBytes(result);
		}
		return result;
	}

	@Override
	protected LongArray abstractAppend(ImmutableArray<Long> other) {
		Long[] result = new Long[this.length + other.getLength()];
		for (int i : this.getAllIndices()) {
			result[i] = this.abstractGetAt(i);
		}
		for (int i : other.getAllIndices()) {
			result[this.length + i] = other.getAt(i);
		}
		return new LongArray(result);
	}

	@Override
	protected LongArray abstractInsertAt(int index, Long newLong) {
		Long[] result = new Long[this.length + 1];
		for (int i : this.getAllIndices()) {
			if (i < index) {
				result[i] = this.abstractGetAt(i);
			} else {
				result[i + 1] = this.abstractGetAt(i);
			}
		}
		result[index] = newLong;
		return new LongArray(result);
	}

	@Override
	protected LongArray abstractReplaceAt(int index, Long newLong) {
		Long[] result = new Long[this.length];
		for (int i : this.getAllIndices()) {
			result[i] = this.abstractGetAt(i);
		}
		result[index] = newLong;
		return new LongArray(result);
	}

	@Override
	protected LongArray abstractReverse() {
		// switch trailer and header
		return new LongArray(this.longs, this.length, this.rangeOffset, !this.reverse, this.header, this.trailer, this.rangeLength, this.negated, this.bitReverse, this.byteReverse);
	}

	@Override
	protected LongArray abstractNot() {
		return new LongArray(this.longs, this.length, this.rangeOffset, this.reverse, this.trailer, this.header, this.rangeLength, !this.negated, this.bitReverse, this.byteReverse);
	}

	@Override
	protected LongArray abstractGetInstance(int length, int rangeOffset, int rangeLength, int trailer, int header) {
		return new LongArray(this.longs, length, rangeOffset, this.reverse, trailer, header, rangeLength, this.negated, this.bitReverse, this.byteReverse);
	}

	@Override
	protected LongArray abstractAndOrXor(Operator operator, LongArray other, boolean maxLength, boolean fillBit) {
		Long fillLong = fillBit ? ALL_ONE : ALL_ZERO;
		int newLength = maxLength ? Math.max(this.length, other.length) : Math.min(this.length, other.length);
		Long[] result = new Long[newLength];
		for (int i = 0; i < newLength; i++) {
			long long1 = (i < this.length) ? this.abstractGetAt(i) : fillLong;
			long long2 = (i < other.length) ? other.abstractGetAt(i) : fillLong;
			switch (operator) {
				case AND:
					result[i] = MathUtil.and(long1, long2);
					break;
				case OR:
					result[i] = MathUtil.or(long1, long2);
					break;
				case XOR:
					result[i] = MathUtil.xor(long1, long2);
					break;
			}
		}
		return new LongArray(result);
	}

	// adds an overall, possibly negative shift
	protected long getLongAt(int shift, int index, int rangeOffset, int rangeLength) {
		// Java8: use floorMod and floorDiv to handle negative values properly
		int shiftMod = MathUtil.modulo(shift, Long.SIZE);
		index = index + MathUtil.divide(shift, Long.SIZE);
		long long1 = this.getLongAt(index, rangeOffset, rangeLength);
		if (shiftMod == 0) {
			return long1;
		}
		long long2 = this.getLongAt(index + 1, rangeOffset, rangeLength);
		return MathUtil.or(MathUtil.shiftRight(long1, shiftMod),
						   MathUtil.shiftLeft(long2, Long.SIZE - shiftMod));
	}

	// makes the long array arbitraily long and filters out unused bits
	private long getLongAt(int index, int rangeOffset, int rangeLength) {
		if (index < 0 || index >= this.length || rangeLength == 0) {
			return ALL_ZERO;
		}
		long mask = this.getMaskAt(index, rangeOffset, rangeLength);
		return MathUtil.and(this.abstractGetAt(index), mask);
	}

	// computes the corresponding mask
	private long getMaskAt(int leftIndex, int leftOffset, int rangeLength) {
		int rightIndex = this.length - leftIndex - 1;
		int rightOffset = this.length * Long.SIZE - leftOffset - rangeLength;

		int leftDiv = MathUtil.divide(leftOffset, Long.SIZE);
		int rightDiv = MathUtil.divide(rightOffset, Long.SIZE);

		if (leftIndex < leftDiv || rightIndex < rightDiv) {
			return ALL_ZERO;
		}
		if (leftIndex > leftDiv && rightIndex > rightDiv) {
			return ALL_ONE;
		}
		long mask1 = (leftIndex == leftDiv) ? MathUtil.shiftLeft(ALL_ONE, MathUtil.modulo(leftOffset, Long.SIZE)) : ALL_ONE;
		long mask2 = (rightIndex == rightDiv) ? MathUtil.shiftRight(ALL_ONE, MathUtil.modulo(rightOffset, Long.SIZE)) : ALL_ONE;
		return MathUtil.and(mask1, mask2);
	}

//	public static void main(String[] args) {
//		LongArray l = LongArray.getInstance(0x8000000000000001L, 0xC000000000000003L, 0xE000000000000007L);
//		int rangeLength = 4;
//		int trailer = 0;
//		int header = 0;
//		int length = rangeLength + trailer + header;
//		boolean reverse = false;
//		for (int rangeOffset = 0; rangeOffset < 196 - rangeLength; rangeOffset = rangeOffset + 4) {
//			System.out.println(l.getLongArray(length, rangeOffset, reverse, trailer, header, rangeLength));
//		}
//		System.out.println(l.bitReverse());
//		System.out.println(l.byteReverse());
//		System.out.println(l.not());
//	}
}
