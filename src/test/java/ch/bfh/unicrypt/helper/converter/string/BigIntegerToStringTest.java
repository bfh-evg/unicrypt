/*
 * UniCrypt
 *
 *  UniCrypt(tm): Cryptographic framework allowing the implementation of cryptographic protocols, e.g. e-voting
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
package ch.bfh.unicrypt.helper.converter.string;

import ch.bfh.unicrypt.helper.converter.classes.string.BigIntegerToString;
import java.math.BigInteger;
import org.junit.Assert;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author R. Haenni
 */
public class BigIntegerToStringTest {

	@Test
	public void bigIntegerToStringTest1() {

		for (int radix = 2; radix <= 36; radix++) {
			BigIntegerToString c1 = BigIntegerToString.getInstance(radix, true);
			BigIntegerToString c2 = BigIntegerToString.getInstance(radix, false);
			for (int i = -100; i <= 100; i++) {
				BigInteger b = BigInteger.valueOf(i);
				Assert.assertEquals(b, c1.reconvert(c1.convert(b)));
				Assert.assertEquals(b, c2.reconvert(c2.convert(b)));
			}
		}
	}

	@Test
	public void bigIntegerToStringTest2() {

		BigIntegerToString c1 = BigIntegerToString.getInstance(16, true);
		BigIntegerToString c2 = BigIntegerToString.getInstance(16, false);
		BigInteger b1 = BigInteger.valueOf(65535);
		BigInteger b2 = BigInteger.valueOf(-65535);
		Assert.assertEquals("FFFF", c1.convert(b1));
		Assert.assertEquals("-FFFF", c1.convert(b2));
		Assert.assertEquals("ffff", c2.convert(b1));
		Assert.assertEquals("-ffff", c2.convert(b2));

		Assert.assertTrue(c1.isValidOutput("0"));
		Assert.assertTrue(c1.isValidOutput("F"));
		Assert.assertTrue(c1.isValidOutput("F"));
		Assert.assertFalse(c1.isValidOutput("f"));
		Assert.assertFalse(c1.isValidOutput("G"));
		Assert.assertFalse(c1.isValidOutput(" "));
		Assert.assertFalse(c1.isValidOutput(""));
		Assert.assertFalse(c1.isValidOutput("-"));

		try {
			BigIntegerToString.getInstance(37, true);
			fail();
		} catch (Exception e) {
		}
		try {
			BigIntegerToString.getInstance(1, true);
			fail();
		} catch (Exception e) {
		}

	}

}
