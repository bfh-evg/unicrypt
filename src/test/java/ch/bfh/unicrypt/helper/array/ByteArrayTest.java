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
 *   but WITHOUT ANY WARRANTY{} without even the implied warranty of
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
package ch.bfh.unicrypt.helper.array;

import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.converter.classes.string.ByteArrayToString;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author R. Haenni <rolf.haenni@bfh.ch>
 */
public class ByteArrayTest {

	private final ArrayList<ByteArray> byteArrayList = new ArrayList<>();
	private final ArrayList<ByteString> byteStringList = new ArrayList<>();
	private final ByteArrayToString converter = ByteArrayToString.getInstance();

	private final byte BYTE_ZERO = (byte) 0L;
	private final byte BYTE_ONE = (byte) 0xFFL;

	public ByteArrayTest() {
		List<String> stringList = new LinkedList<>();
		// trivial cases
		stringList.add("");
		stringList.add("00");
		stringList.add("FF");
		stringList.add("0000");
		stringList.add("00FF");
		stringList.add("FF00");
		stringList.add("FFFF");
		stringList.add("00000000");
		stringList.add("FFFFFFFF");
		// around 8 bytes
		stringList.add("00000000000000");
		stringList.add("FFFFFFFFFFFFFF");
		stringList.add("00FF00FF00FF00");
		stringList.add("FF00FF00FF00FF");
		stringList.add("0123456789ABCD");
		stringList.add("0000000000000000");
		stringList.add("FFFFFFFFFFFFFFFF");
		stringList.add("00FF00FF00FF00FF");
		stringList.add("FF00FF00FF00FF00");
		stringList.add("0123456789ABCDEF");
		stringList.add("000000000000000000");
		stringList.add("FFFFFFFFFFFFFFFFFF");
		stringList.add("00FF00FF00FF00FF00");
		stringList.add("FF00FF00FF00FF00FF");
		stringList.add("0123456789ABCDEF01");
		// around 16 bytes
		stringList.add("0123456789ABCDEF0123456789ABCD");
		stringList.add("0123456789ABCDEF0123456789ABCDEF");
		stringList.add("0123456789ABCDEF0123456789ABCDEF01");
		for (String string : stringList) {
			ByteArray ba = converter.reconvert(string);
			ByteString bs = new ByteString(string);

			byteArrayList.add(ba);
			byteStringList.add(bs);
// COMMENTED FOR EFFICIENCY REASONS
//			byteArrayList.add(ba.reverse().addPrefixAndSuffix(10, 10).extractRange(5, 15));
//			byteStringList.add(bs.reverse().appendPrefixAndSuffix(10, 10).extractRange(5, 15));
		}
	}

	@Test
	public void test_constructor() {
		for (int i = 0; i < byteArrayList.size(); i++) {
			ByteArray ba = byteArrayList.get(i);
			ByteString bs = byteStringList.get(i);

			performTest(ba, bs);
		}
	}

	@Test
	public void test_add() {
		for (int i = 0; i < byteArrayList.size(); i++) {
			ByteArray ba = byteArrayList.get(i);
			ByteString bs = byteStringList.get(i);
			performTest(ba.add(), bs.add());
			performTest(ba.add(BYTE_ZERO), bs.add(BYTE_ZERO));
			performTest(ba.add(BYTE_ONE), bs.add(BYTE_ONE));
			performTest(ba.add(BYTE_ONE).add(BYTE_ZERO), bs.add(BYTE_ONE).add(BYTE_ZERO));
		}
	}

	@Test
	public void test_append() {
		for (int i = 0; i < byteArrayList.size(); i++) {
			ByteArray ba1 = byteArrayList.get(i);
			ByteString bs1 = byteStringList.get(i);
			for (int j = 0; j < byteArrayList.size(); j++) {
				ByteArray ba2 = byteArrayList.get(j);
				ByteString bs2 = byteStringList.get(j);

				performTest(ba1.append(ba2), bs1.append(bs2));
				performTest(ba1.append(ba2).append(ba1), bs1.append(bs2).append(bs1));

			}
		}
	}

	@Test
	public void test_appendPrefix() {
		for (int i = 0; i < byteArrayList.size(); i++) {
			ByteArray ba = byteArrayList.get(i);
			ByteString bs = byteStringList.get(i);

			for (int n = 0; n < 100; n++) {
				performTest(ba.addPrefix(n), bs.appendPrefix(n));
			}
		}
	}

	@Test
	public void test_appendSuffix() {
		for (int i = 0; i < byteArrayList.size(); i++) {
			ByteArray ba = byteArrayList.get(i);
			ByteString bs = byteStringList.get(i);

			for (int n = 0; n < 100; n++) {
				performTest(ba.addSuffix(n), bs.appendSuffix(n));
			}
		}
	}

