/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.general.classes;

import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;
import org.junit.Test;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class ProductGroupTest {

	@Test
	public void testIteration() {
		CyclicGroup g1 = GStarModSafePrime.getInstance(11);
		CyclicGroup g2 = GStarModSafePrime.getInstance(23);
		CyclicGroup g3 = GStarModSafePrime.getInstance(5);
		ProductCyclicGroup pg = ProductCyclicGroup.getInstance(g1, g2, g3);
		for (CyclicGroup group : pg.makeIterable()) {
			System.out.println(group.getDefaultGenerator());
		}
		for (Tuple tuple : pg) {
			System.out.println(tuple);
		}

	}

}
