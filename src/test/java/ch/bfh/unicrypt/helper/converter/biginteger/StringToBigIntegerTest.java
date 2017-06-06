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

import ch.bfh.unicrypt.helper.converter.classes.biginteger.StringToBigInteger;
import ch.bfh.unicrypt.helper.math.Alphabet;
import java.math.BigInteger;
import org.junit.Assert;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author R. Haenni
 */
public class StringToBigIntegerTest {

	Alphabet alphabet1 = Alphabet.getInstance("0");
	String s10 = "";
	String s11 = "0";
	String s12 = "00";
	String s13 = "000";
	String s14 = "0000";
	String s15 = "00000";
	String s16 = "000000";
	String s17 = "0000000";
	String s18 = "00000000";

	Alphabet alphabet2 = Alphabet.getInstance("01");
	String s20 = "";
	String s210 = "0";
	String s211 = "1";
	String s220 = "00";
	String s221 = "01";
	String s222 = "10";
	String s223 = "11";
	String s230 = "000";
	String s231 = "001";
	String s232 = "010";
	String s233 = "011";
	String s234 = "100";
	String s235 = "101";
	String s236 = "110";
	String s237 = "111";
	String s240 = "0000";

	Alphabet alphabet3 = Alphabet.getInstance("012");

	String s30 = "";
	String s310 = "0";
	String s311 = "1";
	String s312 = "2";
	String s320 = "00";
	String s321 = "01";
	String s322 = "02";
	String s323 = "10";
	String s324 = "11";
	String s325 = "12";
	String s326 = "20";
	String s327 = "21";
	String s328 = "22";
	String s330 = "000";

	@Test
	public void testGetInstance_without_blockLengh_without_minBlocks() {

		{
			String[] strings = {s10, s11, s12, s13, s14, s15, s16, s17, s18};
			StringToBigInteger converter = StringToBigInteger.getInstance(alphabet1);
			int i = 0;
			for (String s : strings) {
				BigInteger value = BigInteger.valueOf(i);
				Assert.assertEquals(value, converter.convert(s));
				i++;
			}
			try {
				converter.reconvert(BigInteger.valueOf(-1));
				fail();
			} catch (Exception e) {
			}
		}
		{
			String[] strings = {s20, s210, s211, s220, s221, s222, s223, s230, s231, s232, s233, s234, s235, s236, s237, s240};
			StringToBigInteger converter = StringToBigInteger.getInstance(alphabet2);
			int i = 0;
			for (String s : strings) {
				BigInteger value = BigInteger.valueOf(i);
				Assert.assertEquals(value, converter.convert(s));
				i++;
			}
			try {
				converter.reconvert(BigInteger.valueOf(-1));
				fail();
			} catch (Exception e) {
			}
		}
		{
			String[] strings = {s30, s310, s311, s312, s320, s321, s322, s323, s324, s325, s326, s327, s328, s330};
			StringToBigInteger converter = StringToBigInteger.getInstance(alphabet3);
			int i = 0;
			for (String s : strings) {
				BigInteger value = BigInteger.valueOf(i);
				Assert.assertEquals(value, converter.convert(s));
				i++;
			}
		}
	}

	@Test
	public void testGetInstance_with_blocklength_equals_2() {

		{
			String[] strings = {s10, s12, s14, s16, s18};
			StringToBigInteger converter = StringToBigInteger.getInstance(alphabet1, 2);
			int i = 0;
			for (String s : strings) {
				BigInteger value = BigInteger.valueOf(i);
				Assert.assertEquals(value, converter.convert(s));
				i++;
			}
			try {
				converter.reconvert(BigInteger.valueOf(-1));
				fail();
			} catch (Exception e) {
			}
		}
		{
			String[] strings = {s20, s220, s221, s222, s223, s240};
			StringToBigInteger converter = StringToBigInteger.getInstance(alphabet2, 2);
			int i = 0;
			for (String s : strings) {
				BigInteger value = BigInteger.valueOf(i);
				Assert.assertEquals(value, converter.convert(s));
				i++;
			}
			try {
				converter.reconvert(BigInteger.valueOf(-1));
				fail();
			} catch (Exception e) {
			}
		}
		{
			String[] strings = {s30, s320, s321, s322, s323, s324, s325, s326, s327, s328};
			StringToBigInteger converter = StringToBigInteger.getInstance(alphabet3, 2);
			int i = 0;
			for (String s : strings) {
				BigInteger value = BigInteger.valueOf(i);
				Assert.assertEquals(value, converter.convert(s));
				i++;
			}
			try {
				converter.reconvert(BigInteger.valueOf(-1));
				fail();
			} catch (Exception e) {
			}
		}
	}

