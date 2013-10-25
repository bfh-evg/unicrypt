package ch.bfh.unicrypt.math.utility;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

/**
 * This is a helper class with some static methods for various functions for
 * generating random values. It contains a static pseudo random number generator
 * (PNRG) object, which may serve as a default random generator in a large
 * project. The initial seeding of this PRNG might be slow due to system-wide
 * entropy gathering.
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 1.0
 */
public final class RandomUtil {

  /**
   * The default pseudo random number generator
   */
  private static SecureRandom defaultRandomNumberGenerator;

  static {
    RandomUtil.defaultRandomNumberGenerator = RandomUtil.getRandomNumberGenerator();
    RandomUtil.defaultRandomNumberGenerator.nextBoolean(); // initiates the entropy gathering
  }

  /**
   * Creates a new pseudo random number generator.
   *
   * @return The new random generator
   */
  public static SecureRandom getRandomNumberGenerator() {
    SecureRandom randomNumberGenerator;
    try {
      randomNumberGenerator = SecureRandom.getInstance("SHA1PRNG");
    } catch (final NoSuchAlgorithmException e) {
      randomNumberGenerator = new SecureRandom();
    }
    return randomNumberGenerator;
  }

  public static SecureRandom getRandomNumberGenerator(byte[] seed) {
    SecureRandom randomNumberGenerator = RandomUtil.getRandomNumberGenerator();
    randomNumberGenerator.setSeed(seed);
    return randomNumberGenerator;
  }

  public static SecureRandom getRandomNumberGenerator(long seed) {
    SecureRandom randomNumberGenerator = RandomUtil.getRandomNumberGenerator();
    randomNumberGenerator.setSeed(seed);
    return randomNumberGenerator;
  }

  /**
   * This is a helper method to switch between the default pseudo random number
   * generator and some random generator given as an optional argument. This
   * method is called by several of this class' public methods.
   *
   * @param random Either {@code null} or a given random generator
   * @return {@code random}, if
   * {@code random != null}, {@code defaultRandomGenerator} otherwise
   */
  private static Random getRandomNumberGenerator(final Random random) {
    if (random == null) {
      return RandomUtil.defaultRandomNumberGenerator;
    }
    return random;
  }

  /**
   * Uses the default random generator to create a random boolean value
   *
   * @return The random boolean value
   */
  public static boolean getRandomBoolean() {
    return RandomUtil.getRandomBoolean((Random) null);
  }

  /**
   * Uses a given random generator to create a random boolean value
   *
   * @param random The given random generator
   * @return The random boolean value
   */
  public static boolean getRandomBoolean(final Random random) {
    return RandomUtil.getRandomNumberGenerator(random).nextBoolean();
  }

  /**
   * Uses the default random generator to create a random integer between 0 and
   * {@code maxValue} (inclusive)
   *
   * @param maxValue The maximal value
   * @return The random integer
   * @throws IllegalArgumentException if {@code maxValue < 0}
   */
  public static int getRandomInteger(final int maxValue) {
    return RandomUtil.getRandomInteger(maxValue, (Random) null);
  }

  /**
   * Uses the default random generator to create a random integer between
   * {@code minValue} (inclusive) and {@code maxValue} (inclusive)
   *
   * @param minValue The minimal value
   * @param maxValue The maximal value
   * @return The random integer
   * @throws IllegalArgumentException if {@code maxValue < minValue}
   */
  public static int getRandomInteger(final int minValue, final int maxValue) {
    return RandomUtil.getRandomInteger(minValue, maxValue, (Random) null);
  }

  /**
   * Uses a given random generator to create a random integer between
   * {@code minValue} (inclusive) and {@code maxValue} (inclusive)
   *
   * @param minValue The minimal value
   * @param maxValue The maximal value
   * @param random The given random generator
   * @return The random integer
   * @throws IllegalArgumentException if {@code maxValue < minValue}
   */
  public static int getRandomInteger(final int minValue, final int maxValue, final Random random) {
    return RandomUtil.getRandomInteger(maxValue - minValue, random) + minValue;
  }

