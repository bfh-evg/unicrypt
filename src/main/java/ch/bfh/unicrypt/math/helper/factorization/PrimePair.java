/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.helper.factorization;

import java.math.BigInteger;
import java.util.Random;

/**
 *
 * @author rolfhaenni
 */
public class PrimePair extends Factorization {

  protected PrimePair(Prime prime1, Prime prime2) {
    this(prime1.getValue(), prime2.getValue());
  }

  protected PrimePair(BigInteger prime1, BigInteger prime2) {
    super(prime1.multiply(prime2), new BigInteger[]{prime1, prime2}, new int[]{1, 1});
  }

  public BigInteger getFirstPrime() {
    return this.getPrimeFactors()[0];
  }

  public BigInteger getSecondPrime() {
    return this.getPrimeFactors()[1];
  }

  public static PrimePair getInstance(BigInteger prime1, BigInteger prime2) {
    if (prime1.equals(prime2)) {
      throw new IllegalArgumentException();
    }
    return new PrimePair(prime1, prime2);
  }

  public static PrimePair getInstance(Prime prime1, Prime prime2) {
    if (prime1.equals(prime2)) {
      throw new IllegalArgumentException();
    }
    return new PrimePair(prime1, prime2);
  }

  public static PrimePair getRandomInstance(int bitLength) {
    return PrimePair.getRandomInstance(bitLength, (Random) null);
  }

  public static PrimePair getRandomInstance(int bitLength, Random random) {
    return PrimePair.getInstance(Prime.getRandomInstance(bitLength, random), Prime.getRandomInstance(bitLength, random));
  }

}
