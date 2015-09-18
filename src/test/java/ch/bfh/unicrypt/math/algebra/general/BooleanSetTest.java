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
package ch.bfh.unicrypt.math.algebra.general;

import ch.bfh.unicrypt.UniCryptException;
import ch.bfh.unicrypt.helper.math.MathUtil;
import ch.bfh.unicrypt.helper.random.deterministic.CTR_DRBG;
import ch.bfh.unicrypt.helper.random.deterministic.DeterministicRandomByteSequence;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanSet;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.ZStarMod;
import java.math.BigInteger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author reto
 */
public class BooleanSetTest {

	public BooleanSetTest() {
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
	 * Test of getElementFromFromFromFrom method, of class BooleanSet.
	 */
	@Test
	public void testGetElement() {
		boolean bit = false;
		BooleanSet instance = BooleanSet.getInstance();
		BooleanElement result = instance.getElement(bit);
		assertEquals(false, result.getValue());
		BooleanElement result1 = instance.getElement((!bit));
		assertEquals(true, result1.getValue());

	}

	@Test
	public void testGetElementFromBigInteger() {
		BigInteger value = null;
		BooleanSet instance = BooleanSet.getInstance();
		BooleanElement result;
		try {
			result = instance.getElementFrom(MathUtil.ZERO);
			assertEquals(false, result.getValue());
		} catch (UniCryptException ex) {
			fail();
		}

		try {
			result = instance.getElementFrom(MathUtil.ONE);
			assertEquals(true, result.getValue());
		} catch (UniCryptException ex) {
			fail();
		}

	}

	@Test
	public void testGetElementBoolean() {
		BigInteger value = null;
		BooleanSet instance = BooleanSet.getInstance();
		BooleanElement expResult = instance.getElement(true);
		BooleanElement result = instance.getElement(false);
		Assert.assertNotSame(expResult.getValue(), result.getValue());

	}

	@Test
	public void testGetElementInt() {

		try {
			BooleanSet instance = BooleanSet.getInstance();
			BooleanElement expResult = instance.getElement(true);
			BooleanElement result = instance.getElementFrom(1);
			assertEquals(expResult, result);
		} catch (Exception e) {
			fail();
		}
		try {
			BooleanSet instance = BooleanSet.getInstance();
			BooleanElement expResult = instance.getElement(false);
			BooleanElement result = instance.getElementFrom(0);
			assertEquals(expResult, result);
		} catch (Exception e) {
			fail();
		}

	}

	/**
	 * Test of getOrderUpperBound method, of class BooleanSet.
	 */
	@Test
	public void testGetMaxOrder() {
//		System.out.println("getMaxOrder");
		BigInteger expResult = BigInteger.valueOf(2);
		BigInteger result = BooleanSet.getInstance().getOrderUpperBound();
		Assert.assertEquals(expResult, result);
	}

	/**
	 * Test of getOrderLowerBound method, of class BooleanSet.
	 */
	@Test
	public void testGetMinOrder() {
//		System.out.println("getMinOrder");
		BigInteger expResult = BigInteger.valueOf(2);
		BigInteger result = BooleanSet.getInstance().getOrderLowerBound();
		Assert.assertEquals(expResult, result);
	}

	/**
	 * Test of getOrder method, of class BooleanSet.
	 */
	@Test
	public void testGetOrder() {
//		System.out.println("getOrder");
		BigInteger expResult = BigInteger.valueOf(2);
		BigInteger result = BooleanSet.getInstance().getOrder();
		Assert.assertEquals(expResult, result);
	}

	/**
	 * Test of getRandomElement method, of class BooleanSet.
	 */
	@Test
	public void testGetRandomElement() {
//		System.out.println("getRandomElement");
		int counter = 0;
		while (BooleanSet.getInstance().getRandomElement().getValue() && counter++ < 100);
		if (counter >= 100) {
			Assert.fail();
		}
		while (!(BooleanSet.getInstance().getRandomElement().getValue()) && counter++ < 100);
		if (counter >= 100) {
			Assert.fail();
		}

	}

	/**
	 * Test of getRandomElement2 method, of class BooleanSet.
	 */
	@Test
	public void testGetRandomElement2() {
		DeterministicRandomByteSequence random = CTR_DRBG.getFactory().getInstance().getRandomByteSequence();
		boolean result = true;
		for (BooleanElement element : BooleanSet.getInstance().getRandomElements(random).limit(100)) {
			result = result && element.getValue();
		}
		Assert.assertFalse(result);
		result = false;
		for (BooleanElement element : BooleanSet.getInstance().getRandomElements(random).limit(100)) {
			result = result || element.getValue();
		}
		Assert.assertTrue(result);

	}

	/**
	 * Test of getZModOrder method, of class BooleanSet.
	 */
	@Test
	public void testGetZModOrder() {
//		System.out.println("getZModOrder");
		ZModPrime expResult = ZModPrime.getInstance(2);
		ZModPrime result = BooleanSet.getInstance().getZModOrder();

		// TODO implement equals methods in sets
		Assert.assertTrue(expResult.isEquivalent(result));
	}

	/**
	 * Test of getZStarModOrder method, of class BooleanSet.
	 */
	@Test
	public void testGetZStarModOrder() {
//		System.out.println("getZStarModOrder");
		ZStarMod expResult = ZStarMod.getInstance(2);
		ZStarMod result = BooleanSet.getInstance().getZStarModOrder();
		// TODO implement equals methods in sets
		Assert.assertTrue(expResult.isEquivalent(result));
	}

	/**
	 * Test of contains method, of class BooleanSet.
	 */
	@Test
	public void testContains() {
//		System.out.println("contains");
		Assert.assertTrue(BooleanSet.getInstance().contains(true));
		Assert.assertTrue(BooleanSet.getInstance().contains(false));

	}

	/**
	 * Test of isEquivalent method, of class BooleanSet.
	 */
	@Test
	public void testEquals() {
//		System.out.println("equals");
		Assert.assertTrue(BooleanSet.getInstance().isEquivalent(BooleanSet.getInstance()));
		{
			ZMod zmod = ZMod.getInstance(2);
			Assert.assertTrue(!BooleanSet.getInstance().isEquivalent(zmod));
		}
		{
			ZMod zmod = ZMod.getInstance(3);
			Assert.assertTrue(!BooleanSet.getInstance().isEquivalent(zmod));
		}

	}

	/**
	 * Test of hasKnownOrder method, of class BooleanSet.
	 */
	@Test
	public void testhasKnownOrder() {
//		System.out.println("hasKnownOrder");
		Assert.assertTrue(BooleanSet.getInstance().hasKnownOrder());
	}

	/**
	 * Test of isAdditive method, of class BooleanSet.
	 */
	@Test
	public void testIsAdditive() {
//		System.out.println("isAdditive");
		Assert.assertTrue(!BooleanSet.getInstance().isAdditive());
	}

	/**
	 * Test of isConcatenative method, of class BooleanSet.?
	 */
	@Test
	public void testIsConcatenative() {
//		System.out.println("isConcatenative");
		Assert.assertTrue(!BooleanSet.getInstance().isConcatenative());
	}

	/**
	 * Test of isCyclic method, of class BooleanSet.?
	 */
	@Test
	public void testIsCyclic() {
//		System.out.println("isCyclic");
		Assert.assertTrue(!BooleanSet.getInstance().isCyclic());
	}

	/**
	 * Test of isField method, of class BooleanSet.?
	 */
	@Test
	public void testIsField() {
//		System.out.println("isField");
		Assert.assertTrue(!BooleanSet.getInstance().isField());
	}

	/**
	 * Test of isField method, of class BooleanSet.?
	 */
	@Test
	public void testIsFinite() {
//		System.out.println("isFinite");
		Assert.assertTrue(BooleanSet.getInstance().isFinite());
	}

	/**
	 * Test of isGroup method, of class BooleanSet.?
	 */
	@Test
	public void testIsGroup() {
//		System.out.println("isGroup");
		Assert.assertTrue(!BooleanSet.getInstance().isGroup());
	}

	/**
	 * Test of isMonoid method, of class BooleanSet.?
	 */
	@Test
	public void testIsMonoid() {
//		System.out.println("isMonoid");
		Assert.assertTrue(!BooleanSet.getInstance().isMonoid());
	}

	/**
	 * Test of isMultiplicative method, of class BooleanSet.?
	 */
	@Test
	public void testIsMultiplicative() {
//		System.out.println("isMultiplicative");
		Assert.assertTrue(!BooleanSet.getInstance().isMultiplicative());
	}

	/**
	 * Test of isProduct method, of class BooleanSet.?
	 */
	@Test
	public void testIsProduct() {
//		System.out.println("isProduct");
		Assert.assertTrue(!BooleanSet.getInstance().isProduct());
	}

	/**
	 * Test of isRing method, of class BooleanSet.?
	 */
	@Test
	public void testIsRing() {
//		System.out.println("isRing");
		Assert.assertTrue(!BooleanSet.getInstance().isRing());
	}

	/**
	 * Test of isSemiGroup method, of class BooleanSet.?
	 */
	@Test
	public void testIsSemiGroup() {
//		System.out.println("isSemiGroup");
		Assert.assertTrue(!BooleanSet.getInstance().isSemiGroup());
	}

	/**
	 * Test of isSemiRing method, of class BooleanSet.?
	 */
	@Test
	public void testIsSemiRing() {
//		System.out.println("isSemiRing");
		Assert.assertTrue(!BooleanSet.getInstance().isSemiRing());
	}

	/**
	 * Test of isSingleton method, of class BooleanSet.?
	 */
	@Test
	public void testIsSingleton() {
//		System.out.println("isSingleton");
		Assert.assertTrue(!BooleanSet.getInstance().isSingleton());
	}

	/**
	 * Test of getInstance method, of class BooleanSet.
	 */
	@Test
	public void testGetInstance() {
//		System.out.println("getInstance");
		BooleanSet expResult = null;
		BooleanSet result = BooleanSet.getInstance();
		Assert.assertNotNull(result);

		expResult = result;
		result = BooleanSet.getInstance();

		Assert.assertEquals(expResult, result);
	}

}
