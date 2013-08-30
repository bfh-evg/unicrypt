/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.helper.factorization;

import ch.bfh.unicrypt.math.utility.MathUtil;
import ch.bfh.unicrypt.math.utility.RandomUtil;
import java.math.BigInteger;
import java.util.Random;

/**
 *
 * @author rolfhaenni
 */
public class SafePrime extends Prime {

  protected SafePrime(BigInteger safePrime) {
    super(safePrime);
  }

  public static SafePrime getInstance(BigInteger safePrime) {
    if (!MathUtil.isSavePrime(safePrime)) {
      throw new IllegalArgumentException();
    }
    return new SafePrime(safePrime);
  }

  public static SafePrime getRandomInstance(int bitLength) {
    return SafePrime.getRandomInstance(bitLength, (Random) null);
  }

  public static SafePrime getRandomInstance(int bitLength, Random random) {
    return new SafePrime(RandomUtil.createRandomSavePrime(bitLength, random));
  }

}
