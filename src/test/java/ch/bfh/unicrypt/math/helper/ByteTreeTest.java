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

import ch.bfh.unicrypt.math.helper.bytetree.ByteTree;
import java.nio.ByteBuffer;
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
public class ByteTreeTest {

	public ByteTreeTest() {
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
	 * Test of getInstance method, of class ByteTree.
	 */
	@Test
	@Ignore
	public void testGetInstance_ByteTreeArr() {
//		System.out.println("getInstance");
		ByteTree[] children = null;
		ByteTree expResult = null;
		ByteTree result = ByteTree.getInstance(children);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getInstance method, of class ByteTree.
	 */
//	@Test
//	@Ignore
//	public void testGetInstance_byteArr() {
////		System.out.println("getInstance");
//		ByteArray bytes = null;
//		ByteTree expResult = null;
//		ByteTree result = ByteTree.getInstance(bytes);
//		assertEquals(expResult, result);
//		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case is a prototype.");
//	}
//	/**
//	 * Test of getInstanceFrom method, of class ByteTree.
//	 */
//	@Test
//	@Ignore
//
//	public void testGetDeserializedInstance() {
////		System.out.println("getInstanceFrom");
//		ByteArray bytes = null;
//		ByteTree expResult = null;
//		ByteTree result = ByteTree.getInstanceFrom(bytes);
//		assertEquals(expResult, result);
//		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case is a prototype.");
//	}
	/**
	 * Test of getByteArray method, of class ByteTree.
	 */
	@Test
	@Ignore
	public void testGetSerializedByteTree() {
//		System.out.println("getByteArray");
		ByteTree instance = new ByteTreeImpl();
		ByteArray expResult = null;
		ByteArray result = instance.getByteArray();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of defaultGetByteArray method, of class ByteTree.
	 */
	@Test
	@Ignore

	public void testDefaultSerialize() {
//		System.out.println("defaultGetByteArray");
		ByteBuffer buffer = null;
		ByteTree instance = new ByteTreeImpl();
//		instance.defaultGetByteArray(buffer);
//		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case is a prototype.");
	}

	/**
	 * Test of defaultGetSize method, of class ByteTree.
	 */
	@Test
	@Ignore

	public void testDefaultGetSize() {
//		System.out.println("defaultGetSize");
		ByteTree instance = new ByteTreeImpl();
		int expResult = 0;
//		int result = instance.defaultGetSize();
//		assertEquals(expResult, result);
//		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case is a prototype.");
	}

	/**
	 * Test of abstractGetByteArray method, of class ByteTree.
	 */
	@Test
	@Ignore

	public void testAbstractSerialize() {
//		System.out.println("abstractGetByteArray");
		ByteBuffer buffer = null;
		ByteTree instance = new ByteTreeImpl();
//		instance.abstractGetByteArray(buffer);
//		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case is a prototype.");
	}

	/**
	 * Test of abstractGetSize method, of class ByteTree.
	 */
	@Test
	@Ignore

	public void testAbstractGetSize() {
//		System.out.println("abstractGetSize");
		ByteTree instance = new ByteTreeImpl();
		int expResult = 0;
//		int result = instance.abstractGetSize();
//		assertEquals(expResult, result);
//		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case is a prototype.");
	}

	public class ByteTreeImpl
		   extends ByteTree {

		public void abstractGetByteArray(ByteBuffer buffer) {
		}

		public int abstractGetSize() {
			return 0;
		}

	}

}
