/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.random.classes;

import ch.bfh.unicrypt.crypto.random.interfaces.RandomReferenceString;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.ByteArrayMonoid;
import java.math.BigInteger;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class PseudoRandomReferenceStringTest {

	@Test
	public void generalTest() {
		RandomReferenceString rrs = PseudoRandomReferenceString.getInstance();
		BigInteger prime = rrs.nextPrime(10);
		rrs.nextBoolean();
		rrs.nextBytes(5);
		rrs.reset();
		Assert.assertEquals(prime, rrs.nextPrime(10));
		rrs = PseudoRandomReferenceString.getInstance(ByteArrayMonoid.getInstance().getElement(new byte[]{10, 5, 120}));
		prime = rrs.nextPrime(10);
		rrs.nextBoolean();
		rrs.nextBytes(5);
		rrs.reset();
		Assert.assertEquals(prime, rrs.nextPrime(10));
	}

}
