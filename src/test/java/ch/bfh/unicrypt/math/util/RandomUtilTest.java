package ch.bfh.unicrypt.math.util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

import junit.framework.Assert;

import org.junit.Test;

import ch.bfh.unicrypt.math.utility.MathUtil;
import ch.bfh.unicrypt.math.utility.RandomUtil;

@SuppressWarnings("static-method")
public class RandomUtilTest {

  public static Random[] randoms = new Random[] {null, new Random(), RandomUtil.getRandomNumberGenerator()};

  @Test
  public void testCreateRandomGenerator() {
    Random prng1 = RandomUtil.getRandomNumberGenerator();
    Random prng2 = RandomUtil.getRandomNumberGenerator();
    Assert.assertNotSame(prng1, prng2);
    Assert.assertFalse(RandomUtil.getRandomBigInteger(100, prng1).equals(RandomUtil.getRandomBigInteger(100, prng2)));
  }

  @Test
  public void testCreateRandomGeneratorByteArray() {
    Random prng1 = RandomUtil.getRandomNumberGenerator(new byte[]{(byte)123, (byte)231});
    Random prng2 = RandomUtil.getRandomNumberGenerator(new byte[]{(byte)123, (byte)231});
    Random prng3 = RandomUtil.getRandomNumberGenerator(new byte[]{(byte)231, (byte)123});
    Assert.assertEquals(RandomUtil.getRandomBigInteger(1, prng1), RandomUtil.getRandomBigInteger(1, prng2));
    Assert.assertEquals(RandomUtil.getRandomBigInteger(10, prng1), RandomUtil.getRandomBigInteger(10, prng2));
    Assert.assertEquals(RandomUtil.getRandomBigInteger(100, prng1), RandomUtil.getRandomBigInteger(100, prng2));
    Assert.assertTrue(RandomUtil.getRandomBigInteger(100, prng1).equals(RandomUtil.getRandomBigInteger(100, prng2)));
    Assert.assertFalse(RandomUtil.getRandomBigInteger(100, prng1).equals(RandomUtil.getRandomBigInteger(100, prng3)));
    Assert.assertFalse(RandomUtil.getRandomBigInteger(100, prng2).equals(RandomUtil.getRandomBigInteger(100, prng3)));
  }

  @Test
  public void testCreateRandomBoolean() {
    boolean r;
    do {
      r = RandomUtil.getRandomBoolean();
    } while (r);
    do {
      r = RandomUtil.getRandomBoolean();
    } while (!r);
  }

  @Test
  public void testCreateRandomBooleanRandom() {
    boolean r;
    for (Random random : randoms) {
      do {
        r = RandomUtil.getRandomBoolean(random);
      } while (r);
      do {
        r = RandomUtil.getRandomBoolean(random);
      } while (!r);
    }
  }

