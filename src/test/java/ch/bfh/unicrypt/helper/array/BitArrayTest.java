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

import ch.bfh.unicrypt.helper.array.classes.BitArray;
import ch.bfh.unicrypt.helper.converter.classes.string.BitArrayToString;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author R. Haenni <rolf.haenni@bfh.ch>
 */
public class BitArrayTest {

	ArrayList<BitArray> bitArrayList = new ArrayList<>();
	ArrayList<BooleanString> booleanStringList = new ArrayList<>();
	BitArrayToString converter = BitArrayToString.getInstance();

	public BitArrayTest() {
		List<String> stringList = new LinkedList<>();
		// trivial cases
		stringList.add("");
		stringList.add("0");
		stringList.add("1");
		stringList.add("00");
		stringList.add("01");
		stringList.add("10");
		stringList.add("11");
		stringList.add("00000000");
		stringList.add("11111111");
		// around 64 bits
		stringList.add("000000000000000000000000000000000000000000000000000000000000000");
		stringList.add("111111111111111111111111111111111111111111111111111111111111111");
		stringList.add("000111010010101010100101000101111010110101010101010101010010110");
		stringList.add("000111010010101010100101000101111010110101010101011101010010111");
		stringList.add("100111010010101010100101000101111010110101010101011011010010110");
		stringList.add("100111010010101010100101000101111010110101010101011010110010111");
		stringList.add("0000000000000000000000000000000000000000000000000000000000000000");
		stringList.add("1111111111111111111111111111111111111111111111111111111111111111");
		stringList.add("0001110100101010101001010001011110101101010101010110101010010110");
		stringList.add("0001110100101010101001010001011110101101010101010110101010010111");
		stringList.add("1001110100101010101001010001011110101101010101010110101010010110");
		stringList.add("1001110100101010101001010001011110101101010101010110101010010111");
		stringList.add("00000000000000000000000000000000000000000000000000000000000000000");
		stringList.add("11111111111111111111111111111111111111111111111111111111111111111");
		stringList.add("00011101001010101010010100010111101011010101010101101010100101100");
		stringList.add("00011101001010101010010100010111101011010101010101101010100101111");
		stringList.add("10011101001010101010010100010111101011010101010101101010100101100");
		stringList.add("10011101001010101010010100010111101011010101010101101010100101111");
		// around 128 bits
		stringList.add("0001110100101010101001010001011110101101010101010110101010010110000111010010101010100101000101111010110101010101011010101001010");
		stringList.add("00011101001010101010010100010111101011010101010101101010100101100001110100101010101001010001011110101101010101010110101010010110");
		stringList.add("000111010010101010100101000101111010110101010101011010101001011000011101001010101010010100010111101011010101010101101010100101100");
		for (String string : stringList) {
			BitArray ba = converter.reconvert(string);
			BooleanString bs = new BooleanString(string);

			bitArrayList.add(ba);
			booleanStringList.add(bs);
// COMMENTED FOR EFFICIENCY REASONS
//			bitArrayList.add(ba.reverse().addPrefixAndSuffix(10, 10).extractRange(5, 15));
//			booleanStringList.add(bs.reverse().appendPrefixAndSuffix(10, 10).extractRange(5, 15));
		}
	}

	@Test
	public void test_constructor() {
		for (int i = 0; i < bitArrayList.size(); i++) {
			BitArray ba = bitArrayList.get(i);
			BooleanString bs = booleanStringList.get(i);

			performTest(ba, bs);
		}
	}

	@Test
	public void test_add() {
		for (int i = 0; i < bitArrayList.size(); i++) {
			BitArray ba = bitArrayList.get(i);
			BooleanString bs = booleanStringList.get(i);

			performTest(ba.add(), bs.add());
			performTest(ba.add(false), bs.add(false));
			performTest(ba.add(true), bs.add(true));
			performTest(ba.add(true).add(false), bs.add(true).add(false));
		}
	}