	@Test
	public void testGetInstance_with_blocklength_equals_3() {

		{
			String[] strings = {s10, s13, s16};
			StringToBigInteger converter = StringToBigInteger.getInstance(alphabet1, 3);
			int i = 0;
			for (String s : strings) {
				BigInteger value = BigInteger.valueOf(i);
				Assert.assertEquals(value, converter.convert(s));
				i++;
			}
			try {
				converter.reconvert(BigInteger.valueOf(-1));
				fail();
			} catch (Exception e) {
			}
		}
		{
			String[] strings = {s20, s230, s231, s232, s233, s234, s235, s236, s237};
			StringToBigInteger converter = StringToBigInteger.getInstance(alphabet2, 3);
			int i = 0;
			for (String s : strings) {
				BigInteger value = BigInteger.valueOf(i);
				Assert.assertEquals(value, converter.convert(s));
				i++;
			}
			try {
				converter.reconvert(BigInteger.valueOf(-1));
				fail();
			} catch (Exception e) {
			}
		}
		{
			String[] strings = {s30, s330};
			StringToBigInteger converter = StringToBigInteger.getInstance(alphabet3, 3);
			int i = 0;
			for (String s : strings) {
				BigInteger value = BigInteger.valueOf(i);
				Assert.assertEquals(value, converter.convert(s));
				i++;
			}
			try {
				converter.reconvert(BigInteger.valueOf(-1));
				fail();
			} catch (Exception e) {
			}
		}
	}

	@Test
	public void testGetInstance_with_minBlocks_equals_1() {

		{
			String[] strings = {s11, s12, s13, s14, s15, s16, s17, s18};
			StringToBigInteger converter = StringToBigInteger.getInstance(alphabet1, 1, 1);
			int i = 0;
			for (String s : strings) {
				BigInteger value = BigInteger.valueOf(i);
				Assert.assertEquals(value, converter.convert(s));
				i++;
			}
			try {
				converter.reconvert(BigInteger.valueOf(-1));
				fail();
			} catch (Exception e) {
			}
		}
		{
			String[] strings = {s210, s211, s220, s221, s222, s223, s230, s231, s232, s233, s234, s235, s236, s237, s240};
			StringToBigInteger converter = StringToBigInteger.getInstance(alphabet2, 1, 1);
			int i = 0;
			for (String s : strings) {
				BigInteger value = BigInteger.valueOf(i);
				Assert.assertEquals(value, converter.convert(s));
				i++;
			}
			try {
				converter.reconvert(BigInteger.valueOf(-1));
				fail();
			} catch (Exception e) {
			}
		}
		{
			String[] strings = {s310, s311, s312, s320, s321, s322, s323, s324, s325, s326, s327, s328, s330};
			StringToBigInteger converter = StringToBigInteger.getInstance(alphabet3, 1, 1);
			int i = 0;
			for (String s : strings) {
				BigInteger value = BigInteger.valueOf(i);
				Assert.assertEquals(value, converter.convert(s));
				i++;
			}
			try {
				converter.reconvert(BigInteger.valueOf(-1));
				fail();
			} catch (Exception e) {
			}
		}
	}

	@Test
	public void testGetInstance_with_minBlocks_equals_2() {

		{
			String[] strings = {s12, s13, s14, s15, s16, s17, s18};
			StringToBigInteger converter = StringToBigInteger.getInstance(alphabet1, 1, 2);
			int i = 0;
			for (String s : strings) {
				BigInteger value = BigInteger.valueOf(i);
				Assert.assertEquals(value, converter.convert(s));
				i++;
			}
			try {
				converter.reconvert(BigInteger.valueOf(-1));
				fail();
			} catch (Exception e) {
			}
		}
		{
			String[] strings = {s220, s221, s222, s223, s230, s231, s232, s233, s234, s235, s236, s237, s240};
			StringToBigInteger converter = StringToBigInteger.getInstance(alphabet2, 1, 2);
			int i = 0;
			for (String s : strings) {
				BigInteger value = BigInteger.valueOf(i);
				Assert.assertEquals(value, converter.convert(s));
				i++;
			}
			try {
				converter.reconvert(BigInteger.valueOf(-1));
				fail();
			} catch (Exception e) {
			}
		}
		{
			String[] strings = {s320, s321, s322, s323, s324, s325, s326, s327, s328, s330};
			StringToBigInteger converter = StringToBigInteger.getInstance(alphabet3, 1, 2);
			int i = 0;
			for (String s : strings) {
				BigInteger value = BigInteger.valueOf(i);
				Assert.assertEquals(value, converter.convert(s));
				i++;
			}
			try {
				converter.reconvert(BigInteger.valueOf(-1));
				fail();
			} catch (Exception e) {
			}
		}
	}

