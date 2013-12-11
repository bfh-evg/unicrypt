/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.general.classes;

import ch.bfh.unicrypt.math.helper.Alphabet;
import java.math.BigInteger;
import org.junit.Test;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class FixedStringSetTest {

//	@Test
//	public void testIteration() {
//		FiniteStringSet set = FiniteStringSet.getInstance(Alphabet.OCTAL, 2, true);
//		for (Element element : set) {
//			System.out.println(element);
//		}
//		System.out.println(set.getOrder());
//	}
	@Test
	public void testGetValue() {
		FixedStringSet set = FixedStringSet.getInstance(Alphabet.BINARY, 4);
		System.out.println(set.getElement("0000").getValue());
		System.out.println(set.getElement("0001").getValue());
		System.out.println(set.getElement("0010").getValue());
		System.out.println(set.getElement("0011").getValue());
		System.out.println(set.getElement("0100").getValue());
		System.out.println(set.getElement("0101").getValue());
		System.out.println(set.getElement("0110").getValue());
		System.out.println(set.getElement("0111").getValue());
		System.out.println(set.getElement("1111").getValue());
		for (BigInteger i = BigInteger.ZERO; i.compareTo(BigInteger.valueOf(15)) <= 0; i = i.add(BigInteger.ONE)) {
			System.out.println(set.getElement(i));
		}
		System.out.println(set.getOrder());
		System.out.println(FixedStringSet.getInstance(Alphabet.BINARY, BigInteger.valueOf(54)).getOrder());
	}

}