	@Test
	public void test_append() {
		for (int i = 0; i < bitArrayList.size(); i++) {
			BitArray ba1 = bitArrayList.get(i);
			BooleanString bs1 = booleanStringList.get(i);
			for (int j = 0; j < bitArrayList.size(); j++) {
				BitArray ba2 = bitArrayList.get(j);
				BooleanString bs2 = booleanStringList.get(j);

				performTest(ba1.append(ba2), bs1.append(bs2));
				performTest(ba1.append(ba2).append(ba1), bs1.append(bs2).append(bs1));

			}
		}
	}

	@Test
	public void test_appendPrefix() {
		for (int i = 0; i < bitArrayList.size(); i++) {
			BitArray ba = bitArrayList.get(i);
			BooleanString bs = booleanStringList.get(i);

			for (int n = 0; n < 100; n++) {
				performTest(ba.addPrefix(n), bs.appendPrefix(n));
			}
		}
	}

	@Test
	public void test_appendSuffix() {
		for (int i = 0; i < bitArrayList.size(); i++) {
			BitArray ba = bitArrayList.get(i);
			BooleanString bs = booleanStringList.get(i);

			for (int n = 0; n < 100; n++) {
				performTest(ba.addSuffix(n), bs.appendSuffix(n));
			}
		}
	}

	@Test
	public void test_appendPrefixAndSuffix() {
		for (int i = 0; i < bitArrayList.size(); i++) {
			BitArray ba = bitArrayList.get(i);
			BooleanString bs = booleanStringList.get(i);

			for (int n = 0; n < 66; n++) {
				for (int m = 0; m < 66; m++) {
					performTest(ba.addPrefixAndSuffix(n, m), bs.appendPrefixAndSuffix(n, m));
				}
			}
		}
	}

	@Test
	public void test_count() {
		for (int i = 0; i < bitArrayList.size(); i++) {
			BitArray ba = bitArrayList.get(i);
			BooleanString bs = booleanStringList.get(i);

			performTest(ba.count(), bs.count());
			performTest(ba.count(false), bs.count(false));
			performTest(ba.count(true), bs.count(true));
			performTest(ba.countPrefix(), bs.countPrefix());
			performTest(ba.countSuffix(), bs.countSuffix());
			performTest(ba.countPrefix(false), bs.countPrefix(false));
			performTest(ba.countPrefix(true), bs.countPrefix(true));
			performTest(ba.countSuffix(false), bs.countSuffix(false));
			performTest(ba.countSuffix(true), bs.countSuffix(true));
		}
	}

