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
package ch.bfh.unicrypt.random.classes;

import ch.bfh.unicrypt.helper.array.ByteArray;
import ch.bfh.unicrypt.helper.hash.HashAlgorithm;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Reto E. Koenig <reto.koenig@bfh.ch>
 */
public class ReferenceRandomByteSequenceTest {

	public ReferenceRandomByteSequenceTest() {
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
	 * Test of getRandomByteBuffer method, of class ReferenceRandomByteSequence.
	 */
	@Test
	public void testGetRandomByteBuffer() {
		// System.out.println("getDefault");
		ReferenceRandomByteSequence instance = ReferenceRandomByteSequence.getInstance();
		ByteArray expResult = instance.getNextByteArray(10);
		ReferenceRandomByteSequence instance2 = ReferenceRandomByteSequence.getInstance();
		ByteArray result = instance2.getNextByteArray(10);

		Assert.assertEquals(expResult, result);

	}

	/**
	 * Test of reset method, of class ReferenceRandomByteSequence.
	 */
	@Test
	public void testReset() {
		// System.out.println("testReset");
		ReferenceRandomByteSequence instance = PseudoRandomOracle.getInstance().getReferenceRandomByteSequence(ByteArray.getInstance("testReset".getBytes()));
		ReferenceRandomByteSequence instance2 = PseudoRandomOracle.getInstance().getReferenceRandomByteSequence(ByteArray.getInstance("testReset".getBytes()));

		long time = System.currentTimeMillis();
		ByteArray expResult = instance.getNextByteArray(100000);
		long expTime = System.currentTimeMillis() - time;
		instance.reset();
		time = System.currentTimeMillis();
		ByteArray result = instance.getNextByteArray(100000);
		long resTime = System.currentTimeMillis() - time;
		Assert.assertEquals(expResult, result);
		// System.out.println("Timing: " + (expTime - resTime));
		Assert.assertTrue(expTime > resTime);
	}

	/**
	 * Test of isReset method, of class ReferenceRandomByteSequence.
	 */
	@Test
	public void testIsReset() {
		// System.out.println("isReset");
		ReferenceRandomByteSequence instance = ReferenceRandomByteSequence.getInstance();
		instance.reset();
		Assert.assertTrue(instance.isReset());
		instance.getNextByte();
		Assert.assertFalse(instance.isReset());
		instance = ReferenceRandomByteSequence.getInstance();
		Assert.assertTrue(instance.isReset());

	}

	/**
	 * Test of getInstance method, of class ReferenceRandomByteSequence.
	 */
	@Test
	public void testGetInstance_ByteArray() {
		// System.out.println("getInstance");
		ByteArray seed = ByteArray.getInstance("testGetInstance_ByteArray".getBytes());
		ReferenceRandomByteSequence instance = ReferenceRandomByteSequence.getInstance(seed);
		ReferenceRandomByteSequence instance2 = ReferenceRandomByteSequence.getInstance(seed);

		assertEquals(instance.getNextByteArray(10), instance2.getNextByteArray(10));

		ReferenceRandomByteSequence instance3 = ReferenceRandomByteSequence.getInstance();
		instance.reset();
		instance2.reset();
		instance3.reset();
		Assert.assertFalse(instance.getNextByteArray(10).equals(instance3.getNextByteArray(10)));
		instance2.getNextByteArray(10);
		assertEquals(instance.getNextByteArray(10), instance2.getNextByteArray(10));

	}

	/**
	 * Test of getInstance method, of class ReferenceRandomByteSequence.
	 */
	@Test
	public void testGetInstance_HashAlgorithm() {
		// System.out.println("getInstance");
		HashAlgorithm hashAlgorithm = HashAlgorithm.getInstance();
		HashAlgorithm expResult = hashAlgorithm;
		HashAlgorithm result = ReferenceRandomByteSequence.getInstance(hashAlgorithm).getHashAlgorithm();
		assertEquals(expResult, result);

	}

	/**
	 * Test of hashCode method, of class ReferenceRandomByteSequence.
	 */
	@Test
	public void testHashCode() {
		// System.out.println("hashCode");
		ReferenceRandomByteSequence instance = ReferenceRandomByteSequence.getInstance();
		ReferenceRandomByteSequence instance2 = ReferenceRandomByteSequence.getInstance();
		assertEquals(instance.hashCode(), instance2.hashCode());

		instance.getNextByte();
		assertEquals(instance.hashCode(), instance2.hashCode());

	}

	/**
	 * Test of equals method, of class ReferenceRandomByteSequence.
	 */
	@Test
	public void testEquals() {
		// System.out.println("equals");
		ReferenceRandomByteSequence instance = ReferenceRandomByteSequence.getInstance();
		ReferenceRandomByteSequence instance2 = ReferenceRandomByteSequence.getInstance();
		assertEquals(instance, instance2);
		instance.getNextByte();
		assertEquals(instance, instance2);
	}

}
