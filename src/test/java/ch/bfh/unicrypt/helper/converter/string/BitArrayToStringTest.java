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

import ch.bfh.unicrypt.helper.array.classes.BitArray;
import ch.bfh.unicrypt.helper.converter.classes.string.BitArrayToString;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author R. Haenni
 */
public class BitArrayToStringTest {

	@Test
	public void testBitArrayToStringTest1() {
		String[] strings = {"", "0", "1", "00", "01", "10", "11", "00000000", "11111111", "000000001", "10101001011101010100101010101010101"};

		BitArrayToString c1 = BitArrayToString.getInstance();
		BitArrayToString c2 = BitArrayToString.getInstance(true);

		for (String s : strings) {
			BitArray ba = BitArray.getInstance(s);
			Assert.assertEquals(s, c1.convert(ba));
			Assert.assertEquals(s, c2.convert(ba.reverse()));
			Assert.assertEquals(ba, c1.reconvert(c1.convert(ba)));
			Assert.assertEquals(ba, c2.reconvert(c2.convert(ba)));
		}
	}

	@Test
	public void testBitArrayToStringTest2() {
		BitArrayToString c = BitArrayToString.getInstance();

		Assert.assertTrue(c.isValidOutput(""));
		Assert.assertTrue(c.isValidOutput("0"));
		Assert.assertTrue(c.isValidOutput("1"));
		Assert.assertTrue(c.isValidOutput("010101001010101010101010101010101010010000101010101010"));
		Assert.assertFalse(c.isValidOutput(" "));
		Assert.assertFalse(c.isValidOutput("0 "));
		Assert.assertFalse(c.isValidOutput(" 1"));
		Assert.assertFalse(c.isValidOutput("010101001010101010101 010101010101010010000101010101010"));
	}

}
