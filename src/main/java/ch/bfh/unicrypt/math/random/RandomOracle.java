/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.random;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author rolfhaenni
 */
public class RandomOracle {

  private static final String DEFAULT_ALGORITHM_NAME = "SHA1PRNG";
  public static final RandomOracle DEFAULT = RandomOracle.getInstance(DEFAULT_ALGORITHM_NAME);

  private final String algorithmName;

  protected RandomOracle(String algorithmName) {
    this.algorithmName = algorithmName;
  }

  public SecureRandom getSecureRandom(long i) {
    if (i < 0) {
      throw new IllegalArgumentException();
    }
    try {
      SecureRandom secureRandom = SecureRandom.getInstance(this.getAlgorithmName());
      secureRandom.setSeed(i + 1); // to avoid 0 to be ignored
      return secureRandom;
    } catch (final NoSuchAlgorithmException e) {
      throw new InternalError();
    }
  }

  public String getAlgorithmName() {
    return this.algorithmName;
  }

  public static RandomOracle getInstance() {
    return RandomOracle.DEFAULT;
  }

  public static RandomOracle getInstance(String algorithmName) {
    return new RandomOracle(algorithmName);
  }

}