	@Test
	public void test_appendPrefixAndSuffix() {
		for (int i = 0; i < byteArrayList.size(); i++) {
			ByteArray ba = byteArrayList.get(i);
			ByteString bs = byteStringList.get(i);

			for (int n = 0; n < 66; n++) {
				for (int m = 0; m < 66; m++) {
					performTest(ba.addPrefixAndSuffix(n, m), bs.appendPrefixAndSuffix(n, m));
				}
			}
		}
	}

	@Test
	public void test_count() {
		for (int i = 0; i < byteArrayList.size(); i++) {
			ByteArray ba = byteArrayList.get(i);
			ByteString bs = byteStringList.get(i);

			performTest(ba.count(), bs.count());
			performTest(ba.count(BYTE_ZERO), bs.count(BYTE_ZERO));
			performTest(ba.count(BYTE_ONE), bs.count(BYTE_ONE));
			performTest(ba.countPrefix(), bs.countPrefix());
			performTest(ba.countSuffix(), bs.countSuffix());
			performTest(ba.countPrefix(BYTE_ZERO), bs.countPrefix(BYTE_ZERO));
			performTest(ba.countPrefix(BYTE_ONE), bs.countPrefix(BYTE_ONE));
			performTest(ba.countSuffix(BYTE_ZERO), bs.countSuffix(BYTE_ZERO));
			performTest(ba.countSuffix(BYTE_ONE), bs.countSuffix(BYTE_ONE));
		}
	}

	@Test
	public void test_extract() {
		for (int i = 0; i < byteArrayList.size(); i++) {
			ByteArray ba = byteArrayList.get(i);
			ByteString bs = byteStringList.get(i);

			for (int index = 0; index < ba.getLength(); index++) {
				for (int length = 0; index + length < ba.getLength(); length++) {
					performTest(ba.extract(index, length), bs.extract(index, length));
				}
				for (int toIndex = index; toIndex < ba.getLength(); toIndex++) {
					performTest(ba.extractRange(index, toIndex), bs.extractRange(index, toIndex));
				}
			}
			for (int n = 0; n <= ba.getLength(); n++) {
				performTest(ba.extractPrefix(n), bs.extractPrefix(n));
				performTest(ba.extractSuffix(n), bs.extractSuffix(n));
			}
		}
	}

	@Test
	public void test_getLength() {
		for (int i = 0; i < byteArrayList.size(); i++) {
			ByteArray ba = byteArrayList.get(i);
			ByteString bs = byteStringList.get(i);

			performTest(ba.getLength(), bs.getLength());
		}
	}

	@Test
	public void test_insertAt() {
		for (int i = 0; i < byteArrayList.size(); i++) {
			ByteArray ba = byteArrayList.get(i);
			ByteString bs = byteStringList.get(i);

			for (int index = 0; index <= ba.getLength(); index++) {
				performTest(ba.insertAt(index), bs.insertAt(index));
				performTest(ba.insertAt(index, BYTE_ZERO), bs.insertAt(index, BYTE_ZERO));
				performTest(ba.insertAt(index, BYTE_ONE), bs.insertAt(index, BYTE_ONE));
			}
		}
	}

	@Test
	public void test_remove() {
		for (int i = 0; i < byteArrayList.size(); i++) {
			ByteArray ba = byteArrayList.get(i);
			ByteString bs = byteStringList.get(i);

			performTest(ba.removePrefix(), bs.removePrefix());
			performTest(ba.removeSuffix(), bs.removeSuffix());
			for (int index = 0; index < ba.getLength(); index++) {
				performTest(ba.removeAt(index), bs.removeAt(index));
				for (int toIndex = index; toIndex < ba.getLength(); toIndex++) {
					performTest(ba.removeRange(index, toIndex), bs.removeRange(index, toIndex));
				}
			}
			for (int n = 0; n <= ba.getLength(); n++) {
				performTest(ba.removePrefix(n), bs.removePrefix(n));
				performTest(ba.removeSuffix(n), bs.removeSuffix(n));
			}
		}
	}

	@Test
	public void test_replaceAt() {
		for (int i = 0; i < byteArrayList.size(); i++) {
			ByteArray ba = byteArrayList.get(i);
			ByteString bs = byteStringList.get(i);

			for (int index = 0; index < ba.getLength(); index++) {
				performTest(ba.replaceAt(index), bs.replaceAt(index));
				performTest(ba.replaceAt(index, BYTE_ZERO), bs.replaceAt(index, BYTE_ZERO));
				performTest(ba.replaceAt(index, BYTE_ONE), bs.replaceAt(index, BYTE_ONE));
			}
		}
	}

