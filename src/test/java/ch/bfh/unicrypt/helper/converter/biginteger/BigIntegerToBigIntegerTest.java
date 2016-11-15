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

import ch.bfh.unicrypt.helper.converter.classes.biginteger.BigIntegerToBigInteger;
import java.math.BigInteger;
import org.junit.Assert;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author R. Haenni
 */
public class BigIntegerToBigIntegerTest {

	@Test
	public void test_unrestricted() {

		BigIntegerToBigInteger converter = BigIntegerToBigInteger.getInstance();

		Assert.assertEquals(BigInteger.valueOf(0), converter.convert(BigInteger.valueOf(0)));
		Assert.assertEquals(BigInteger.valueOf(2), converter.convert(BigInteger.valueOf(1)));
		Assert.assertEquals(BigInteger.valueOf(4), converter.convert(BigInteger.valueOf(2)));
		Assert.assertEquals(BigInteger.valueOf(6), converter.convert(BigInteger.valueOf(3)));
		Assert.assertEquals(BigInteger.valueOf(1), converter.convert(BigInteger.valueOf(-1)));
		Assert.assertEquals(BigInteger.valueOf(3), converter.convert(BigInteger.valueOf(-2)));
		Assert.assertEquals(BigInteger.valueOf(5), converter.convert(BigInteger.valueOf(-3)));

		for (int i = -10; i <= 10; i++) {
			BigInteger k = BigInteger.valueOf(i);
			Assert.assertEquals(k, converter.reconvert(converter.convert(k)));

		}
	}

	@Test
	public void test_with_minValue_equals_5() {

		BigIntegerToBigInteger converter = BigIntegerToBigInteger.getInstance(5);

		Assert.assertEquals(BigInteger.valueOf(0), converter.convert(BigInteger.valueOf(5)));
		Assert.assertEquals(BigInteger.valueOf(1), converter.convert(BigInteger.valueOf(6)));
		Assert.assertEquals(BigInteger.valueOf(2), converter.convert(BigInteger.valueOf(7)));
		Assert.assertEquals(BigInteger.valueOf(3), converter.convert(BigInteger.valueOf(8)));
		Assert.assertEquals(BigInteger.valueOf(4), converter.convert(BigInteger.valueOf(9)));
		Assert.assertEquals(BigInteger.valueOf(5), converter.convert(BigInteger.valueOf(10)));
		Assert.assertEquals(BigInteger.valueOf(6), converter.convert(BigInteger.valueOf(11)));

		for (int i = 5; i <= 10; i++) {
			BigInteger k = BigInteger.valueOf(i);
			Assert.assertEquals(k, converter.reconvert(converter.convert(k)));

		}

		try {
			converter.convert(BigInteger.valueOf(4));
			fail();
		} catch (Exception e) {
		}
		try {
			converter.reconvert(BigInteger.valueOf(-1));
			fail();
		} catch (Exception e) {
		}
	}

	@Test
	public void test_with_minValue_equals__minus_5() {

		BigIntegerToBigInteger converter = BigIntegerToBigInteger.getInstance(-5);

		Assert.assertEquals(BigInteger.valueOf(0), converter.convert(BigInteger.valueOf(-5)));
		Assert.assertEquals(BigInteger.valueOf(1), converter.convert(BigInteger.valueOf(-4)));
		Assert.assertEquals(BigInteger.valueOf(2), converter.convert(BigInteger.valueOf(-3)));
		Assert.assertEquals(BigInteger.valueOf(3), converter.convert(BigInteger.valueOf(-2)));
		Assert.assertEquals(BigInteger.valueOf(4), converter.convert(BigInteger.valueOf(-1)));
		Assert.assertEquals(BigInteger.valueOf(5), converter.convert(BigInteger.valueOf(0)));
		Assert.assertEquals(BigInteger.valueOf(6), converter.convert(BigInteger.valueOf(1)));

		for (int i = -5; i <= 10; i++) {
			BigInteger k = BigInteger.valueOf(i);
			Assert.assertEquals(k, converter.reconvert(converter.convert(k)));

		}
	}

}
