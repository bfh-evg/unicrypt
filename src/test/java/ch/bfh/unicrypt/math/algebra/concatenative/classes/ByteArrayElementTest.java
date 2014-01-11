/*
 * UniCrypt
 *
 *  UniCrypt(tm) : Cryptographical framework allowing the implementation of cryptographic protocols e.g. e-voting
 *  Copyright (C) 2014 ???
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
 *   a written agreement between you and ???.
 *
 *
 *   For further information contact ???
 *
 *
 * Redistributions of files must retain the above copyright notice.
 */
package ch.bfh.unicrypt.math.algebra.concatenative.classes;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import java.math.BigInteger;
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
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
public class ByteArrayElementTest {

	public ByteArrayElementTest() {
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
	 * Test of getByteArray method, of class ByteArrayElement.
	 */
	@Test
	public void testGetByteArray() {
		ByteArrayMonoid bam = ByteArrayMonoid.getInstance(3);
		Element element = bam.getElement(new byte[]{(byte) -1, (byte) 2, (byte) 3});
		Element element2 = bam.getElement(element.getValue());
		Assert.assertEquals("Should be: " + element, element, element2);
//		System.out.println("getByteArray");
//		ByteArrayElement instance = null;
//		byte[] expResult = null;
//		byte[] result = instance.getByteArray();
//		assertArrayEquals(expResult, result);
//		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case is a prototype.");
//		byte[] bytes = new byte[]{(byte) -1, (byte) 2, (byte) 3};
//		BigInteger value1 = BigInteger.ZERO;
//		//BigInteger byteSize = BigInteger.valueOf(1 << Byte.SIZE);
//		for (byte b : bytes) {
//			int intValue = b & 0xFF;
//			value1 = value1.shiftLeft(Byte.SIZE).add(BigInteger.valueOf(intValue));
//		}
//		BigInteger value1 = new BigInteger(1, bytes);
//		System.out.println(value1);
	}

	/**
	 * Test of getLength method, of class ByteArrayElement.
	 */
	@Test
	@Ignore
	public void testGetLength() {
		System.out.println("getLength");
		ByteArrayElement instance = null;
		int expResult = 0;
		int result = instance.getLength();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of standardGetValue method, of class ByteArrayElement.
	 */
	@Test
	@Ignore

	public void testStandardGetValue() {
		System.out.println("standardGetValue");
		ByteArrayElement instance = null;
		BigInteger expResult = null;
		BigInteger result = instance.standardGetValue();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of standardIsEquivalent method, of class ByteArrayElement.
	 */
	@Test
	@Ignore

	public void testStandardIsEquivalent() {
		System.out.println("standardIsEquivalent");
		ByteArrayElement element = null;
		ByteArrayElement instance = null;
		boolean expResult = false;
		boolean result = instance.standardIsEquivalent(element);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of standardToStringContent method, of class ByteArrayElement.
	 */
	@Test
	@Ignore

	public void testStandardToStringContent() {
		System.out.println("standardToStringContent");
		ByteArrayElement instance = null;
		String expResult = "";
		String result = instance.standardToStringContent();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

}
