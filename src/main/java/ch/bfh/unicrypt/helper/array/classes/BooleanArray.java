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
import ch.bfh.unicrypt.helper.converter.classes.string.BooleanArrayToString;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class BooleanArray
	   extends AbstractBinaryArray<BooleanArray, Boolean> {

	private final LongArray longArray;

	private BooleanArray(LongArray longArray, int length) {
		this(longArray, length, 0, 0, 0, length);
	}

	private BooleanArray(LongArray longArray, int length, int rangeOffset) {
		this(longArray, length, rangeOffset, 0, 0, length);
	}

	// default value is false, reverse is not used in this class (always false)
	private BooleanArray(LongArray longArray, int length, int rangeOffset, int trailer, int header, int rangeLength) {
		super(BooleanArray.class, length, rangeOffset, false, false, trailer, header, rangeLength);
		this.longArray = longArray;
	}

	private LongArray getLongArray() {
		int arrayLength = MathUtil.divide(this.length - 1, Long.SIZE) + 1;
		long[] longs = new long[arrayLength];
		for (int index = 0; index < arrayLength; index++) {
			longs[index] = this.longArray.getLongAt(this.rangeOffset - this.trailer, index, this.rangeOffset, this.rangeLength);
		}
		return LongArray.getInstance(longs);
	}

	@Override
	protected String defaultToStringValue() {
		String str = BooleanArrayToString.getInstance().convert(this);
		return "\"" + str + "\"";
	}

	@Override
	protected Boolean abstractGetValueAt(int index) {
		int longIndex = index / Long.SIZE;
		long longMask = 1L << (index % Long.SIZE);
		return (this.longArray.getAt(longIndex) & longMask) != 0;
	}

	@Override
	protected BooleanArray abstractGetInstance(int length, int rangeOffset, int rangeLength, int trailer, int header) {
		return new BooleanArray(this.longArray, length, rangeOffset, trailer, header, rangeLength);
	}

	@Override
	protected BooleanArray abstractInsertAt(int index, Boolean value) {
		BooleanArray bitArray1 = this.extractPrefix(index);
		BooleanArray bitArray2 = this.extract(index, this.length - index);
		if (value) {
			bitArray2 = BooleanArray.getInstance(true).append(bitArray2);
		} else {
			bitArray2 = bitArray2.addPrefix(1);
		}
		return bitArray1.append(bitArray2);
	}

	@Override
	protected BooleanArray abstractReplaceAt(int index, Boolean value) {
		BooleanArray bitArray1 = this.extractPrefix(index);
		BooleanArray bitArray2 = this.extract(index + 1, this.length - index - 1);
		if (value) {
			bitArray2 = BooleanArray.getInstance(true).append(bitArray2);
		} else {
			bitArray2 = bitArray2.addPrefix(1);
		}
		return bitArray1.append(bitArray2);
	}

	@Override
	protected BooleanArray abstractAppend(ImmutableArray<Boolean> other) {
		return this.abstractAppend(BooleanArray.getInstance(other));
	}

	protected BooleanArray abstractAppend(BooleanArray other) {
		// make this BooleanArray right-aligned with a LongArray
		int shift = (Long.SIZE - this.length % Long.SIZE) % Long.SIZE;
		LongArray longArray1 = this.addPrefix(shift).getLongArray();
		LongArray longArray2 = other.getLongArray();
		return new BooleanArray(longArray1.append(longArray2), this.length + other.length, shift);
	}

	@Override
	protected BooleanArray abstractReverse() {
		// reverse longArrarangeOffsetust offset, switch trailer and header (reverse is always false)
		int newOffset = this.longArray.getLength() * Long.SIZE - this.rangeOffset - this.rangeLength;
		return new BooleanArray(this.longArray.bitReverse(), this.length, newOffset, this.header, this.trailer, this.rangeLength);
	}

	@Override
	protected BooleanArray abstractAndOrXor(Operator operator, BooleanArray other, boolean maxLength, boolean fillBit) {
		// make the shorter BooleanArray right-aligned with a LongArray
		int minLength = Math.min(this.length, other.length);
		int shift = (Long.SIZE - minLength % Long.SIZE) % Long.SIZE;
		LongArray longArray1 = this.addPrefix(shift).getLongArray();
		LongArray longArray2 = other.addPrefix(shift).getLongArray();
		LongArray result = longArray1.abstractAndOrXor(operator, longArray2, maxLength, fillBit);
		int newLength = maxLength ? Math.max(this.length, other.length) : minLength;
		return new BooleanArray(result, newLength, shift);
	}

	@Override
	protected BooleanArray abstractNot() {
		return new BooleanArray(this.getLongArray().not(), this.length);
	}

	public static BooleanArray getInstance(LongArray longArray) {
		if (longArray == null) {
			throw new IllegalArgumentException();
		}
		return BooleanArray.getInstance(longArray, longArray.getLength() * Long.SIZE);
	}

	public static BooleanArray getInstance(LongArray longArray, int length) {
		if (longArray == null || length < 0 || length > longArray.getLength() * Long.SIZE) {
			throw new IllegalArgumentException();
		}
		return new BooleanArray(longArray, length);
	}

	public static BooleanArray getInstance(boolean... bits) {
		if (bits == null) {
			throw new IllegalArgumentException();
		}
		int longLength = (bits.length + Long.SIZE - 1) / Long.SIZE;
		long[] longs = new long[longLength];
		for (int i = 0; i < bits.length; i++) {
			int longIndex = i / Long.SIZE;
			int bitIndex = i % Long.SIZE;
			if (bits[i]) {
				longs[longIndex] = MathUtil.setBit(longs[longIndex], bitIndex);
			}
		}
		return BooleanArray.getInstance(LongArray.getInstance(longs), bits.length);
	}

	public static BooleanArray getInstance(ImmutableArray<Boolean> immutableArray) {
		if (immutableArray == null) {
			throw new IllegalArgumentException();
		}
		if (immutableArray instanceof BooleanArray) {
			return (BooleanArray) immutableArray;
		}
		int longLength = (immutableArray.getLength() + Long.SIZE - 1) / Long.SIZE;
		long[] longs = new long[longLength];
		for (int i = 0; i < immutableArray.getLength(); i++) {
			int longIndex = i / Long.SIZE;
			int bitIndex = i % Long.SIZE;
			if (immutableArray.getAt(i)) {
				longs[longIndex] = MathUtil.setBit(longs[longIndex], bitIndex);
			}
		}
		return BooleanArray.getInstance(LongArray.getInstance(longs), immutableArray.getLength());
	}

//	public static void main(String[] args) {
//		LongArray la = LongArray.getInstance(0x1000000000000001L, 0xC000000000000003L, 0xE000000000000007L);
//		BooleanArray ba = BooleanArray.getInstance(la);
//		System.out.println(ba);
//		System.out.println(ba.getLongArray());
//		System.out.println(ba.getLongArray(true));
//		System.out.println(ba.append(ba));
//		ba = ba.reverse();
//		System.out.println(ba);
//		System.out.println(ba.getLongArray());
//		System.out.println(ba.getLongArray(true));
//		System.out.println(ba.append(ba));
//		ba = ba.extract(60, 10);
//		System.out.println(ba);
//		System.out.println(ba.getLongArray());
//		System.out.println(ba.getLongArray(true));
//		System.out.println(ba.append(ba));
//		ba = ba.addPrefix(5);
//		System.out.println(ba);
//		System.out.println(ba.getLongArray());
//		System.out.println(ba.getLongArray(true));
//		System.out.println(ba.append(ba));
//		ba = ba.insertAt(7, false);
//		System.out.println(ba);
//		System.out.println(ba.getLongArray());
//		System.out.println(ba.getLongArray(true));
//		System.out.println(ba.append(ba));
//		ba = ba.reverse();
//		System.out.println(ba);
//		System.out.println(ba.getLongArray());
//		System.out.println(ba.getLongArray(true));
//		System.out.println(ba.append(ba));
//		ba = ba.addPrefixAndSuffix(2, 2);
//		System.out.println(ba);
//		System.out.println(ba.getLongArray());
//		System.out.println(ba.getLongArray(true));
//		System.out.println(ba.append(ba));
//		ba = ba.extract(3, 4);
//		System.out.println(ba);
//		System.out.println(ba.getLongArray());
//		System.out.println(ba.getLongArray(true));
//		System.out.println(ba.append(ba));
//		ba = ba.shiftLeft(3);
//		System.out.println(ba);
//		System.out.println(ba.getLongArray());
//		System.out.println(ba.getLongArray(true));
//		System.out.println(ba.append(ba));
//		ba = ba.shiftLeft(1);
//		System.out.println(ba);
//		System.out.println(ba.getLongArray());
//		System.out.println(ba.getLongArray(true));
//		System.out.println(ba.append(ba));
//	}
}
