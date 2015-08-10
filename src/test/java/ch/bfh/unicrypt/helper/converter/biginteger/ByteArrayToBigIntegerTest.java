/*
 * UniCrypt
 *
 *  UniCrypt(tm) : Cryptographical framework allowing the implementation of cryptographic protocols e.g. e-voting
 *  Copyright (C) 2015 Bern University of Applied Sciences (BFH), Research Institute for
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
package ch.bfh.unicrypt.helper.converter.biginteger;

import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.converter.classes.biginteger.ByteArrayToBigInteger;
import java.math.BigInteger;
import org.junit.Assert;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author R. Haenni
 */
public class ByteArrayToBigIntegerTest {

	ByteArray ba0 = ByteArray.getInstance("");
	ByteArray ba11 = ByteArray.getInstance("00");
	ByteArray ba12 = ByteArray.getInstance("01");
	ByteArray ba13 = ByteArray.getInstance("02");
	ByteArray ba14 = ByteArray.getInstance("FF");
	ByteArray ba21 = ByteArray.getInstance("00|00");
	ByteArray ba22 = ByteArray.getInstance("00|01");
	ByteArray ba23 = ByteArray.getInstance("00|02");
	ByteArray ba24 = ByteArray.getInstance("FF|FF");
	ByteArray ba31 = ByteArray.getInstance("00|00|00");
	ByteArray ba32 = ByteArray.getInstance("00|00|01");
	ByteArray ba33 = ByteArray.getInstance("00|00|02");
	ByteArray ba34 = ByteArray.getInstance("FF|FF|FF");
	ByteArray ba41 = ByteArray.getInstance("00|00|00|00");
	ByteArray ba42 = ByteArray.getInstance("00|00|00|01");
	ByteArray ba43 = ByteArray.getInstance("00|00|00|02");
	ByteArray ba44 = ByteArray.getInstance("FF|FF|FF|FF");

	@Test
	public void test_without_blockLenght_and_minBlocks() {
		ByteArrayToBigInteger converter = ByteArrayToBigInteger.getInstance();
		ByteArray[] bas = {ba0, ba11, ba12, ba13, ba14, ba21, ba22, ba23, ba24, ba31, ba32, ba33, ba34, ba41, ba42, ba43, ba44};
		int o1 = 1;
		int o2 = 1 + 256;
		int o3 = 1 + 256 + 65536;
		int o4 = 1 + 256 + 65536 + 16777216;
		long[] expected = {
			0,
			o1 + 0, o1 + 1, o1 + 2, o1 + 255,
			o2 + 0, o2 + 1, o2 + 2, o2 + 65535,
			o3 + 0, o3 + 1, o3 + 2, o3 + 16777215,
			o4 + 0, o4 + 1, o4 + 2, o4 + 4294967295L};
		for (int i = 0; i < expected.length; i++) {
			BigInteger exp = BigInteger.valueOf(expected[i]);
			BigInteger result = converter.convert(bas[i]);
			Assert.assertEquals(exp, result);
			Assert.assertEquals(bas[i], converter.reconvert(result));
		}
		try {
			converter.reconvert(BigInteger.valueOf(-1));
			fail();
		} catch (Exception e) {
		}
	}

	@Test
	public void test_with_blockLenght_equals_2() {
		ByteArrayToBigInteger converter = ByteArrayToBigInteger.getInstance(2, 0);
		ByteArray[] bas = {ba0, ba21, ba22, ba23, ba24, ba41, ba42, ba43, ba44};
		int o2 = 1;
		int o4 = 1 + 65536;
		long[] expected = {
			0,
			o2 + 0, o2 + 1, o2 + 2, o2 + 65535,
			o4 + 0, o4 + 1, o4 + 2, o4 + 4294967295L};
		for (int i = 0; i < expected.length; i++) {
			BigInteger exp = BigInteger.valueOf(expected[i]);
			BigInteger result = converter.convert(bas[i]);
			Assert.assertEquals(exp, result);
			Assert.assertEquals(bas[i], converter.reconvert(result));
		}
		try {
			converter.reconvert(BigInteger.valueOf(-1));
			fail();
		} catch (Exception e) {
		}
	}

	@Test
	public void test_with_blockLenght_equals_3() {
		ByteArrayToBigInteger converter = ByteArrayToBigInteger.getInstance(3, 0);
		ByteArray[] bas = {ba0, ba31, ba32, ba33, ba34};
		int o3 = 1;
		long[] expected = {
			0,
			o3 + 0, o3 + 1, o3 + 2, o3 + 16777215};
		for (int i = 0; i < expected.length; i++) {
			BigInteger exp = BigInteger.valueOf(expected[i]);
			BigInteger result = converter.convert(bas[i]);
			Assert.assertEquals(exp, result);
			Assert.assertEquals(bas[i], converter.reconvert(result));
		}
		try {
			converter.reconvert(BigInteger.valueOf(-1));
			fail();
		} catch (Exception e) {
		}
	}

	@Test
	public void test_with_minBlocks_equals_1() {
		ByteArrayToBigInteger converter = ByteArrayToBigInteger.getInstance(1, 1);
		ByteArray[] bas = {ba11, ba12, ba13, ba14, ba21, ba22, ba23, ba24, ba31, ba32, ba33, ba34, ba41, ba42, ba43, ba44};
		int o1 = 0;
		int o2 = 0 + 256;
		int o3 = 0 + 256 + 65536;
		int o4 = 0 + 256 + 65536 + 16777216;
		long[] expected = {
			o1 + 0, o1 + 1, o1 + 2, o1 + 255,
			o2 + 0, o2 + 1, o2 + 2, o2 + 65535,
			o3 + 0, o3 + 1, o3 + 2, o3 + 16777215,
			o4 + 0, o4 + 1, o4 + 2, o4 + 4294967295L};
		for (int i = 0; i < expected.length; i++) {
			BigInteger exp = BigInteger.valueOf(expected[i]);
			BigInteger result = converter.convert(bas[i]);
			Assert.assertEquals(exp, result);
			Assert.assertEquals(bas[i], converter.reconvert(result));
		}
		try {
			converter.reconvert(BigInteger.valueOf(-1));
			fail();
		} catch (Exception e) {
		}
	}

