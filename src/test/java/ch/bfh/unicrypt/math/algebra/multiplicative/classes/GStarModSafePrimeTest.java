/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.multiplicative.classes;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
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
			System.out.println(element);
		}
	}

}
