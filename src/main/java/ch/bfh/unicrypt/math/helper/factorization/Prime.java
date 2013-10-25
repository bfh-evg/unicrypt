/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.helper.factorization;

import java.math.BigInteger;
import java.util.Random;

import ch.bfh.unicrypt.math.utility.RandomUtil;

/**
 *
 * @author rolfhaenni
 */
public class Prime extends SpecialFactorization {

  protected Prime(BigInteger prime) {
    super(prime, new BigInteger[]{prime}, new int[]{1});
  }

  public static Prime getInstance(int prime) {
    return Prime.getInstance(BigInteger.valueOf(prime));
  }

  public static Prime getInstance(BigInteger prime) {
    return new Prime(prime);
  }

  public static Prime getRandomInstance(int bitLength) {
    return Prime.getRandomInstance(bitLength, (Random) null);
  }

  public static Prime getRandomInstance(int bitLength, Random random) {
    return Prime.getInstance(RandomUtil.getRandomPrime(bitLength, random));
  }

}