	@Test
	public void test_extract() {
		for (int i = 0; i < bitArrayList.size(); i++) {
			BitArray ba = bitArrayList.get(i);
			BooleanString bs = booleanStringList.get(i);

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
		for (int i = 0; i < bitArrayList.size(); i++) {
			BitArray ba = bitArrayList.get(i);
			BooleanString bs = booleanStringList.get(i);

			performTest(ba.getLength(), bs.getLength());
		}
	}

	@Test
	public void test_insertAt() {
		for (int i = 0; i < bitArrayList.size(); i++) {
			BitArray ba = bitArrayList.get(i);
			BooleanString bs = booleanStringList.get(i);

			for (int index = 0; index <= ba.getLength(); index++) {
				performTest(ba.insertAt(index), bs.insertAt(index));
				performTest(ba.insertAt(index, false), bs.insertAt(index, false));
				performTest(ba.insertAt(index, true), bs.insertAt(index, true));
			}
		}
	}

	@Test
	public void test_remove() {
		for (int i = 0; i < bitArrayList.size(); i++) {
			BitArray ba = bitArrayList.get(i);
			BooleanString bs = booleanStringList.get(i);

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
		for (int i = 0; i < bitArrayList.size(); i++) {
			BitArray ba = bitArrayList.get(i);
			BooleanString bs = booleanStringList.get(i);

			for (int index = 0; index < ba.getLength(); index++) {
				performTest(ba.replaceAt(index), bs.replaceAt(index));
				performTest(ba.replaceAt(index, false), bs.replaceAt(index, false));
				performTest(ba.replaceAt(index, true), bs.replaceAt(index, true));
			}
		}
	}

	@Test
	public void test_reverse() {
		for (int i = 0; i < bitArrayList.size(); i++) {
			BitArray ba = bitArrayList.get(i);
			BooleanString bs = booleanStringList.get(i);

			performTest(ba.reverse(), bs.reverse());
			performTest(ba.reverse().reverse(), bs);
			performTest(ba, bs.reverse().reverse());
			performTest(ba.reverse().reverse(), bs.reverse().reverse());
		}
	}

	@Test
	public void test_shift() {
		for (int i = 0; i < bitArrayList.size(); i++) {
			BitArray ba = bitArrayList.get(i);
			BooleanString bs = booleanStringList.get(i);

			for (int n = -ba.getLength() - 10; n < ba.getLength() + 10; n++) {
				performTest(ba.shiftLeft(n), bs.shiftLeft(n));
				performTest(ba.shiftRight(n), bs.shiftRight(n));
			}
		}
	}

	@Test
	public void test_getAt() {
		for (int i = 0; i < bitArrayList.size(); i++) {
			BitArray ba = bitArrayList.get(i);
			BooleanString bs = booleanStringList.get(i);

			for (int index = 0; index < ba.getLength(); index++) {
				performTest(ba.getAt(index), bs.getAt(index));
			}
		}
	}

	@Test
	public void test_isUniform_isEmpty() {
		for (int i = 0; i < bitArrayList.size(); i++) {
			BitArray ba = bitArrayList.get(i);
			BooleanString bs = booleanStringList.get(i);

			performTest(ba.isUniform(), bs.isUniform());
			performTest(ba.isEmpty(), bs.isEmpty());
		}
	}

	@Test
	public void test_isUniform_not() {
		for (int i = 0; i < bitArrayList.size(); i++) {
			BitArray ba = bitArrayList.get(i);
			BooleanString bs = booleanStringList.get(i);
			performTest(ba.not(), bs.not());
			performTest(ba.not().not(), bs);
			performTest(ba, bs.not().not());
		}
	}

	@Test
	public void test_andOrXor() {
		for (int i = 0; i < bitArrayList.size(); i++) {
			BitArray ba1 = bitArrayList.get(i);
			BooleanString bs1 = booleanStringList.get(i);
			for (int j = 0; j < bitArrayList.size(); j++) {
				BitArray ba2 = bitArrayList.get(j);
				BooleanString bs2 = booleanStringList.get(j);

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

	public void performTest(int a, int b) {
		Assert.assertEquals(a, b);
	}

	public void performTest(BitArray ba, BooleanString bs) {
		Assert.assertEquals(bs.string, converter.convert(ba));
// COMMENTED FOR EFFICIENCY REASONS
//		Assert.assertEquals(bs.reverse().string, converter.convert(ba.reverse()));
	}

	// LOCAL COMPARISON CLASS FOR TESTING (BASED ON STRINGS)
	public class BooleanString {

		String string;

		public BooleanString(String string) {
			this.string = string;
		}

		public int getLength() {
			return this.string.length();
		}

		public boolean isEmpty() {
			return this.string.equals("");
		}

		public int count(Boolean value) {
			char c;
			if (value) {
				c = '1';
			} else {
				c = '0';
			}
			int result = 0;
			for (int i = 0; i < this.string.length(); i++) {
				if (this.string.charAt(i) == c) {
					result++;
				}
			}
			return result;
		}

		public int countPrefix(Boolean value) {
			char c;
			if (value) {
				c = '1';
			} else {
				c = '0';
			}
			int result = 0;
			for (int i = 0; i < this.string.length(); i++) {
				if (this.string.charAt(i) == c) {
					result++;
				} else {
					return result;
				}
			}
			return result;
		}

		public int countSuffix(Boolean value) {
			char c;
			if (value) {
				c = '1';
			} else {
				c = '0';
			}
			int result = 0;
			for (int i = this.string.length() - 1; i >= 0; i--) {
				if (this.string.charAt(i) == c) {
					result++;
				} else {
					return result;
				}
			}
			return result;
		}

		public Boolean getAt(int index) {
			return this.string.charAt(index) == '1';
		}

		public Boolean getFirst() {
			return this.getAt(0);
		}

		public Boolean getLast() {
			return this.getAt(this.string.length() - 1);
		}

		public BooleanString extract(int fromIndex, int length) {
			return new BooleanString(this.string.substring(fromIndex, fromIndex + length));
		}

		public BooleanString extractPrefix(int length) {
			return new BooleanString(this.string.substring(0, length));
		}

		public BooleanString extractSuffix(int length) {
			return new BooleanString(this.string.substring(this.string.length() - length, this.string.length()));
		}

		public BooleanString extractRange(int fromIndex, int toIndex) {
			return new BooleanString(this.string.substring(fromIndex, toIndex + 1));
		}

		public BooleanString remove(int fromIndex, int length) {
			return new BooleanString(this.string.substring(0, fromIndex)
				   + this.string.substring(fromIndex + length, this.string.length()));
		}

		public BooleanString removePrefix(int n) {
			return new BooleanString(this.string.substring(n, this.string.length()));
		}

		public BooleanString removeSuffix(int n) {
			return new BooleanString(this.string.substring(0, this.string.length() - n));
		}

		public BooleanString removeRange(int fromIndex, int toIndex) {
			return new BooleanString(this.string.substring(0, fromIndex)
				   + this.string.substring(toIndex + 1, this.string.length()));
		}

		public BooleanString removeAt(int index) {
			return this.remove(index, 1);
		}

		public BooleanString insertAt(final int index, final Boolean value) {
			return new BooleanString(this.string.substring(0, index)
				   + (value ? "1" : "0")
				   + this.string.substring(index, this.string.length()));
		}

		public BooleanString replaceAt(int index, Boolean value) {
			return this.removeAt(index).insertAt(index, value);
		}

		public BooleanString add(Boolean value) {
			if (value) {
				return new BooleanString(this.string + "1");
			}
			return new BooleanString(this.string + "0");
		}

		public BooleanString append(BooleanString other) {
			return new BooleanString(this.string + other.string);
		}

		public BooleanString reverse() {
			String result = "";
			for (int i = 0; i < this.string.length(); i++) {
				result = this.string.substring(i, i + 1) + result;
			}
			return new BooleanString(result);
		}

		public BooleanString not() {
			String result = "";
			for (int i = 0; i < this.string.length(); i++) {
				result = result + (this.string.charAt(i) == '0' ? "1" : "0");
			}
			return new BooleanString(result);
		}

		public Boolean getDefault() {
			return false;
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
			return this.count(false);
		}

		public int countPrefix() {
			return this.countPrefix(false);
		}

		public int countSuffix() {
			return this.countSuffix(false);
		}

		public BooleanString insertAt(final int index) {
			return this.insertAt(index, false);
		}

		public BooleanString replaceAt(int index) {
			return this.replaceAt(index, false);
		}

		public BooleanString add() {
			return this.add(false);
		}

		public BooleanString appendPrefix(int n) {
			String prefix = "";
			for (int i = 0; i < n; i++) {
				prefix = prefix + "0";
			}
			return new BooleanString(prefix + this.string);
		}

		public BooleanString appendSuffix(int n) {
			String suffix = "";
			for (int i = 0; i < n; i++) {
				suffix = suffix + "0";
			}
			return new BooleanString(this.string + suffix);
		}

		public BooleanString appendPrefixAndSuffix(int n, int m) {
			return this.appendPrefix(n).appendSuffix(m);
		}

		public BooleanString removePrefix() {
			return this.removePrefix(this.countPrefix());
		}

		public BooleanString removeSuffix() {
			return this.removeSuffix(this.countSuffix());
		}

		// left here means making the array smaller
		public BooleanString shiftLeft(int n) {
			if (n < 0) {
				return this.shiftRight(-n);
			}
			return this.removePrefix(Math.min(n, this.getLength()));
		}

		// right here means making the array larger and fill up with default value
		public BooleanString shiftRight(int n) {
			if (n < 0) {
				return this.shiftLeft(-n);
			}
			return this.appendPrefix(n);
		}

		public BooleanString and(BooleanString other) {
			String result = "";
			for (int i = 0; i < Math.min(this.getLength(), other.getLength()); i++) {
				char c1 = this.string.charAt(i);
				char c2 = other.string.charAt(i);
				if (c1 == '1' && c2 == '1') {
					result = result + "1";
				} else {
					result = result + "0";
				}
			}
			return new BooleanString(result);
		}

		public BooleanString and(BooleanString other, boolean fillBit) {
			char c = '0';
			if (fillBit) {
				c = '1';
			}
			String result = "";
			for (int i = 0; i < Math.max(this.getLength(), other.getLength()); i++) {
				char c1 = i < this.getLength() ? this.string.charAt(i) : c;
				char c2 = i < other.getLength() ? other.string.charAt(i) : c;
				if (c1 == '1' && c2 == '1') {
					result = result + "1";
				} else {
					result = result + "0";
				}
			}
			return new BooleanString(result);
		}

		public BooleanString or(BooleanString other) {
			String result = "";
			for (int i = 0; i < Math.min(this.getLength(), other.getLength()); i++) {
				char c1 = this.string.charAt(i);
				char c2 = other.string.charAt(i);
				if (c1 == '1' || c2 == '1') {
					result = result + "1";
				} else {
					result = result + "0";
				}
			}
			return new BooleanString(result);
		}

		public BooleanString or(BooleanString other, boolean fillBit) {
			char c = '0';
			if (fillBit) {
				c = '1';
			}
			String result = "";
			for (int i = 0; i < Math.max(this.getLength(), other.getLength()); i++) {
				char c1 = i < this.getLength() ? this.string.charAt(i) : c;
				char c2 = i < other.getLength() ? other.string.charAt(i) : c;
				if (c1 == '1' || c2 == '1') {
					result = result + "1";
				} else {
					result = result + "0";
				}
			}
			return new BooleanString(result);
		}

		public BooleanString xor(BooleanString other) {
			String result = "";
			for (int i = 0; i < Math.min(this.getLength(), other.getLength()); i++) {
				char c1 = this.string.charAt(i);
				char c2 = other.string.charAt(i);
				if (c1 == '1' && c2 == '0' || c1 == '0' && c2 == '1') {
					result = result + "1";
				} else {
					result = result + "0";
				}
			}
			return new BooleanString(result);
		}

		public BooleanString xor(BooleanString other, boolean fillBit) {
			char c = '0';
			if (fillBit) {
				c = '1';
			}
			String result = "";
			for (int i = 0; i < Math.max(this.getLength(), other.getLength()); i++) {
				char c1 = i < this.getLength() ? this.string.charAt(i) : c;
				char c2 = i < other.getLength() ? other.string.charAt(i) : c;
				if (c1 == '1' && c2 == '0' || c1 == '0' && c2 == '1') {
					result = result + "1";
				} else {
					result = result + "0";
				}
			}
			return new BooleanString(result);
		}

		public BooleanString[] split(int... indices) {
			return null;
		}

		public boolean isUniform() {
			return this.string.matches("^(0*|1*)$");
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

	}

}
