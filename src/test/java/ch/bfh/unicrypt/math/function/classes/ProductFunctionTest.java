/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.function.classes;

import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class ProductFunctionTest {

	/**
	 * Test of getInstance method, of class Tuple.
	 */
	@Test
	public void tupleTest() {
		Function f1 = IdentityFunction.getInstance(ZMod.getInstance(10));
		Function f2 = IdentityFunction.getInstance(ZMod.getInstance(12));
		Function f3 = IdentityFunction.getInstance(ZMod.getInstance(15));
		ProductFunction functions = ProductFunction.getInstance(f1, f2, f3);
		int index = 0;
		for (Function function : functions) {
			System.err.println(function);
			Assert.assertTrue(function.isEqual(functions.getAt(index++)));
		}
	}

}
