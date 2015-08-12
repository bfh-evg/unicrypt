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

import ch.bfh.unicrypt.helper.array.classes.BitArray;
import ch.bfh.unicrypt.helper.converter.classes.biginteger.BitArrayToBigInteger;
import ch.bfh.unicrypt.helper.math.MathUtil;
import java.math.BigInteger;
import org.junit.Assert;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author R. Haenni
 */
public class BitArrayToBigIntegerTest {

	BitArray ba0 = BitArray.getInstance("");
	BitArray ba10 = BitArray.getInstance("0");
	BitArray ba11 = BitArray.getInstance("1");
	BitArray ba20 = BitArray.getInstance("00");
	BitArray ba21 = BitArray.getInstance("01");
	BitArray ba22 = BitArray.getInstance("10");
	BitArray ba23 = BitArray.getInstance("11");
	BitArray ba30 = BitArray.getInstance("000");
	BitArray ba31 = BitArray.getInstance("001");
	BitArray ba32 = BitArray.getInstance("010");
	BitArray ba33 = BitArray.getInstance("011");
	BitArray ba34 = BitArray.getInstance("100");
	BitArray ba35 = BitArray.getInstance("101");
	BitArray ba36 = BitArray.getInstance("110");
	BitArray ba37 = BitArray.getInstance("111");
	BitArray ba40 = BitArray.getInstance("0000");

	BitArrayToBigInteger converter = BitArrayToBigInteger.getInstance();

	@Test
	public void testGetInstance() {

		BigInteger expected = MathUtil.ZERO;
		for (BitArray ba : new BitArray[]{ba0, ba10, ba11, ba20, ba21, ba22, ba23, ba30, ba31, ba32, ba33, ba34, ba35, ba36, ba37, ba40}) {
			Assert.assertEquals(expected, converter.convert(ba));
			expected = expected.add(MathUtil.ONE);
		}
		Assert.assertEquals(BigInteger.valueOf(510), converter.convert(BitArray.getInstance("11111111")));
		Assert.assertEquals(BigInteger.valueOf(511), converter.convert(BitArray.getInstance("000000000")));
		Assert.assertEquals(BigInteger.valueOf(512), converter.convert(BitArray.getInstance("000000001")));
		Assert.assertEquals(BigInteger.valueOf(513), converter.convert(BitArray.getInstance("000000010")));
		Assert.assertEquals(BigInteger.valueOf(766), converter.convert(BitArray.getInstance("011111111")));
		Assert.assertEquals(BigInteger.valueOf(767), converter.convert(BitArray.getInstance("100000000")));
		Assert.assertEquals(BigInteger.valueOf(768), converter.convert(BitArray.getInstance("100000001")));
		Assert.assertEquals(BigInteger.valueOf(1022), converter.convert(BitArray.getInstance("111111111")));
		Assert.assertEquals(BigInteger.valueOf(1023), converter.convert(BitArray.getInstance("0000000000")));
		Assert.assertEquals(BigInteger.valueOf(1024), converter.convert(BitArray.getInstance("0000000001")));
	}

	@Test
	public void test_inverted() {
		for (int k = 0; k < 1000000; k++) {
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
