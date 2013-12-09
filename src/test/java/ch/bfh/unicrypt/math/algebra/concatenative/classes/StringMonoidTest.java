/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.concatenative.classes;

import ch.bfh.unicrypt.math.helper.Alphabet;
import java.math.BigInteger;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class StringMonoidTest {

	@Test
	public void testGetValue() {
		StringMonoid sm = StringMonoid.getInstance(Alphabet.BINARY, 4);
		for (BigInteger i = BigInteger.valueOf(0); i.compareTo(BigInteger.valueOf(300)) <= 0; i = i.add(BigInteger.ONE)) {
			StringElement element = sm.getElement(i);
			Assert.assertEquals(element.getValue(), i);
//			System.out.println(element);
		}
	}

}
