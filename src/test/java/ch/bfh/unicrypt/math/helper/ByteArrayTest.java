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
package ch.bfh.unicrypt.math.helper;

import ch.bfh.unicrypt.crypto.random.interfaces.RandomGenerator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Reto E. Koenig <reto.koenig@bfh.ch>
 */
public class ByteArrayTest {

	public ByteArrayTest() {
	}

	@BeforeClass
	public static void setUpClass() {
	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	/**
	 * Test of getAts method, of class ByteArray.
	 */
	@Test
	@Ignore
	public void testGetBytes() {
//		System.out.println("getBytes");
		ByteArray instance = null;
		byte[] expResult = null;
		byte[] result = instance.getAll();
		assertArrayEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getLength method, of class ByteArray.
	 */
	@Test
	@Ignore
	public void testGetLength() {
//		System.out.println("getLength");
		ByteArray instance = null;
		int expResult = 0;
		int result = instance.getLength();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getAt method, of class ByteArray.
	 */
	@Test
	@Ignore
	public void testGetByte() {
//		System.out.println("getByte");
		int index = 0;
		ByteArray instance = null;
		byte expResult = 0;
		byte result = instance.getAt(index);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of concatenate method, of class ByteArray.
	 */
	@Test
	public void testConcatenate() {
//		System.out.println("concatenate");
		ByteArray instance = ByteArray.getInstance(new byte[]{1, 2, 3, 4, 5, 6});
		ByteArray other = ByteArray.getInstance(new byte[]{7, 8, 9, 10});
		ByteArray expResult = ByteArray.getInstance(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});;
		ByteArray result = instance.concatenate(other);
		assertEquals(expResult, result);
	}

	/**
	 * Test of xor method, of class ByteArray.
	 */
	@Test
	public void testXor() {
		ByteArray one = ByteArray.getInstance(new byte[]{1, 1, 1});
		ByteArray two = ByteArray.getInstance(new byte[]{0, 0, 0});
		ByteArray xored = one.xor(two);
		Assert.assertEquals(one, xored);

		ByteArray three = ByteArray.getInstance(new byte[]{1, 1, 1});
		xored = one.xor(three);
		Assert.assertEquals(two, xored);

		ByteArray four = ByteArray.getInstance(new byte[]{1, 1, 1});
		xored = one.xor(four);
		Assert.assertEquals(two, xored);

		ByteArray five = ByteArray.getInstance(new byte[]{1, 1, 1});
		xored = one.xor(two, five);
		Assert.assertEquals(two, xored);

		xored = one.xor(one, two, two);
		Assert.assertEquals(two, xored);

		xored = xored.xor(one);
		Assert.assertEquals(one, xored);
	}

	/**
	 * Test of standardToStringContent method, of class ByteArray.
	 */
	@Test
	@Ignore
	public void testStandardToStringContent() {
//		System.out.println("standardToStringContent");
		ByteArray instance = null;
		String expResult = "";
		String result = instance.standardToStringContent();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of hashCode method, of class ByteArray.
	 */
	@Test
	@Ignore
	public void testHashCode() {
//		System.out.println("hashCode");
		ByteArray instance = null;
		int expResult = 0;
		int result = instance.hashCode();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of equals method, of class ByteArray.
	 */
	@Test
	@Ignore
	public void testEquals() {
//		System.out.println("equals");
		Object obj = null;
		ByteArray instance = null;
		boolean expResult = false;
		boolean result = instance.equals(obj);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getInstance method, of class ByteArray.
	 */
	@Test
	@Ignore
	public void testGetInstance_0args() {
//		System.out.println("getInstance");
		ByteArray expResult = null;
		ByteArray result = ByteArray.getInstance();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getInstance method, of class ByteArray.
	 */
	@Test
	@Ignore
	public void testGetInstance_int() {
//		System.out.println("getInstance");
		int length = 0;
		ByteArray expResult = null;
		ByteArray result = ByteArray.getInstance(length);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getInstance method, of class ByteArray.
	 */
	@Test
	@Ignore
	public void testGetInstance_byteArr() {
//		System.out.println("getInstance");
		byte[] bytes = null;
		ByteArray expResult = null;
		ByteArray result = ByteArray.getInstance(bytes);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getRandomInstance method, of class ByteArray.
	 */
	@Test
	@Ignore
	public void testGetRandomInstance_int() {
//		System.out.println("getRandomInstance");
		int length = 0;
		ByteArray expResult = null;
		ByteArray result = ByteArray.getRandomInstance(length);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getRandomInstance method, of class ByteArray.
	 */
	@Test
	@Ignore
	public void testGetRandomInstance_int_RandomGenerator() {
//		System.out.println("getRandomInstance");
		int length = 0;
		RandomGenerator randomGenerator = null;
		ByteArray expResult = null;
		ByteArray result = ByteArray.getRandomInstance(length, randomGenerator);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

}
