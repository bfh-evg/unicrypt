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
package ch.bfh.unicrypt.crypto.random.classes;

import ch.bfh.unicrypt.math.algebra.dualistic.classes.Z;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.helper.HashMethod;
import java.math.BigInteger;
import java.util.HashSet;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class PseudoRandomGeneratorTest {

	public PseudoRandomGeneratorTest() {
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
	 * Test of getSeed method, of class PseudoRandomGenerator.
	 */
	@Test
	public void testGetDefaultSeed() {
		System.out.println("getSeed");
		PseudoRandomGenerator instance = PseudoRandomGenerator.getInstance();
		Element seed = instance.getSeed();
		Assert.assertTrue(seed.isEquivalent(PseudoRandomGenerator.DEFAULT_SEED));
	}

	/**
	 * Test of getSeed method, of class PseudoRandomGenerator.
	 */
	@Test
	public void testGetSeed() {
		System.out.println("getSeed");
		Element expSeed = Z.getInstance().getElement(4711);
		PseudoRandomGenerator instance = PseudoRandomGenerator.getInstance(expSeed);
		Element seed = instance.getSeed();
		Assert.assertTrue(expSeed.isEquivalent(seed));
	}

	/**
	 * Test of getCounter method, of class PseudoRandomGenerator.
	 */
	@Test
	public void testGetCounter() {
		System.out.println("getCounter");
		PseudoRandomGenerator instance = PseudoRandomGenerator.getInstance();
		int counter = instance.getCounter();
		Assert.assertTrue(0 == counter);
		instance.nextBytes(HashMethod.DEFAULT.getLength());
		counter = instance.getCounter();
		Assert.assertTrue(1 == counter);
		instance.nextBytes(1);
		counter = instance.getCounter();
		Assert.assertTrue(1 == counter);

	}

	/**
	 * Test of abstractNextBoolean method, of class PseudoRandomGenerator.
	 */
	@Test
	public void testAbstractNextBoolean() {
		System.out.println("abstractNextBoolean");
		PseudoRandomGenerator instance1 = PseudoRandomGenerator.getInstance();

		for (int i = 0; i < 1000; i++) {
			if (instance1.nextBoolean()) {
				for (int j = 0; j < 1000; i++) {
					if (!instance1.nextBoolean()) {
						return;
					}

				}
			}
		}
		Assert.fail();
	}

	/**
	 * Test of abstractNextBoolean method, of class PseudoRandomGenerator.
	 */
	@Test
	public void testAbstractNextBooleanSame() {
		System.out.println("abstractNextBoolean");
		PseudoRandomGenerator instance1 = PseudoRandomGenerator.getInstance();
		PseudoRandomGenerator instance2 = PseudoRandomGenerator.getInstance();

		boolean b1 = instance1.nextBoolean();
		boolean b2 = instance2.nextBoolean();
		Assert.assertTrue(b1 == b2);

	}

	/**
	 * Test of abstractNextBoolean method, of class PseudoRandomGenerator.
	 */
	@Test
	public void testAbstractNextBooleansSame() {
		System.out.println("abstractNextBoolean");
		PseudoRandomGenerator instance1 = PseudoRandomGenerator.getInstance();
		PseudoRandomGenerator instance2 = PseudoRandomGenerator.getInstance();

		for (int i = 0; i < 4096; i++) {
			boolean b1 = instance1.nextBoolean();
			boolean b2 = instance2.nextBoolean();
			Assert.assertTrue(b1 == b2);
		}
	}

	/**
	 * Test of abstractNextBytes method, of class PseudoRandomGenerator.
	 */
	@Test
	public void testNextBytesSingleByteZero() {
		System.out.println("abstractNextBytes");
		int byteArrayLength = 1;
		PseudoRandomGenerator instance = PseudoRandomGenerator.getInstance();
		byte[] expResult = Pair.getInstance(PseudoRandomGenerator.DEFAULT_SEED, Z.getInstance().getElement(0)).getHashValue().getByteArray().getBytes();
		byte[] result = instance.nextBytes(byteArrayLength);
//TODO:		Assert.assertTrue(expResult[0] == result[0]);
	}

	/**
	 * Test of abstractNextBytes method, of class PseudoRandomGenerator.
	 */
	@Test
	public void testNextBytesSingleBytes() {
		System.out.println("abstractNextBytes");
		int byteArrayLength = 1;
		PseudoRandomGenerator instance = PseudoRandomGenerator.getInstance();
		byte[] expResult = Pair.getInstance(PseudoRandomGenerator.DEFAULT_SEED, Z.getInstance().getElement(0)).getHashValue().getByteArray().getBytes();
		for (int i = 0; i < expResult.length; i++) {
			byte[] result = instance.nextBytes(byteArrayLength);
//TODO:			Assert.assertTrue("Byte no: " + i, expResult[i] == result[0]);

		}
	}

	/**
	 * Test of abstractNextBytes method, of class PseudoRandomGenerator.
	 */
	@Test
	public void testNextBytesSingleBytesMulti() {
		System.out.println("abstractNextBytes");
		int byteArrayLength = 1;
		PseudoRandomGenerator instance = PseudoRandomGenerator.getInstance();
		for (int j = 0; j < 10; j++) {
			byte[] expResult = Pair.getInstance(PseudoRandomGenerator.DEFAULT_SEED, Z.getInstance().getElement(j)).getHashValue().getByteArray().getBytes();
			for (int i = 0; i < expResult.length; i++) {
				byte[] result = instance.nextBytes(byteArrayLength);
//TODO:				Assert.assertTrue("Byte no: " + i, expResult[i] == result[0]);

			}
		}
	}

	/**
	 * Test of abstractNextBytes method, of class PseudoRandomGenerator.
	 */
	@Test
	public void testNextBytesDigestBytes() {
		System.out.println("abstractNextBytes");
		int byteArrayLength = HashMethod.DEFAULT.getLength();
		PseudoRandomGenerator instance = PseudoRandomGenerator.getInstance();
		byte[] expResult = Pair.getInstance(PseudoRandomGenerator.DEFAULT_SEED, Z.getInstance().getElement(0)).getHashValue().getByteArray().getBytes();
		byte[] result = instance.nextBytes(byteArrayLength);
//TODO:		Assert.assertTrue(Arrays.equals(expResult, result));
	}

	/**
	 * Test of abstractNextBytes method, of class PseudoRandomGenerator.
	 */
	@Test
	public void testNextBytesTwiceDigestBytes() {
		System.out.println("abstractNextBytes");
		int byteArrayLength = HashMethod.DEFAULT.getLength();
		PseudoRandomGenerator instance = PseudoRandomGenerator.getInstance();
		instance.nextBytes(byteArrayLength);
		byte[] expResult = Pair.getInstance(PseudoRandomGenerator.DEFAULT_SEED, Z.getInstance().getElement(1)).getHashValue().getByteArray().getBytes();
		byte[] result = instance.nextBytes(byteArrayLength);
//TODO:		Assert.assertTrue(Arrays.equals(expResult, result));

	}

	/**
	 * Test of abstractNextInteger method, of class PseudoRandomGenerator.
	 */
	@Test
	public void testAbstractNextInteger() {
		System.out.println("abstractNextInteger");
		PseudoRandomGenerator instance = PseudoRandomGenerator.getInstance();
		boolean maxReached = false;
		HashSet<Integer> numbers = new HashSet<Integer>();
		for (int i = 0; i < 10000; i++) {
			int result = instance.nextInteger(1023);
			numbers.add(result);
			if (result == 1023) {
				maxReached = true;
			}
			Assert.assertTrue("" + result, result >= 0 && result < 1024);
		}
		Assert.assertTrue("Amount: " + numbers.size(), numbers.size() == 1024);
		Assert.assertTrue(maxReached);
	}

	/**
	 * Test of abstractNextBigInteger method, of class PseudoRandomGenerator.
	 */
	@Test
	public void testAbstractNextBigInteger_int1() {
		System.out.println("abstractNextBigInteger");
		PseudoRandomGenerator instance = PseudoRandomGenerator.getInstance();
		for (int i = 0; i < 1000; i++) {
			BigInteger result = instance.nextBigInteger(1);
			Assert.assertTrue(result.intValue() == 0 || result.intValue() == 1);
		}
	}

	/**
	 * Test of abstractNextBigInteger method, of class PseudoRandomGenerator.
	 */
	@Test
	public void testAbstractNextBigInteger_int10() {
		System.out.println("abstractNextBigInteger");
		PseudoRandomGenerator instance = PseudoRandomGenerator.getInstance();
		boolean maxReached = false;
		HashSet<BigInteger> numbers = new HashSet<BigInteger>();
		for (int i = 0; i < 10000; i++) {
			BigInteger result = instance.nextBigInteger(10);
			numbers.add(result);
			if (result.intValue() == 1023) {
				maxReached = true;
			}
			Assert.assertTrue("" + result, result.intValue() >= 0 && result.intValue() < 1024);
		}
		Assert.assertTrue("Amount: " + numbers.size(), numbers.size() == 1024 / 2);
		Assert.assertTrue(maxReached);

	}

	/**
	 * Test of abstractNextBigInteger method, of class PseudoRandomGenerator.
	 */
	@Test
	public void testAbstractNextBigInteger_int8() {
		System.out.println("abstractNextBigInteger");
		PseudoRandomGenerator instance = PseudoRandomGenerator.getInstance();
		boolean maxReached = false;
		HashSet<BigInteger> numbers = new HashSet<BigInteger>();
		for (int i = 0; i < 1000; i++) {
			BigInteger result = instance.nextBigInteger(8);
			numbers.add(result);
			if (result.intValue() == 255) {
				maxReached = true;
			}
			Assert.assertTrue("" + result, result.intValue() >= 0 && result.intValue() < 256);

		}
		Assert.assertTrue(maxReached);
		Assert.assertTrue("Amount: " + numbers.size(), numbers.size() == 256 / 2);

	}

	/**
	 * Test of abstractNextBigInteger method, of class PseudoRandomGenerator.
	 */
	@Test
	public void testAbstractNextBigInteger_BigInteger10() {
		System.out.println("abstractNextBigInteger");
		PseudoRandomGenerator instance = PseudoRandomGenerator.getInstance();
		boolean maxReached = false;
		HashSet<BigInteger> numbers = new HashSet<BigInteger>();
		for (int i = 0; i < 10000; i++) {
			BigInteger result = instance.nextBigInteger(BigInteger.valueOf(1023));
			numbers.add(result);

			if (result.intValue() == 1023) {
				maxReached = true;
			}
			Assert.assertTrue("" + result, result.intValue() >= 0 && result.intValue() < 1024);

		}
		Assert.assertTrue(maxReached);
		Assert.assertTrue("Amount: " + numbers.size(), numbers.size() == 1024);

	}

	/**
	 * Test of abstractNextBigInteger method, of class PseudoRandomGenerator.
	 */
	@Test
	public void testAbstractNextBigInteger_BigInteger8() {
		System.out.println("abstractNextBigInteger");
		PseudoRandomGenerator instance = PseudoRandomGenerator.getInstance();
		boolean maxReached = false;
		HashSet<BigInteger> numbers = new HashSet<BigInteger>();

		for (int i = 0; i < 10000; i++) {
			BigInteger result = instance.nextBigInteger(BigInteger.valueOf(255));
			numbers.add(result);

			if (result.intValue() == 255) {
				maxReached = true;
			}
			Assert.assertTrue("" + result, result.intValue() >= 0 && result.intValue() < 256);

		}
		Assert.assertTrue(maxReached);
		Assert.assertTrue("Amount: " + numbers.size(), numbers.size() == 256);

	}

	/**
	 * Test of abstractNextPrime method, of class PseudoRandomGenerator.
	 */
	@Test
	public void testAbstractNextPrime8() {
		System.out.println("abstractNextPrime");
		PseudoRandomGenerator instance = PseudoRandomGenerator.getInstance();
		HashSet<BigInteger> primes = new HashSet<BigInteger>();
		for (int i = 0; i < 10000; i++) {
			BigInteger result = instance.nextPrime(8);
			primes.add(result);
		}
		Assert.assertTrue("Size: " + primes.size(), primes.size() == 23);
	}

}
