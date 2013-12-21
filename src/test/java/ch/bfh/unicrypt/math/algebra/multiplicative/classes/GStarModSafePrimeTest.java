/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.multiplicative.classes;

import ch.bfh.unicrypt.crypto.random.classes.PseudoRandomReferenceString;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomReferenceString;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class GStarModSafePrimeTest {

	@Test
	public void testIteration() {
		GStarModSafePrime set = GStarModSafePrime.getInstance(23);
		for (Element element : set) {
			// System.out.println(element);
		}
	}

	@Test
	public void testGetIndependentGenerators1() {
		RandomReferenceString rrs = PseudoRandomReferenceString.getInstance();
		GStarModSafePrime set = GStarModSafePrime.getInstance(23);
		Element g1 = set.getIndependentGenerator(3, rrs);
		Element g2 = set.getIndependentGenerator(5, rrs);
		Element g3 = set.getIndependentGenerator(2, rrs);
		Element[] gs1 = set.getIndependentGenerators(20, rrs);
		Assert.assertTrue(gs1[3].isEquivalent(g1));
		Assert.assertTrue(gs1[5].isEquivalent(g2));
		Assert.assertTrue(gs1[2].isEquivalent(g3));
		Element[] gs2 = set.getIndependentGenerators(2, 10, rrs);
		Assert.assertTrue(gs2[1].isEquivalent(g1));
		Assert.assertTrue(gs2[3].isEquivalent(g2));
		Assert.assertTrue(gs2[0].isEquivalent(g3));
//		System.out.println(g1);
//		System.out.println(g2);
//		System.out.println(g3);
//		System.out.println("Generators:");
//		for (int i = 0; i < gs1.length; i++) {
//			System.out.println(gs1[i]);
//		}
	}

	@Test
	public void testGetIndependentGenerators2() {
		RandomReferenceString rrs = PseudoRandomReferenceString.getInstance(new byte[]{2, 5});
		GStarModSafePrime set = GStarModSafePrime.getInstance(23);
		Element g1 = set.getIndependentGenerator(3, rrs);
		Element g2 = set.getIndependentGenerator(5, rrs);
		Element g3 = set.getIndependentGenerator(2, rrs);
		Element[] gs1 = set.getIndependentGenerators(20, rrs);
		Assert.assertTrue(gs1[3].isEquivalent(g1));
		Assert.assertTrue(gs1[5].isEquivalent(g2));
		Assert.assertTrue(gs1[2].isEquivalent(g3));
		Element[] gs2 = set.getIndependentGenerators(2, 10, rrs);
		Assert.assertTrue(gs2[1].isEquivalent(g1));
		Assert.assertTrue(gs2[3].isEquivalent(g2));
		Assert.assertTrue(gs2[0].isEquivalent(g3));
	}

}