	@Test
	public void test_reverse() {
		for (int i = 0; i < byteArrayList.size(); i++) {
			ByteArray ba = byteArrayList.get(i);
			ByteString bs = byteStringList.get(i);

			performTest(ba.reverse(), bs.reverse());
			performTest(ba.reverse().reverse(), bs);
			performTest(ba, bs.reverse().reverse());
			performTest(ba.reverse().reverse(), bs.reverse().reverse());
		}
	}

	@Test
	public void test_shift() {
		for (int i = 0; i < byteArrayList.size(); i++) {
			ByteArray ba = byteArrayList.get(i);
			ByteString bs = byteStringList.get(i);

			for (int n = -ba.getLength() - 10; n < ba.getLength() + 10; n++) {
				performTest(ba.shiftLeft(n), bs.shiftLeft(n));
				performTest(ba.shiftRight(n), bs.shiftRight(n));
			}
		}
	}

	@Test
	public void test_getAt() {
		for (int i = 0; i < byteArrayList.size(); i++) {
			ByteArray ba = byteArrayList.get(i);
			ByteString bs = byteStringList.get(i);

			for (int index = 0; index < ba.getLength(); index++) {
				performTest(ba.getAt(index), bs.getAt(index));
			}
		}
	}

	@Test
	public void test_isUniform_isEmpty() {
		for (int i = 0; i < byteArrayList.size(); i++) {
			ByteArray ba = byteArrayList.get(i);
			ByteString bs = byteStringList.get(i);

			performTest(ba.isUniform(), bs.isUniform());
			performTest(ba.isEmpty(), bs.isEmpty());
		}
	}

	@Test
	public void test_not() {
		for (int i = 0; i < byteArrayList.size(); i++) {
			ByteArray ba = byteArrayList.get(i);
			ByteString bs = byteStringList.get(i);
			performTest(ba.not(), bs.not());
			performTest(ba.not().not(), bs);
			performTest(ba, bs.not().not());
		}
	}

	@Test
	public void test_andOrXor() {
		for (int i = 0; i < byteArrayList.size(); i++) {
			ByteArray ba1 = byteArrayList.get(i);
			ByteString bs1 = byteStringList.get(i);
			for (int j = 0; j < byteArrayList.size(); j++) {
				ByteArray ba2 = byteArrayList.get(j);
				ByteString bs2 = byteStringList.get(j);

				performTest(ba1.and(ba2), bs1.and(bs2));
				performTest(ba1.and(ba2, false), bs1.and(bs2, false));
				performTest(ba1.and(ba2, true), bs1.and(bs2, true));

				performTest(ba1.or(ba2), bs1.or(bs2));
				performTest(ba1.or(ba2, false), bs1.or(bs2, false));
				performTest(ba1.or(ba2, true), bs1.or(bs2, true));

				performTest(ba1.xor(ba2), bs1.xor(bs2));
				performTest(ba1.xor(ba2, false), bs1.xor(bs2, false));
				performTest(ba1.xor(ba2, true), bs1.xor(bs2, true));
			}
		}
	}

	public void performTest(boolean a, boolean b) {
		Assert.assertEquals(a, b);
	}

	public void performTest(byte a, byte b) {
		Assert.assertEquals(a, b);
	}

	public void performTest(int a, int b) {
		Assert.assertEquals(a, b);
	}

	public void performTest(ByteArray ba, ByteString bs) {
		Assert.assertEquals(bs.string, converter.convert(ba));
// COMMENTED FOR EFFICIENCY REASONS
//		Assert.assertEquals(bs.reverse().string, converter.convert(ba.reverse()));
	}

	// LOCAL COMPARISON CLASS FOR TESTING (BASED ON STRINGS)
	public class ByteString {

		public String byteToHexString(byte b) {
			return longToHexString(b & 0xFFL);
		}

		public String longToHexString(long l) {
			return String.format("%02X", l);
		}

		public byte hexStringToByte(String s) {
			return (byte) hexStringToLong(s);
		}

		public long hexStringToLong(String s) {
			return Long.parseLong(s, 16);
		}

		String string;

		public ByteString(String string) {
			this.string = string;
		}

		public int getLength() {
			return this.string.length() / 2;
		}

		public boolean isEmpty() {
			return this.string.equals("");
		}

		public int count(Byte value) {
			String str = byteToHexString(value);
			int result = 0;
			for (int i = 0; i < this.getLength(); i++) {
				if (this.string.substring(i * 2, i * 2 + 2).equals(str)) {
					result++;
				}
			}
			return result;
		}