	@Test
	public void testGetInstance_with_minBlocks_equals_3() {

		{
			String[] strings = {s13, s14, s15, s16, s17, s18};
			StringToBigInteger converter = StringToBigInteger.getInstance(alphabet1, 1, 3);
			int i = 0;
			for (String s : strings) {
				BigInteger value = BigInteger.valueOf(i);
				Assert.assertEquals(value, converter.convert(s));
				i++;
			}
			try {
				converter.reconvert(BigInteger.valueOf(-1));
				fail();
			} catch (Exception e) {
			}
		}
		{
			String[] strings = {s230, s231, s232, s233, s234, s235, s236, s237, s240};
			StringToBigInteger converter = StringToBigInteger.getInstance(alphabet2, 1, 3);
			int i = 0;
			for (String s : strings) {
				BigInteger value = BigInteger.valueOf(i);
				Assert.assertEquals(value, converter.convert(s));
				i++;
			}
			try {
				converter.reconvert(BigInteger.valueOf(-1));
				fail();
			} catch (Exception e) {
			}
		}
		{
			String[] strings = {s330};
			StringToBigInteger converter = StringToBigInteger.getInstance(alphabet3, 1, 3);
			int i = 0;
			for (String s : strings) {
				BigInteger value = BigInteger.valueOf(i);
				Assert.assertEquals(value, converter.convert(s));
				i++;
			}
			try {
				converter.reconvert(BigInteger.valueOf(-1));
				fail();
			} catch (Exception e) {
			}
		}
	}

	@Test
	public void testGetInstance_with_blocklength_equals_2_and_minBlocks_equals_1() {

		{
			String[] strings = {s12, s14, s16, s18};
			StringToBigInteger converter = StringToBigInteger.getInstance(alphabet1, 2, 1);
			int i = 0;
			for (String s : strings) {
				BigInteger value = BigInteger.valueOf(i);
				Assert.assertEquals(value, converter.convert(s));
				i++;
			}
			try {
				converter.reconvert(BigInteger.valueOf(-1));
				fail();
			} catch (Exception e) {
			}
		}
		{
			String[] strings = {s220, s221, s222, s223, s240};
			StringToBigInteger converter = StringToBigInteger.getInstance(alphabet2, 2, 1);
			int i = 0;
			for (String s : strings) {
				BigInteger value = BigInteger.valueOf(i);
				Assert.assertEquals(value, converter.convert(s));
				i++;
			}
			try {
				converter.reconvert(BigInteger.valueOf(-1));
				fail();
			} catch (Exception e) {
			}
		}
		{
			String[] strings = {s320, s321, s322, s323, s324, s325, s326, s327, s328};
			StringToBigInteger converter = StringToBigInteger.getInstance(alphabet3, 2, 1);
			int i = 0;
			for (String s : strings) {
				BigInteger value = BigInteger.valueOf(i);
				Assert.assertEquals(value, converter.convert(s));
				i++;
			}
			try {
				converter.reconvert(BigInteger.valueOf(-1));
				fail();
			} catch (Exception e) {
			}
		}
	}

	@Test
	public void testGetInstance_with_blocklength_equals_2_and_minBlocks_equals_2() {

		{
			String[] strings = {s14, s16, s18};
			StringToBigInteger converter = StringToBigInteger.getInstance(alphabet1, 2, 2);
			int i = 0;
			for (String s : strings) {
				BigInteger value = BigInteger.valueOf(i);
				Assert.assertEquals(value, converter.convert(s));
				i++;
			}
			try {
				converter.reconvert(BigInteger.valueOf(-1));
				fail();
			} catch (Exception e) {
			}
		}
		{
			String[] strings = {s240};
			StringToBigInteger converter = StringToBigInteger.getInstance(alphabet2, 2, 2);
			int i = 0;
			for (String s : strings) {
				BigInteger value = BigInteger.valueOf(i);
				Assert.assertEquals(value, converter.convert(s));
				i++;
			}
			try {
				converter.reconvert(BigInteger.valueOf(-1));
				fail();
			} catch (Exception e) {
			}
		}
	}

	@Test
	public void test_inverted() {
		for (Alphabet alphabet : new Alphabet[]{alphabet1, alphabet2, alphabet3}) {
			for (int i = 1; i < 5; i++) {
				for (int j = 0; j < 5; j++) {
					StringToBigInteger converter = StringToBigInteger.getInstance(alphabet, i, j);
					for (int k = 0; k < 300; k++) {
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

}
