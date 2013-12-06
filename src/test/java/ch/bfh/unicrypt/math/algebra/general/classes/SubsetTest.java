/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.general.classes;

import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class SubsetTest {

	public SubsetTest() {
	}

	/**
	 * Test of getSuperset method, of class Subset.
	 */
	@Test
	public void testSubset() {
		ZMod zMod = ZMod.getInstance(15);
		ZModElement e1 = zMod.getElement(4);
		ZModElement e2 = zMod.getElement(5);
		ZModElement e3 = zMod.getElement(7);
		ZModElement e4 = zMod.getElement(7);
		ZModElement e5 = zMod.getElement(7);
		ZModElement e6 = zMod.getElement(9);
		Subset subset = Subset.getInstance(zMod, new Element[]{e1, e2, e3, e3, e4});
		Assert.assertTrue(subset.contains(e1));
		Assert.assertTrue(subset.contains(e2));
		Assert.assertTrue(subset.contains(e3));
		Assert.assertTrue(subset.contains(e4));
		Assert.assertTrue(subset.contains(e5));
		Assert.assertFalse(subset.contains(e6));
		Assert.assertEquals(3, subset.getOrder().intValue());
		for (Element element : subset) {
			System.out.println(element);
		}
	}

//	/**
//	 * Test of getSuperset method, of class Subset.
//	 */
//	@Test
//	public void testGetSuperset() {
//		System.out.println("getSuperset");
//		Subset instance = null;
//		Set expResult = null;
//		Set result = instance.getSuperset();
//		assertEquals(expResult, result);
//		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case is a prototype.");
//	}
//
//	/**
//	 * Test of getElements method, of class Subset.
//	 */
//	@Test
//	public void testGetElements() {
//		System.out.println("getElements");
//		Subset instance = null;
//		Element[] expResult = null;
//		Element[] result = instance.getElements();
//		assertArrayEquals(expResult, result);
//		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case is a prototype.");
//	}
//
//	/**
//	 * Test of iterator method, of class Subset.
//	 */
//	@Test
//	public void testIterator() {
//		System.out.println("iterator");
//		Subset instance = null;
//		Iterator<Element> expResult = null;
//		Iterator<Element> result = instance.iterator();
//		assertEquals(expResult, result);
//		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case is a prototype.");
//	}
//
//	/**
//	 * Test of standardIsEqual method, of class Subset.
//	 */
//	@Test
//	public void testStandardIsEqual() {
//		System.out.println("standardIsEqual");
//		Set set = null;
//		Subset instance = null;
//		boolean expResult = false;
//		boolean result = instance.standardIsEqual(set);
//		assertEquals(expResult, result);
//		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case is a prototype.");
//	}
//
//	/**
//	 * Test of standardContains method, of class Subset.
//	 */
//	@Test
//	public void testStandardContains() {
//		System.out.println("standardContains");
//		Element element = null;
//		Subset instance = null;
//		boolean expResult = false;
//		boolean result = instance.standardContains(element);
//		assertEquals(expResult, result);
//		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case is a prototype.");
//	}
//
//	/**
//	 * Test of abstractContains method, of class Subset.
//	 */
//	@Test
//	public void testAbstractContains() {
//		System.out.println("abstractContains");
//		BigInteger value = null;
//		Subset instance = null;
//		boolean expResult = false;
//		boolean result = instance.abstractContains(value);
//		assertEquals(expResult, result);
//		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case is a prototype.");
//	}
//
//	/**
//	 * Test of abstractGetElement method, of class Subset.
//	 */
//	@Test
//	public void testAbstractGetElement() {
//		System.out.println("abstractGetElement");
//		BigInteger value = null;
//		Subset instance = null;
//		Element expResult = null;
//		Element result = instance.abstractGetElement(value);
//		assertEquals(expResult, result);
//		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case is a prototype.");
//	}
//
//	/**
//	 * Test of abstractGetOrder method, of class Subset.
//	 */
//	@Test
//	public void testAbstractGetOrder() {
//		System.out.println("abstractGetOrder");
//		Subset instance = null;
//		BigInteger expResult = null;
//		BigInteger result = instance.abstractGetOrder();
//		assertEquals(expResult, result);
//		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case is a prototype.");
//	}
//
//	/**
//	 * Test of abstractGetRandomElement method, of class Subset.
//	 */
//	@Test
//	public void testAbstractGetRandomElement() {
//		System.out.println("abstractGetRandomElement");
//		Random random = null;
//		Subset instance = null;
//		Element expResult = null;
//		Element result = instance.abstractGetRandomElement(random);
//		assertEquals(expResult, result);
//		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case is a prototype.");
//	}
//
//	/**
//	 * Test of getInstance method, of class Subset.
//	 */
//	@Test
//	public void testGetInstance() {
//		System.out.println("getInstance");
//		Set superSet = null;
//		Element[] elements = null;
//		Subset expResult = null;
//		Subset result = Subset.getInstance(superSet, elements);
//		assertEquals(expResult, result);
//		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case is a prototype.");
//	}
}
