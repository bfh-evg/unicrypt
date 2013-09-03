/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.multiplicative.classes;

import java.math.BigInteger;
import java.util.Random;

import ch.bfh.unicrypt.math.helper.factorization.Prime;

/**
 *
 * @author rolfhaenni
 */
public class ZTimesModPrime extends ZTimesMod {

  protected ZTimesModPrime(Prime prime) {
    super(prime.getValue());
  }

  public static ZTimesModPrime getInstance(final Prime prime) {
    if (prime == null) {
      throw new IllegalArgumentException();
    }
    return new ZTimesModPrime(prime);
  }

  public static ZTimesModPrime getInstance(BigInteger prime) {
    return new ZTimesModPrime(Prime.getInstance(prime));
  }

  public static ZTimesModPrime getRandomInstance(int bitLength, Random random) {
    return new ZTimesModPrime(Prime.getRandomInstance(bitLength, random));
  }

  public static ZTimesModPrime getRandomInstance(int bitLength) {
    return ZTimesModPrime.getRandomInstance(bitLength, (Random) null);
  }

}