		public int countPrefix(Byte value) {
			String str = byteToHexString(value);
			int result = 0;
			for (int i = 0; i < this.getLength(); i++) {
				if (this.string.substring(i * 2, i * 2 + 2).equals(str)) {
					result++;
				} else {
					return result;
				}
			}
			return result;
		}

		public int countSuffix(Byte value) {
			String str = byteToHexString(value);
			int result = 0;
			for (int i = this.getLength() - 1; i >= 0; i--) {
				if (this.string.substring(i * 2, i * 2 + 2).equals(str)) {
					result++;
				} else {
					return result;
				}
			}
			return result;
		}

		public Byte getAt(int index) {
			return this.hexStringToByte(this.string.substring(index * 2, index * 2 + 2));
		}

		public Byte getFirst() {
			return this.getAt(0);
		}

		public Byte getLast() {
			return this.getAt(this.getLength() - 1);
		}

		public ByteString extract(int fromIndex, int length) {
			return new ByteString(this.string.substring(fromIndex * 2, fromIndex * 2 + length * 2));
		}

		public ByteString extractPrefix(int length) {
			return new ByteString(this.string.substring(0, length * 2));
		}

		public ByteString extractSuffix(int length) {
			return new ByteString(this.string.substring(this.getLength() * 2 - length * 2, this.getLength() * 2));
		}

		public ByteString extractRange(int fromIndex, int toIndex) {
			return new ByteString(this.string.substring(fromIndex * 2, toIndex * 2 + 2));
		}

		public ByteString remove(int fromIndex, int length) {
			return new ByteString(this.string.substring(0, fromIndex * 2)
				   + this.string.substring(fromIndex * 2 + length * 2, this.getLength() * 2));
		}

		public ByteString removePrefix(int n) {
			return new ByteString(this.string.substring(n * 2, this.getLength() * 2));
		}

		public ByteString removeSuffix(int n) {
			return new ByteString(this.string.substring(0, this.getLength() * 2 - n * 2));
		}

		public ByteString removeRange(int fromIndex, int toIndex) {
			return new ByteString(this.string.substring(0, fromIndex * 2)
				   + this.string.substring(toIndex * 2 + 2, this.getLength() * 2));
		}

		public ByteString removeAt(int index) {
			return this.remove(index, 1);
		}

		public ByteString insertAt(final int index, final Byte value) {
			return new ByteString(this.string.substring(0, index * 2)
				   + this.byteToHexString(value)
				   + this.string.substring(index * 2, this.getLength() * 2));
		}

		public ByteString replaceAt(int index, Byte value) {
			return this.removeAt(index).insertAt(index, value);
		}

		public ByteString add(Byte value) {
			return new ByteString(this.string + this.byteToHexString(value));
		}

		public ByteString append(ByteString other) {
			return new ByteString(this.string + other.string);
		}

		public ByteString reverse() {
			String result = "";
			for (int i = 0; i < this.getLength(); i++) {
				result = this.string.substring(i * 2, i * 2 + 2) + result;
			}
			return new ByteString(result);
		}

		public ByteString not() {
			String result = "";
			for (int i = 0; i < this.getLength(); i++) {
				String s = this.string.substring(i * 2, i * 2 + 2);
				String r = longToHexString(~hexStringToLong(s) & 0xFFL);
				result = result + r;
			}
			return new ByteString(result);
		}

		public Byte getDefault() {
			return BYTE_ZERO;
		}

		// collects the indices of default values
		public Iterable<Integer> getIndices() {
			return this.getIndices(false);
		}

		// collects the indices of values different from the default
		public Iterable<Integer> getIndicesExcept() {
			return this.getIndicesExcept(false);
		}

		public int count() {
			return this.count((byte) 0);
		}

		public int countPrefix() {
			return this.countPrefix((byte) 0);
		}

		public int countSuffix() {
			return this.countSuffix((byte) 0);
		}

		public ByteString insertAt(final int index) {
			return this.insertAt(index, (byte) 0);
		}

		public ByteString replaceAt(int index) {
			return this.replaceAt(index, (byte) 0);
		}

		public ByteString add() {
			return this.add(BYTE_ZERO);
		}

		public ByteString appendPrefix(int n) {
			String prefix = "";
			for (int i = 0; i < n; i++) {
				prefix = prefix + "00";
			}
			return new ByteString(prefix + this.string);
		}

		public ByteString appendSuffix(int n) {
			String suffix = "";
			for (int i = 0; i < n; i++) {
				suffix = suffix + "00";
			}
			return new ByteString(this.string + suffix);
		}

