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
package ch.bfh.unicrypt.helper.converter;

import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.converter.BigIntegerConverter;
import java.math.BigInteger;
import java.nio.ByteOrder;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class BigIntegerConverterTest {

	public static BigIntegerConverter c1 = BigIntegerConverter.getInstance(ByteOrder.BIG_ENDIAN, 0);
	public static BigIntegerConverter c2 = BigIntegerConverter.getInstance(ByteOrder.LITTLE_ENDIAN, 0);
	public static BigIntegerConverter c3 = BigIntegerConverter.getInstance(ByteOrder.BIG_ENDIAN, 10);
	public static BigIntegerConverter c4 = BigIntegerConverter.getInstance(ByteOrder.LITTLE_ENDIAN, 10);

	@Test
	public void testByteArrayConverter() {
		BigInteger b0 = BigInteger.valueOf(0);
		BigInteger b1 = BigInteger.valueOf(9);
		BigInteger b2 = BigInteger.valueOf(200);
		BigInteger b3 = BigInteger.valueOf(300);
		for (BigIntegerConverter converter : new BigIntegerConverter[]{c1, c2, c3, c4}) {
			for (BigInteger bigInteger : new BigInteger[]{b0, b1, b2, b3}) {
				ByteArray ba = converter.convertToByteArray(bigInteger);
				Assert.assertTrue(ba.getLength() >= converter.getMinLength());
				Assert.assertEquals(bigInteger, converter.convertFromByteArray(ba));
			}

		}
	}

}
