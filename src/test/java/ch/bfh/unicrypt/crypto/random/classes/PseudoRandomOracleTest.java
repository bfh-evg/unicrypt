/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.random.classes;

import ch.bfh.unicrypt.crypto.random.interfaces.RandomOracle;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomReferenceString;
import java.math.BigInteger;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class PseudoRandomOracleTest {

	@Test
	public void generalTest() {
		RandomOracle ro = PseudoRandomOracle.getInstance();
		RandomReferenceString rrs = ro.getRandomReferenceString(0);
		BigInteger prime = rrs.nextPrime(10);
		rrs.nextBoolean();
		rrs.nextBytes(5);
		rrs.reset();
		Assert.assertEquals(prime, rrs.nextPrime(10));
		rrs = ro.getRandomReferenceString(1);
		prime = rrs.nextPrime(10);
		rrs.nextBoolean();
		rrs.nextBytes(5);
		rrs.reset();
		Assert.assertEquals(prime, rrs.nextPrime(10));
	}

}
