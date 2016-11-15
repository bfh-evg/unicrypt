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
package ch.bfh.unicrypt.helper.converter.string;

import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.converter.classes.string.ByteArrayToString;
import org.junit.Assert;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author R. Haenni <rolf.haenni@bfh.ch>
 */
public class ByteArrayToStringTest {

	@Test
	public void testMain() {

		ByteArray b1 = ByteArray.getInstance("");
		ByteArray b2 = ByteArray.getInstance("00");
		ByteArray b3 = ByteArray.getInstance("F8");
		ByteArray b4 = ByteArray.getInstance("F8|F8");
		ByteArray b5 = ByteArray.getInstance("01|23|45|67|89|AB|CD|EF");

		ByteArray[] bs = {b1, b2, b3, b4, b5};

		ByteArrayToString c1 = ByteArrayToString.getInstance(ByteArrayToString.Radix.BASE64);
		ByteArrayToString c2 = ByteArrayToString.getInstance(ByteArrayToString.Radix.BINARY);
		ByteArrayToString c3 = ByteArrayToString.getInstance(ByteArrayToString.Radix.BINARY, "|");
		ByteArrayToString c4 = ByteArrayToString.getInstance(ByteArrayToString.Radix.BINARY, " ");
		ByteArrayToString c5 = ByteArrayToString.getInstance(ByteArrayToString.Radix.BINARY, "-");
		ByteArrayToString c6 = ByteArrayToString.getInstance(ByteArrayToString.Radix.BINARY, "x");
		ByteArrayToString c7 = ByteArrayToString.getInstance(ByteArrayToString.Radix.BINARY, "X");
		ByteArrayToString c8 = ByteArrayToString.getInstance(ByteArrayToString.Radix.BINARY, "2");
		ByteArrayToString c9 = ByteArrayToString.getInstance(ByteArrayToString.Radix.HEX);
		ByteArrayToString c10 = ByteArrayToString.getInstance(ByteArrayToString.Radix.HEX, true);
		ByteArrayToString c11 = ByteArrayToString.getInstance(ByteArrayToString.Radix.HEX, "|", true);
		ByteArrayToString c12 = ByteArrayToString.getInstance(ByteArrayToString.Radix.HEX, " ", true);
		ByteArrayToString c13 = ByteArrayToString.getInstance(ByteArrayToString.Radix.HEX, "-", true);
		ByteArrayToString c14 = ByteArrayToString.getInstance(ByteArrayToString.Radix.HEX, "x", true);
		ByteArrayToString c15 = ByteArrayToString.getInstance(ByteArrayToString.Radix.HEX, "X", true);
		ByteArrayToString c16 = ByteArrayToString.getInstance(ByteArrayToString.Radix.HEX, "a", true);
		ByteArrayToString c17 = ByteArrayToString.getInstance(ByteArrayToString.Radix.HEX, false);
		ByteArrayToString c18 = ByteArrayToString.getInstance(ByteArrayToString.Radix.HEX, "|", false);
		ByteArrayToString c19 = ByteArrayToString.getInstance(ByteArrayToString.Radix.HEX, " ", false);
		ByteArrayToString c20 = ByteArrayToString.getInstance(ByteArrayToString.Radix.HEX, "-", false);
		ByteArrayToString c21 = ByteArrayToString.getInstance(ByteArrayToString.Radix.HEX, "x", false);
		ByteArrayToString c22 = ByteArrayToString.getInstance(ByteArrayToString.Radix.HEX, "X", false);
		ByteArrayToString c23 = ByteArrayToString.getInstance(ByteArrayToString.Radix.HEX, "A", false);
		ByteArrayToString[] cs = {c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15, c16, c17, c18, c19, c20, c21, c22, c23};

		for (ByteArrayToString c : cs) {
			for (ByteArray b : bs) {
				Assert.assertEquals(b, c.reconvert(c.convert(b)));
			}
			Assert.assertEquals("", c.convert(b1));
		}

		for (ByteArrayToString c : new ByteArrayToString[]{c2, c3, c4, c5, c6, c7, c8}) {
			Assert.assertEquals("11111000", c.convert(b3));
		}
		for (ByteArrayToString c : new ByteArrayToString[]{c9, c10, c11, c12, c13, c14, c15, c16}) {
			Assert.assertEquals("F8", c.convert(b3));
		}
		for (ByteArrayToString c : new ByteArrayToString[]{c17, c18, c19, c20, c21, c22, c23}) {
			Assert.assertEquals("f8", c.convert(b3));
		}
		Assert.assertEquals("F8|F8", c11.convert(b4));
		Assert.assertEquals("F8 F8", c12.convert(b4));
		Assert.assertEquals("F8-F8", c13.convert(b4));
		Assert.assertEquals("F8xF8", c14.convert(b4));
		Assert.assertEquals("F8XF8", c15.convert(b4));
		Assert.assertEquals("F8aF8", c16.convert(b4));
		Assert.assertEquals("f8|f8", c18.convert(b4));
		Assert.assertEquals("f8 f8", c19.convert(b4));
		Assert.assertEquals("f8-f8", c20.convert(b4));
		Assert.assertEquals("f8xf8", c21.convert(b4));
		Assert.assertEquals("f8Xf8", c22.convert(b4));
		Assert.assertEquals("f8Af8", c23.convert(b4));
	}

	@Test
	public void testGetInstance() {

		try {
			ByteArrayToString.getInstance("A");
			fail();
		} catch (Exception e) {
		}
		try {
			ByteArrayToString.getInstance(ByteArrayToString.Radix.HEX, "A");
			fail();
		} catch (Exception e) {
		}
		try {
			ByteArrayToString.getInstance(ByteArrayToString.Radix.BINARY, "0");
			fail();
		} catch (Exception e) {
		}
		try {
			ByteArrayToString.getInstance(ByteArrayToString.Radix.BASE64, "|");
			fail();
		} catch (Exception e) {
		}
		try {
			ByteArrayToString.getInstance(ByteArrayToString.Radix.BASE64, "");
		} catch (Exception e) {
			fail();
		}
	}

}