  /**
   * Uses a given random generator to create a random integer between 0 and
   * {@code maxValue} (inclusive)
   *
   * @param maxValue The maximal value
   * @param random The given random generator
   * @return The random integer
   * @throws IllegalArgumentException if {@code maxValue < 0}
   */
  public static int getRandomInteger(final int maxValue, final Random random) {
    if (maxValue < 0) {
      throw new IllegalArgumentException();
    }
    return RandomUtil.getRandomNumberGenerator(random).nextInt(maxValue + 1);
  }

  /**
   * Uses the default random generator to create a random BigInteger value of a
   * certain bit length
   *
   * @param bitLength The given bit length
   * @return The random BigInteger value
   * @throws IllegalArgumentException if {@code bitLength < 0}
   */
  public static BigInteger getRandomBigInteger(final int bitLength) {
    return RandomUtil.getRandomBigInteger(bitLength, (Random) null);
  }

  /**
   * Uses a given random generator to create a random BigInteger value of a
   * certain bit length
   *
   * @param bitLength The given bit length
   * @param random The given random generator
   * @return The random BigInteger value
   * @throws IllegalArgumentException if {@code bitLength < 0}
   */
  public static BigInteger getRandomBigInteger(final int bitLength, final Random random) {
    if (bitLength < 0) {
      throw new IllegalArgumentException();
    }
    if (bitLength == 0) {
      return BigInteger.ZERO;
    }
    return new BigInteger(bitLength - 1, RandomUtil.getRandomNumberGenerator(random)).add(BigInteger.valueOf(2).pow(bitLength - 1));
  }

  /**
   * Uses the default random generator to create a random BigInteger value
   * between 0 and {@code maxValue} (inclusive)
   *
   * @param maxValue The maximal value
   * @return The random BigInteger value
   * @throws IllegalArgumentException if {@code maxValue} is null or if {@code maxValue < 0}
   */
  public static BigInteger getRandomBigInteger(final BigInteger maxValue) {
    return RandomUtil.getRandomBigInteger(maxValue, (Random) null);
  }

  /**
   * Uses a given random generator to create a random BigInteger between 0 and
   * {@code maxValue} (inclusive)
   *
   * @param maxValue The maximal value
   * @param random The given random generator
   * @return The random BigInteger value
   * @throws IllegalArgumentException if {@code maxValue} is null or if {@code maxValue < 0}
   */
  public static BigInteger getRandomBigInteger(final BigInteger maxValue, final Random random) {
    if (maxValue == null || maxValue.signum() < 0) {
      throw new IllegalArgumentException();
    }
    BigInteger randomValue;
    int bitLength = maxValue.bitLength();
    do {
      randomValue = new BigInteger(bitLength, RandomUtil.getRandomNumberGenerator(random));
    } while (randomValue.compareTo(maxValue) > 0);
    return randomValue;
  }

  /**
   * Uses the default random generator to create a random BigInteger value
   * between {@code minValue} (inclusive) and {@code maxValue} (inclusive)
   *
   * @param minValue The minimal value
   * @param maxValue The maximal value
   * @return The random BigInteger value
   * @throws IllegalArgumentException if {@code minValue} or {@code maxValue} is
   * null, or if {@code maxValue < minValue}
   */
  public static BigInteger getRandomBigInteger(final BigInteger minValue, final BigInteger maxValue) {
    return RandomUtil.getRandomBigInteger(minValue, maxValue, (Random) null);
  }

  /**
   * Uses a given random generator to create a random BigInteger value between
   * {@code minValue} (inclusive) and {@code maxValue} (inclusive)
   *
   * @param minValue The minimal value
   * @param maxValue The maximal value
   * @param random The given random generator
   * @return The random BigInteger value
   * @throws IllegalArgumentException if {@code minValue} or {@code maxValue} is
   * null, or if {@code maxValue < minValue}
   */
  public static BigInteger getRandomBigInteger(final BigInteger minValue, final BigInteger maxValue, final Random random) {
    if (minValue == null || maxValue == null) {
      throw new IllegalArgumentException();
    }
    return RandomUtil.getRandomBigInteger(maxValue.subtract(minValue), random).add(minValue);
  }

