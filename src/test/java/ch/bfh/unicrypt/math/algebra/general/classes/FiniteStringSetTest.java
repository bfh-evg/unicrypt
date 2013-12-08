/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.general.classes;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.helper.Alphabet;
import org.junit.Test;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class FiniteStringSetTest {

	@Test
	public void testIteration() {
		FiniteStringSet set = FiniteStringSet.getInstance(Alphabet.OCTAL, 2, true);
		for (Element element : set) {
			System.out.println(element);
		}
		System.out.println(set.getOrder());
	}

}