  @Test
  public void testCreateRandomIntInt() {
    Assert.assertEquals(RandomUtil.getRandomInteger(0), 0);
    int maxValue = 10;
    int r;
    for (int i=0; i<=maxValue; i++) {
      do {
        r = RandomUtil.getRandomInteger(maxValue);
        Assert.assertTrue(r >= 0);
        Assert.assertTrue(r <= maxValue);
      } while (r != i);
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testCreateRandomIntIntException() {
    RandomUtil.getRandomInteger(-1);
  }

  @Test
  public void testCreateRandomIntIntInt() {
    Assert.assertEquals(RandomUtil.getRandomInteger(5, 5), 5);
    Assert.assertEquals(RandomUtil.getRandomInteger(-5, -5), -5);
    int minValue = -10;
    int maxValue = 10;
    int r;
    for (int i=minValue; i<=maxValue; i++) {
      do {
        r = RandomUtil.getRandomInteger(minValue, maxValue);
        Assert.assertTrue(r >= minValue);
        Assert.assertTrue(r <= maxValue);
      } while (r != i);
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testCreateRandomIntIntIntException() {
    RandomUtil.getRandomInteger(5, 3);
  }

  @Test
  public void testCreateRandomIntIntIntRandom() {
    for (Random random : randoms) {
      Assert.assertEquals(RandomUtil.getRandomInteger(5, 5, random), 5);
      Assert.assertEquals(RandomUtil.getRandomInteger(-5, -5, random), -5);
      int minValue = -10;
      int maxValue = 10;
      int r;
      for (int i=minValue; i<=maxValue; i++) {
        do {
          r = RandomUtil.getRandomInteger(minValue, maxValue, random);
          Assert.assertTrue(r >= minValue);
          Assert.assertTrue(r <= maxValue);
        } while (r != i);
      }
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testCreateRandomIntIntIntRandomException() {
    RandomUtil.getRandomInteger(5, 3, new Random());
  }

  @Test
  public void testCreateRandomIntIntRandom() {
    for (Random random : randoms) {
      Assert.assertEquals(RandomUtil.getRandomInteger(0, random), 0);
      int maxValue = 10;
      int r;
      for (int i=0; i<=maxValue; i++) {
        do {
          r = RandomUtil.getRandomInteger(maxValue, random);
          Assert.assertTrue(r >= 0);
          Assert.assertTrue(r <= maxValue);
        } while (r != i);
      }
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testCreateRandomIntIntRandomException() {
    RandomUtil.getRandomInteger(-1, new Random());
  }

  @Test
  public void testCreateRandomBigIntegerInt() {
    Assert.assertEquals(RandomUtil.getRandomBigInteger(0), BigInteger.ZERO);
    int maxLength = 5; // Test the 2^5 = 32 values from 0...31
    BigInteger maxValue = BigInteger.valueOf(2).pow(maxLength).subtract(BigInteger.ONE);
    BigInteger r;
    for (BigInteger i=BigInteger.ZERO; i.compareTo(maxValue)<=0; i=i.add(BigInteger.ONE)) {
      do {
        r = RandomUtil.getRandomBigInteger(maxLength);
        Assert.assertTrue(r.signum() >= 0);
        Assert.assertTrue(r.compareTo(maxValue) <= 0);
      } while (!r.equals(i));
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testCreateRandomBigIntegerIntException() {
    RandomUtil.getRandomBigInteger(-1);
  }

  @Test
  public void testCreateRandomBigIntegerIntRandom() {
    for (Random random : randoms) {
      Assert.assertEquals(RandomUtil.getRandomBigInteger(0, random), BigInteger.ZERO);
      int maxLength = 5; // Test the 2^5 = 32 values from 0...31
      BigInteger maxValue = BigInteger.valueOf(2).pow(maxLength).subtract(BigInteger.ONE);
      BigInteger r;
      for (BigInteger i=BigInteger.ZERO; i.compareTo(maxValue)<=0; i=i.add(BigInteger.ONE)) {
        do {
          r = RandomUtil.getRandomBigInteger(maxLength, random);
          Assert.assertTrue(r.signum() >= 0);
          Assert.assertTrue(r.compareTo(maxValue) <= 0);
        } while (!r.equals(i));
      }
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testCreateRandomBigIntegerIntRandomException() {
    RandomUtil.getRandomBigInteger(-1, new Random());
  }

  @Test
  public void testCreateRandomBigIntegerBigInteger() {
    Assert.assertEquals(RandomUtil.getRandomBigInteger(BigInteger.ZERO), BigInteger.ZERO);
    BigInteger maxValue = BigInteger.valueOf(10);
    BigInteger r;
    for (BigInteger i=BigInteger.ZERO; i.compareTo(maxValue)<=0; i=i.add(BigInteger.ONE)) {
      do {
        r = RandomUtil.getRandomBigInteger(maxValue);
        Assert.assertTrue(r.signum() >= 0);
        Assert.assertTrue(r.compareTo(maxValue) <= 0);
      } while (!r.equals(i));
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testCreateRandomBigIntegerBigIntegerException1() {
    RandomUtil.getRandomBigInteger((BigInteger)null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testCreateRandomBigIntegerBigIntegerException2() {
    RandomUtil.getRandomBigInteger(BigInteger.valueOf(-1));
  }

  @Test
  public void testCreateRandomBigIntegerBigIntegerRandom() {
    for (Random random : randoms) {
      Assert.assertEquals(RandomUtil.getRandomBigInteger(BigInteger.ZERO, random), BigInteger.ZERO);
      BigInteger maxValue = BigInteger.valueOf(10);
      BigInteger r;
      for (BigInteger i=BigInteger.ZERO; i.compareTo(maxValue)<=0; i=i.add(BigInteger.ONE)) {
        do {
          r = RandomUtil.getRandomBigInteger(maxValue, random);
          Assert.assertTrue(r.signum() >= 0);
          Assert.assertTrue(r.compareTo(maxValue) <= 0);
        } while (!r.equals(i));
      }
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testCreateRandomBigIntegerBigIntegerRandomException1() {
    RandomUtil.getRandomBigInteger((BigInteger)null, new Random());
  }

  @Test (expected=IllegalArgumentException.class)
  public void testCreateRandomBigIntegerBigIntegerRandomException2() {
    RandomUtil.getRandomBigInteger(BigInteger.valueOf(-1), new Random());
  }

  @Test
  public void testCreateRandomBigIntegerBigIntegerBigInteger() {
    Assert.assertEquals(RandomUtil.getRandomBigInteger(BigInteger.TEN, BigInteger.TEN), BigInteger.TEN);
    BigInteger minValue = BigInteger.valueOf(5);
    BigInteger maxValue = BigInteger.valueOf(15);
    BigInteger r;
    for (BigInteger i=minValue; i.compareTo(maxValue)<=0; i=i.add(BigInteger.ONE)) {
      do {
        r = RandomUtil.getRandomBigInteger(minValue, maxValue);
        Assert.assertTrue(r.compareTo(minValue) >= 0);
        Assert.assertTrue(r.compareTo(maxValue) <= 0);
      } while (!r.equals(i));
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testCreateRandomBigIntegerBigIntegerBigIntegerException1() {
    RandomUtil.getRandomBigInteger((BigInteger)null, (BigInteger)null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testCreateRandomBigIntegerBigIntegerBigIntegerException2() {
    RandomUtil.getRandomBigInteger((BigInteger)null, BigInteger.TEN);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testCreateRandomBigIntegerBigIntegerBigIntegerException3() {
    RandomUtil.getRandomBigInteger(BigInteger.TEN, (BigInteger)null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testCreateRandomBigIntegerBigIntegerBigIntegerException4() {
    RandomUtil.getRandomBigInteger(BigInteger.TEN, BigInteger.ONE);
  }

  @Test
  public void testCreateRandomBigIntegerBigIntegerBigIntegerRandom() {
    for (Random random : randoms) {
      Assert.assertEquals(RandomUtil.getRandomBigInteger(BigInteger.TEN, BigInteger.TEN, random), BigInteger.TEN);
      BigInteger minValue = BigInteger.valueOf(5);
      BigInteger maxValue = BigInteger.valueOf(15);
      BigInteger r;
      for (BigInteger i=minValue; i.compareTo(maxValue)<=0; i=i.add(BigInteger.ONE)) {
        do {
          r = RandomUtil.getRandomBigInteger(minValue, maxValue, random);
          Assert.assertTrue(r.compareTo(minValue) >= 0);
          Assert.assertTrue(r.compareTo(maxValue) <= 0);
        } while (!r.equals(i));
      }
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testCreateRandomBigIntegerBigIntegerBigIntegerRandomException1() {
    RandomUtil.getRandomBigInteger((BigInteger)null, (BigInteger)null, new Random());
  }

  @Test (expected=IllegalArgumentException.class)
  public void testCreateRandomBigIntegerBigIntegerBigIntegerRandomException2() {
    RandomUtil.getRandomBigInteger((BigInteger)null, BigInteger.TEN, new Random());
  }

  @Test (expected=IllegalArgumentException.class)
  public void testCreateRandomBigIntegerBigIntegerBigIntegerRandomException3() {
    RandomUtil.getRandomBigInteger(BigInteger.TEN, (BigInteger)null, new Random());
  }

  @Test (expected=IllegalArgumentException.class)
  public void testCreateRandomBigIntegerBigIntegerBigIntegerRandomException4() {
    RandomUtil.getRandomBigInteger(BigInteger.TEN, BigInteger.ONE, new Random());
  }

  @Test
  public void testCreateRandomPrimeInt() {
    for (int maxLength = 2; maxLength <= 6; maxLength++) {
      BigInteger minValue = BigInteger.valueOf(2).pow(maxLength-1);
      BigInteger maxValue = BigInteger.valueOf(2).pow(maxLength).subtract(BigInteger.ONE);
      BigInteger r;
      for (BigInteger i=minValue; i.compareTo(maxValue)<=0; i=i.add(BigInteger.ONE)) {
        if (MathUtil.isPrime(i)) {
          do {
            r = RandomUtil.getRandomPrime(maxLength);
            Assert.assertTrue(r.compareTo(minValue) >= 0);
            Assert.assertTrue(r.compareTo(maxValue) <= 0);
            Assert.assertTrue(MathUtil.isPrime(r));
          } while (!r.equals(i));
        }
      }
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testCreateRandomPrimeIntException() {
    RandomUtil.getRandomPrime(1);
  }

  @Test
  public void testCreateRandomPrimeIntRandom() {
    for (Random random : randoms) {
      for (int maxLength = 2; maxLength <= 6; maxLength++) {
        BigInteger minValue = BigInteger.valueOf(2).pow(maxLength-1);
        BigInteger maxValue = BigInteger.valueOf(2).pow(maxLength).subtract(BigInteger.ONE);
        BigInteger r;
        for (BigInteger i=minValue; i.compareTo(maxValue)<=0; i=i.add(BigInteger.ONE)) {
          if (MathUtil.isPrime(i)) {
            do {
              r = RandomUtil.getRandomPrime(maxLength, random);
              Assert.assertTrue(r.compareTo(minValue) >= 0);
              Assert.assertTrue(r.compareTo(maxValue) <= 0);
              Assert.assertTrue(MathUtil.isPrime(r));
            } while (!r.equals(i));
          }
        }
      }
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testCreateRandomPrimeIntRandomException() {
    RandomUtil.getRandomPrime(1, new Random());
  }

  @Test
  public void testCreateRandomSavePrimeInt() {
    for (int maxLength = 3; maxLength <= 6; maxLength++) {
      BigInteger minValue = BigInteger.valueOf(2).pow(maxLength-1);
      BigInteger maxValue = BigInteger.valueOf(2).pow(maxLength).subtract(BigInteger.ONE);
      BigInteger r;
      for (BigInteger i=minValue; i.compareTo(maxValue)<=0; i=i.add(BigInteger.ONE)) {
        if (MathUtil.isSavePrime(i)) {
          do {
            r = RandomUtil.getRandomSavePrime(maxLength);
            Assert.assertTrue(r.compareTo(minValue) >= 0);
            Assert.assertTrue(r.compareTo(maxValue) <= 0);
            Assert.assertTrue(MathUtil.isSavePrime(r));
          } while (!r.equals(i));
        }
      }
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testCreateRandomSavePrimeIntException() {
    RandomUtil.getRandomSavePrime(2);
  }

  @Test
  public void testCreateRandomSavePrimeIntRandom() {
    for (Random random : randoms) {
      for (int maxLength = 3; maxLength <= 6; maxLength++) {
        BigInteger minValue = BigInteger.valueOf(2).pow(maxLength-1);
        BigInteger maxValue = BigInteger.valueOf(2).pow(maxLength).subtract(BigInteger.ONE);
        BigInteger r;
        for (BigInteger i=minValue; i.compareTo(maxValue)<=0; i=i.add(BigInteger.ONE)) {
          if (MathUtil.isSavePrime(i)) {
            do {
              r = RandomUtil.getRandomSavePrime(maxLength, random);
              Assert.assertTrue(r.compareTo(minValue) >= 0);
              Assert.assertTrue(r.compareTo(maxValue) <= 0);
              Assert.assertTrue(MathUtil.isSavePrime(r));
            } while (!r.equals(i));
          }
        }
      }
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testCreateRandomSavePrimeIntRandomException() {
    RandomUtil.getRandomSavePrime(2, new Random());
  }

  @Test
  public void testCreateRandomPrimePairIntInt() {
    ArrayList<BigInteger[]> results = new ArrayList<BigInteger[]>();
    for (int i=1; i<=10; i++) {
      results.add(RandomUtil.getRandomPrimePair(3,2));
      results.add(RandomUtil.getRandomPrimePair(4,2));
      results.add(RandomUtil.getRandomPrimePair(5,2));
      results.add(RandomUtil.getRandomPrimePair(10,2));
      results.add(RandomUtil.getRandomPrimePair(20,2));
      results.add(RandomUtil.getRandomPrimePair(4,3));
      results.add(RandomUtil.getRandomPrimePair(5,3));
      results.add(RandomUtil.getRandomPrimePair(6,3));
      results.add(RandomUtil.getRandomPrimePair(10,3));
      results.add(RandomUtil.getRandomPrimePair(20,3));
      results.add(RandomUtil.getRandomPrimePair(5,4));
      results.add(RandomUtil.getRandomPrimePair(6,4));
      results.add(RandomUtil.getRandomPrimePair(7,4));
      results.add(RandomUtil.getRandomPrimePair(10,4));
      results.add(RandomUtil.getRandomPrimePair(20,4));
      results.add(RandomUtil.getRandomPrimePair(10,9));
      results.add(RandomUtil.getRandomPrimePair(20,19));
    }
    for (BigInteger[] pair: results) {
      Assert.assertTrue(MathUtil.isPrime(pair[0]));
      Assert.assertTrue(MathUtil.isPrime(pair[1]));
      Assert.assertEquals(pair[0].subtract(BigInteger.ONE).gcd(pair[1]), pair[1]);
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testCreateRandomPrimePairIntIntException1() {
    RandomUtil.getRandomPrimePair(2,3);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testCreateRandomPrimePairIntIntException2() {
    RandomUtil.getRandomPrimePair(3, 1);
  }

  @Test
  public void testCreateRandomPrimePairIntIntRandom() {
    for (Random random : randoms) {
      ArrayList<BigInteger[]> results = new ArrayList<BigInteger[]>();
      for (int i=1; i<=10; i++) {
        results.add(RandomUtil.getRandomPrimePair(3,2, random));
        results.add(RandomUtil.getRandomPrimePair(4,2, random));
        results.add(RandomUtil.getRandomPrimePair(5,2, random));
        results.add(RandomUtil.getRandomPrimePair(10,2, random));
        results.add(RandomUtil.getRandomPrimePair(20,2, random));
        results.add(RandomUtil.getRandomPrimePair(4,3, random));
        results.add(RandomUtil.getRandomPrimePair(5,3, random));
        results.add(RandomUtil.getRandomPrimePair(6,3, random));
        results.add(RandomUtil.getRandomPrimePair(10,3, random));
        results.add(RandomUtil.getRandomPrimePair(20,3, random));
        results.add(RandomUtil.getRandomPrimePair(5,4, random));
        results.add(RandomUtil.getRandomPrimePair(6,4, random));
        results.add(RandomUtil.getRandomPrimePair(7,4, random));
        results.add(RandomUtil.getRandomPrimePair(10,4, random));
        results.add(RandomUtil.getRandomPrimePair(20,4, random));
        results.add(RandomUtil.getRandomPrimePair(10,9, random));
        results.add(RandomUtil.getRandomPrimePair(20,19, random));
      }
      for (BigInteger[] pair: results) {
        Assert.assertTrue(MathUtil.isPrime(pair[0]));
        Assert.assertTrue(MathUtil.isPrime(pair[1]));
        Assert.assertEquals(pair[0].subtract(BigInteger.ONE).gcd(pair[1]), pair[1]);
      }
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testCreateRandomPrimePairIntIntRandomException1() {
    RandomUtil.getRandomPrimePair(2,3, new Random());
  }

  @Test (expected=IllegalArgumentException.class)
  public void testCreateRandomPrimePairIntIntRandomException2() {
    RandomUtil.getRandomPrimePair(3, 1, new Random());
  }
}
