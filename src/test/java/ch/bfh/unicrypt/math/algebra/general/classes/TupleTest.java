/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.general.classes;

import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class TupleTest {

	@Test
	public void tupleTest() {
		ZMod zMod = ZMod.getInstance(17);
		Element e1 = zMod.getElement(4);
		Element e2 = zMod.getElement(2);
		Element e3 = zMod.getElement(1);
		Tuple tuple = Tuple.getInstance(e1, e2, e3);
		int index = 0;
		for (Element element : tuple) {
			Assert.assertTrue(element.isEqual(tuple.getAt(index++)));
		}
	}

}