  /**
   * Uses the default random generator to create a random BigInteger value of a
   * certain bit length that is probably prime with high certainty
   *
   * @param bitLength The given bit length
   * @return The random BigInteger prime number
   * @throws IllegalArgumentException if {@code bitLength < 2}
   */
  public static BigInteger getRandomPrime(final int bitLength) {
    return RandomUtil.getRandomPrime(bitLength, (Random) null);
  }

  /**
   * Uses a given random generator to create a random BigInteger value of a
   * certain bit length that is probably prime with high certainty
   *
   * @param bitLength The given bit length
   * @param random The given random generator
   * @return The random BigInteger prime number
   * @throws IllegalArgumentException if {@code bitLength < 2}
   */
  public static BigInteger getRandomPrime(final int bitLength, final Random random) {
    if (bitLength < 2) {
      throw new IllegalArgumentException();
    }
    return new BigInteger(bitLength, MathUtil.NUMBER_OF_PRIME_TESTS, RandomUtil.getRandomNumberGenerator(random));
  }

  /**
   * Uses the default random generator to create a random BigInteger value of a
   * certain bit length that is a save prime with high certainty
   *
   * @param bitLength The given bit length
   * @return The random BigInteger save prime number
   * @throws IllegalArgumentException if {@code bitLength < 3}
   */
  public static BigInteger getRandomSavePrime(final int bitLength) {
    return RandomUtil.getRandomSavePrime(bitLength, (Random) null);
  }

  /**
   * Uses a given random generator to create a random BigInteger value of a
   * certain bit length that is a save prime with high certainty
   *
   * @param bitLength The given bit length
   * @param random The given random generator
   * @return The random BigInteger save prime
   * @throws IllegalArgumentException if {@code bitLength < 3}
   * @
   * see "Handbook of Applied Cryptography, Algorithm 4.86"
   */
  public static BigInteger getRandomSavePrime(final int bitLength, final Random random) {
    BigInteger prime;
    BigInteger savePrime;
    do {
      prime = RandomUtil.getRandomPrime(bitLength - 1, random);
      savePrime = prime.shiftLeft(1).add(BigInteger.ONE);
    } while (!MathUtil.isPrime(savePrime));
    return savePrime;
  }

  /**
   * Uses the default random generator to create a pair of distinct random
   * BigInteger values of respective bit lengths such that both values are
   * probably prime with high certainty and such that the second divides the
   * first minus one.
   *
   * @param bitLength1 The bit length of the first random prime
   * @param bitLength2 The bit length of the second random prime
   * @throws IllegalArgumentException if {@code bitLength1 <= bitLength2} or {@code bitLengh2<2}
   */
  public static BigInteger[] getRandomPrimePair(final int bitLength1, final int bitLength2) {
    return RandomUtil.getRandomPrimePair(bitLength1, bitLength2, (Random) null);
  }

  /**
   * Uses a given random generator to create a pair of distinct random
   * BigInteger values of respective bit lengths such that both values are
   * probably prime with high certainty and such that the second divides the
   * first minus one.
   *
   * @param bitLength1 The bit length of the first random prime
   * @param bitLength2 The bit length of the second random prime
   * @param random The given random generator
   * @throws IllegalArgumentException if {@code bitLength1 <= bitLength2} or {@code bitLengh2<2}
   */
  public static BigInteger[] getRandomPrimePair(final int bitLength1, final int bitLength2, final Random random) {
    if (bitLength1 <= bitLength2 || bitLength2 < 2) {
      throw new IllegalArgumentException();
    }
    BigInteger k;
    BigInteger prime1, prime2;
    BigInteger minValue, maxValue;
    do {
      prime2 = RandomUtil.getRandomPrime(bitLength2, random);
      minValue = BigInteger.ONE.shiftLeft(bitLength1 - 1);
      maxValue = BigInteger.ONE.shiftLeft(bitLength1).subtract(BigInteger.ONE);
      k = RandomUtil.getRandomBigInteger(minValue.divide(prime2).add(BigInteger.ONE), maxValue.divide(prime2), random);
      prime1 = prime2.multiply(k).add(BigInteger.ONE);
    } while (!MathUtil.isPrime(prime1));
    return new BigInteger[]{prime1, prime2};
  }

}