	@Test
	public void test_with_minBlocks_equals_2() {
		ByteArrayToBigInteger converter = ByteArrayToBigInteger.getInstance(1, 2);
		ByteArray[] bas = {ba21, ba22, ba23, ba24, ba31, ba32, ba33, ba34, ba41, ba42, ba43, ba44};
		int o2 = 0 + 0;
		int o3 = 0 + 0 + 65536;
		int o4 = 0 + 0 + 65536 + 16777216;
		long[] expected = {
			o2 + 0, o2 + 1, o2 + 2, o2 + 65535,
			o3 + 0, o3 + 1, o3 + 2, o3 + 16777215,
			o4 + 0, o4 + 1, o4 + 2, o4 + 4294967295L};
		for (int i = 0; i < expected.length; i++) {
			BigInteger exp = BigInteger.valueOf(expected[i]);
			BigInteger result = converter.convert(bas[i]);
			Assert.assertEquals(exp, result);
			Assert.assertEquals(bas[i], converter.reconvert(result));
		}
		try {
			converter.reconvert(BigInteger.valueOf(-1));
			fail();
		} catch (Exception e) {
		}
	}

	@Test
	public void test_with_minBlocks_equals_3() {
		ByteArrayToBigInteger converter = ByteArrayToBigInteger.getInstance(1, 3);
		ByteArray[] bas = {ba31, ba32, ba33, ba34, ba41, ba42, ba43, ba44};
		int o3 = 0 + 0 + 0;
		int o4 = 0 + 0 + 0 + 16777216;
		long[] expected = {
			o3 + 0, o3 + 1, o3 + 2, o3 + 16777215,
			o4 + 0, o4 + 1, o4 + 2, o4 + 4294967295L};
		for (int i = 0; i < expected.length; i++) {
			BigInteger exp = BigInteger.valueOf(expected[i]);
			BigInteger result = converter.convert(bas[i]);
			Assert.assertEquals(exp, result);
			Assert.assertEquals(bas[i], converter.reconvert(result));
		}
		try {
			converter.reconvert(BigInteger.valueOf(-1));
			fail();
		} catch (Exception e) {
		}
	}

	@Test
	public void test_with_blockLenght_equals_2_minBlocks_equals1() {
		ByteArrayToBigInteger converter = ByteArrayToBigInteger.getInstance(2, 1);
		ByteArray[] bas = {ba21, ba22, ba23, ba24, ba41, ba42, ba43, ba44};
		int o2 = 0;
		int o4 = 0 + 65536;
		long[] expected = {
			o2 + 0, o2 + 1, o2 + 2, o2 + 65535,
			o4 + 0, o4 + 1, o4 + 2, o4 + 4294967295L};
		for (int i = 0; i < expected.length; i++) {
			BigInteger exp = BigInteger.valueOf(expected[i]);
			BigInteger result = converter.convert(bas[i]);
			Assert.assertEquals(exp, result);
			Assert.assertEquals(bas[i], converter.reconvert(result));
		}
		try {
			converter.reconvert(BigInteger.valueOf(-1));
			fail();
		} catch (Exception e) {
		}
	}

	@Test
	public void test_with_blockLenght_equals_2_minBlocks_equals2() {
		ByteArrayToBigInteger converter = ByteArrayToBigInteger.getInstance(2, 2);
		ByteArray[] bas = {ba41, ba42, ba43, ba44};
		int o4 = 0 + 0;
		long[] expected = {
			o4 + 0, o4 + 1, o4 + 2, o4 + 4294967295L};
		for (int i = 0; i < expected.length; i++) {
			BigInteger exp = BigInteger.valueOf(expected[i]);
			BigInteger result = converter.convert(bas[i]);
			Assert.assertEquals(exp, result);
			Assert.assertEquals(bas[i], converter.reconvert(result));
		}
		try {
			converter.reconvert(BigInteger.valueOf(-1));
			fail();
		} catch (Exception e) {
		}
	}

	@Test
	public void test_with_blockLenght_equals_3_minBlocks_equals_1() {
		ByteArrayToBigInteger converter = ByteArrayToBigInteger.getInstance(3, 1);
		ByteArray[] bas = {ba31, ba32, ba33, ba34};
		int o3 = 0;
		long[] expected = {
			o3 + 0, o3 + 1, o3 + 2, o3 + 16777215};
		for (int i = 0; i < expected.length; i++) {
			BigInteger exp = BigInteger.valueOf(expected[i]);
			BigInteger result = converter.convert(bas[i]);
			Assert.assertEquals(exp, result);
			Assert.assertEquals(bas[i], converter.reconvert(result));
		}
		try {
			converter.reconvert(BigInteger.valueOf(-1));
			fail();
		} catch (Exception e) {
		}
	}

	@Test
	public void test_inverted() {
		for (int i = 1; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				ByteArrayToBigInteger converter = ByteArrayToBigInteger.getInstance(i, j);
				for (int k = 0; k < 100000; k++) {
					BigInteger value = BigInteger.valueOf(k);
					Assert.assertEquals(value, converter.convert(converter.reconvert(value)));
				}
				try {
					converter.reconvert(BigInteger.valueOf(-1));
					fail();
				} catch (Exception e) {
				}
			}
		}
	}

}