		public ByteString appendPrefixAndSuffix(int n, int m) {
			return this.appendPrefix(n).appendSuffix(m);
		}

		public ByteString removePrefix() {
			return this.removePrefix(this.countPrefix());
		}

		public ByteString removeSuffix() {
			return this.removeSuffix(this.countSuffix());
		}

		// left here means making the array smaller
		public ByteString shiftLeft(int n) {
			if (n < 0) {
				return this.shiftRight(-n);
			}
			return this.removePrefix(Math.min(n, this.getLength()));
		}

		// right here means making the array larger and fill up with default value
		public ByteString shiftRight(int n) {
			if (n < 0) {
				return this.shiftLeft(-n);
			}
			return this.appendPrefix(n);
		}

		public ByteString and(ByteString other) {
			String result = "";
			for (int i = 0; i < Math.min(this.getLength(), other.getLength()); i++) {
				String s1 = this.string.substring(i * 2, i * 2 + 2);
				String s2 = other.string.substring(i * 2, i * 2 + 2);
				String s3 = longToHexString(hexStringToLong(s1) & hexStringToLong(s2));
				result = result + s3;
			}
			return new ByteString(result);
		}

		public ByteString and(ByteString other, boolean fillBit) {
			String c = "00";
			if (fillBit) {
				c = "FF";
			}
			String result = "";
			for (int i = 0; i < Math.max(this.getLength(), other.getLength()); i++) {
				String s1 = i < this.getLength() ? this.string.substring(i * 2, i * 2 + 2) : c;
				String s2 = i < other.getLength() ? other.string.substring(i * 2, i * 2 + 2) : c;
				String s3 = longToHexString(hexStringToLong(s1) & hexStringToLong(s2));
				result = result + s3;
			}
			return new ByteString(result);
		}

		public ByteString or(ByteString other) {
			String result = "";
			for (int i = 0; i < Math.min(this.getLength(), other.getLength()); i++) {
				String s1 = this.string.substring(i * 2, i * 2 + 2);
				String s2 = other.string.substring(i * 2, i * 2 + 2);
				String s3 = longToHexString(hexStringToLong(s1) | hexStringToLong(s2));
				result = result + s3;
			}
			return new ByteString(result);
		}

		public ByteString or(ByteString other, boolean fillBit) {
			String c = "00";
			if (fillBit) {
				c = "FF";
			}
			String result = "";
			for (int i = 0; i < Math.max(this.getLength(), other.getLength()); i++) {
				String s1 = i < this.getLength() ? this.string.substring(i * 2, i * 2 + 2) : c;
				String s2 = i < other.getLength() ? other.string.substring(i * 2, i * 2 + 2) : c;
				String s3 = longToHexString(hexStringToLong(s1) | hexStringToLong(s2));
				result = result + s3;
			}
			return new ByteString(result);
		}

		public ByteString xor(ByteString other) {
			String result = "";
			for (int i = 0; i < Math.min(this.getLength(), other.getLength()); i++) {
				String s1 = this.string.substring(i * 2, i * 2 + 2);
				String s2 = other.string.substring(i * 2, i * 2 + 2);
				String s3 = longToHexString((hexStringToLong(s1) & ~hexStringToLong(s2)) | (~hexStringToLong(s1) & hexStringToLong(s2)));
				result = result + s3;
			}
			return new ByteString(result);
		}

		public ByteString xor(ByteString other, boolean fillBit) {
			String c = "00";
			if (fillBit) {
				c = "FF";
			}
			String result = "";
			for (int i = 0; i < Math.max(this.getLength(), other.getLength()); i++) {
				String s1 = i < this.getLength() ? this.string.substring(i * 2, i * 2 + 2) : c;
				String s2 = i < other.getLength() ? other.string.substring(i * 2, i * 2 + 2) : c;
				String s3 = longToHexString((hexStringToLong(s1) & ~hexStringToLong(s2)) | (~hexStringToLong(s1) & hexStringToLong(s2)));
				result = result + s3;
			}
			return new ByteString(result);
		}

		public ByteString[] split(int... indices) {
			return null;
		}

		public boolean isUniform() {
			for (int i = 0; i < this.getLength() - 1; i = i + 2) {
				if (!this.getAt(i).equals(this.getAt(i + 1))) {
					return false;
				}
			}
			return true;
		}

		public Iterable<Integer> getAllIndices() {
			return null;
		}

		public Iterable<Integer> getIndices(Boolean value) {
			return null;
		}

		public Iterable<Integer> getIndicesExcept(Boolean value) {
			return null;
		}

		@Override
		public String toString() {
			return this.string;
		}

	}

}
