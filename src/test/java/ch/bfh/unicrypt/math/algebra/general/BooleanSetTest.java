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

import ch.bfh.unicrypt.crypto.random.classes.CounterModeRandomByteSequence;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomByteSequence;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.ZStarMod;
import java.math.BigInteger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
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
	 * Test of getElement method, of class BooleanSet.
	 */
	@Test
	public void testGetElement() {
//		System.out.println("getElement");
		boolean bit = false;
		BooleanSet instance = BooleanSet.getInstance();
		BooleanElement result = instance.getElement(bit);
		assertEquals(false, result.getValue());
		BooleanElement result1 = instance.getElement((!bit));
		assertEquals(true, result1.getValue());

	}

	/**
	 * Test of abstractGetElement method, of class BooleanSet.
	 */
	@Test
	public void testGetElementBigInteger() {
//		System.out.println("getElement BigInteger");
		BigInteger value = null;
		BooleanSet instance = BooleanSet.getInstance();
		BooleanElement result = instance.getElement(BigInteger.ZERO);
		assertEquals(false, result.getValue());

		result = instance.getElement(BigInteger.ONE);
		assertEquals(true, result.getValue());

		try {
			BooleanElement expResult = result;
			result = instance.getElement(BigInteger.TEN);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			//ok
		}
	}

	/**
	 * Test of abstractGetElement method, of class BooleanSet.
	 */
	@Test
	public void testGetElementElement() {
//		System.out.println("getElement Element");
		BigInteger value = null;
		BooleanSet instance = BooleanSet.getInstance();
		BooleanElement expResult = instance.getElement(true);
		BooleanElement result = instance.getElement(expResult);
		assertEquals(expResult, result);

		ZMod zmod = ZMod.getInstance(5);
		Element element = zmod.getElement(1);
		result = instance.getElement(element);
		assertEquals(true, result.getValue());

		element = zmod.getElement(0);
		result = instance.getElement(element);
		assertEquals(false, result.getValue());

		element = zmod.getElement(4);
		try {
			result = instance.getElement(element);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			//ok
		}

	}

	/**
	 * Test of abstractGetElement method, of class BooleanSet.
	 */
	@Test
	public void testGetElementBoolean() {
//		System.out.println("getElement Boolean");
		BigInteger value = null;
		BooleanSet instance = BooleanSet.getInstance();
		BooleanElement expResult = instance.getElement(true);
		BooleanElement result = instance.getElement(false);
		Assert.assertNotSame(expResult.getValue(), result.getValue());

	}

	/**
	 * Test of abstractGetElement method, of class BooleanSet.
	 */
	@Test
	public void testGetElementInt() {
//		System.out.println("getElement int");

		{
			BooleanSet instance = BooleanSet.getInstance();
			BooleanElement expResult = instance.getElement(true);
			BooleanElement result = instance.getElement(1);
			//          assertEquals(expResult, result);
		}
		{
			BooleanSet instance = BooleanSet.getInstance();
			BooleanElement expResult = instance.getElement(false);
			BooleanElement result = instance.getElement(0);
			//          assertEquals(expResult, result);
		}

		{
			BooleanSet instance = BooleanSet.getInstance();
			try {
				BooleanElement result = instance.getElement(2);
				Assert.fail();
			} catch (IllegalArgumentException ex) {
				//ok
			}

		}
		{
			BooleanSet instance = BooleanSet.getInstance();
			try {
				BooleanElement result = instance.getElement(-1);
				Assert.fail();
			} catch (IllegalArgumentException ex) {
				//ok
			}

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
		RandomByteSequence random = CounterModeRandomByteSequence.getInstance();
//		System.out.println("getRandomElement");
		int counter = 0;
		while (BooleanSet.getInstance().getRandomElement(random).getValue() && counter++ < 100);
		if (counter >= 100) {
			Assert.fail();
		}
		while (!(BooleanSet.getInstance().getRandomElement(random).getValue()) && counter++ < 100);
		if (counter >= 100) {
			Assert.fail();
		}

	}

	/**
	 * Test of getZModOrder method, of class BooleanSet.
	 */
	@Test
	public void testGetZModOrder() {
//		System.out.println("getZModOrder");
		ZModPrime expResult = ZModPrime.getInstance(2);
		ZModPrime result = (ZModPrime) BooleanSet.getInstance().getZModOrder();

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
	 * Test of areEqual method, of class BooleanSet.
	 */
	@Test
	public void testAreEqual() {
//		System.out.println("areEqual");
		BooleanSet expResult = null;
		Assert.assertTrue((BooleanSet.getInstance().areEqual(BooleanSet.TRUE, BooleanSet.TRUE)));
		Assert.assertTrue(!(BooleanSet.getInstance().areEqual(BooleanSet.FALSE, BooleanSet.TRUE)));
	}

	/**
	 * Test of contains method, of class BooleanSet.
	 */
	@Test
	public void testContains() {
//		System.out.println("contains");
		Assert.assertTrue(BooleanSet.getInstance().contains(BigInteger.ZERO));
		Assert.assertTrue(BooleanSet.getInstance().contains(BigInteger.ONE));
		Assert.assertTrue(!BooleanSet.getInstance().contains(BigInteger.TEN));

	}

	/**
	 * Test of contains method, of class BooleanSet.
	 */
	@Test
	public void testContains1() {
//		System.out.println("contains1");
		ZMod zmod = ZMod.getInstance(BigInteger.TEN);
		Assert.assertTrue(!BooleanSet.getInstance().contains(zmod.getElement(0)));
		Assert.assertTrue(!BooleanSet.getInstance().contains(zmod.getElement(1)));
		Assert.assertTrue(!BooleanSet.getInstance().contains(zmod.getElement(2)));
	}

	/**
	 * Test of contains method, of class BooleanSet.
	 */
	@Test
	public void testContains2() {
//		System.out.println("contains2");
		Assert.assertTrue(BooleanSet.getInstance().contains(0));
		Assert.assertTrue(BooleanSet.getInstance().contains(1));
		Assert.assertTrue(!BooleanSet.getInstance().contains(2));
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
	 * Test of isCompatible method, of class BooleanSet.?
	 */
	@Test
	public void testIsCompatible() {
//		System.out.println("isCompatible");
		Assert.assertTrue(BooleanSet.getInstance().isCompatible(BooleanSet.getInstance()));
		Assert.assertTrue(!BooleanSet.getInstance().isCompatible(ZMod.getInstance(2)));
		Assert.assertTrue(!BooleanSet.getInstance().isCompatible(ZMod.getInstance(3)));
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
	 * Test of isEmpty method, of class BooleanSet.?
	 */
	@Test
	public void testIsEmpty() {
//		System.out.println("isEmpty");
		Assert.assertTrue(!BooleanSet.getInstance().isEmpty());
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
