/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.concatenative.classes;

import java.math.BigInteger;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class ByteArrayMonoidTest {

	@Test
	public void testGetValue() {
		ByteArrayMonoid bam = ByteArrayMonoid.getInstance(2);
		for (BigInteger i = BigInteger.valueOf(0); i.compareTo(BigInteger.valueOf(65538)) <= 0; i = i.add(BigInteger.ONE)) {
			ByteArrayElement element = bam.getElement(i);
			Assert.assertEquals(i, element.getValue());
//			System.out.println(element);
		}
	}

}
