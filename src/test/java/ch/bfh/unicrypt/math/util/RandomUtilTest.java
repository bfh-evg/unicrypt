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

  public static Random[] randoms = new Random[] {null, new Random(), RandomUtil.createRandomGenerator()};

  @Test
  public void testCreateRandomGenerator() {
    Random prng1 = RandomUtil.createRandomGenerator();
    Random prng2 = RandomUtil.createRandomGenerator();
    Assert.assertNotSame(prng1, prng2);
    Assert.assertFalse(RandomUtil.createRandomBigInteger(100, prng1).equals(RandomUtil.createRandomBigInteger(100, prng2)));
  }

  @Test
  public void testCreateRandomGeneratorByteArray() {
    Random prng1 = RandomUtil.createRandomGenerator(new byte[]{(byte)123, (byte)231});
    Random prng2 = RandomUtil.createRandomGenerator(new byte[]{(byte)123, (byte)231});
    Random prng3 = RandomUtil.createRandomGenerator(new byte[]{(byte)231, (byte)123});
    Assert.assertEquals(RandomUtil.createRandomBigInteger(1, prng1), RandomUtil.createRandomBigInteger(1, prng2));
    Assert.assertEquals(RandomUtil.createRandomBigInteger(10, prng1), RandomUtil.createRandomBigInteger(10, prng2));
    Assert.assertEquals(RandomUtil.createRandomBigInteger(100, prng1), RandomUtil.createRandomBigInteger(100, prng2));
    Assert.assertTrue(RandomUtil.createRandomBigInteger(100, prng1).equals(RandomUtil.createRandomBigInteger(100, prng2)));
    Assert.assertFalse(RandomUtil.createRandomBigInteger(100, prng1).equals(RandomUtil.createRandomBigInteger(100, prng3)));
    Assert.assertFalse(RandomUtil.createRandomBigInteger(100, prng2).equals(RandomUtil.createRandomBigInteger(100, prng3)));
  }

  @Test
  public void testCreateRandomBoolean() {
    boolean r;
    do {
      r = RandomUtil.createRandomBoolean();
    } while (r);
    do {
      r = RandomUtil.createRandomBoolean();
    } while (!r);
  }

  @Test
  public void testCreateRandomBooleanRandom() {
    boolean r;
    for (Random random : randoms) {
      do {
        r = RandomUtil.createRandomBoolean(random);
      } while (r);
      do {
        r = RandomUtil.createRandomBoolean(random);
      } while (!r);
    }
  }

  @Test
  public void testCreateRandomIntInt() {
    Assert.assertEquals(RandomUtil.createRandomInt(0), 0);
    int maxValue = 10;
    int r;
    for (int i=0; i<=maxValue; i++) {
      do {
        r = RandomUtil.createRandomInt(maxValue);
        Assert.assertTrue(r >= 0);
        Assert.assertTrue(r <= maxValue);
      } while (r != i);
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testCreateRandomIntIntException() {
    RandomUtil.createRandomInt(-1);
  }

  @Test
  public void testCreateRandomIntIntInt() {
    Assert.assertEquals(RandomUtil.createRandomInt(5, 5), 5);
    Assert.assertEquals(RandomUtil.createRandomInt(-5, -5), -5);
    int minValue = -10;
    int maxValue = 10;
    int r;
    for (int i=minValue; i<=maxValue; i++) {
      do {
        r = RandomUtil.createRandomInt(minValue, maxValue);
        Assert.assertTrue(r >= minValue);
        Assert.assertTrue(r <= maxValue);
      } while (r != i);
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testCreateRandomIntIntIntException() {
    RandomUtil.createRandomInt(5, 3);
  }

  @Test
  public void testCreateRandomIntIntIntRandom() {
    for (Random random : randoms) {
      Assert.assertEquals(RandomUtil.createRandomInt(5, 5, random), 5);
      Assert.assertEquals(RandomUtil.createRandomInt(-5, -5, random), -5);
      int minValue = -10;
      int maxValue = 10;
      int r;
      for (int i=minValue; i<=maxValue; i++) {
        do {
          r = RandomUtil.createRandomInt(minValue, maxValue, random);
          Assert.assertTrue(r >= minValue);
          Assert.assertTrue(r <= maxValue);
        } while (r != i);
      }
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testCreateRandomIntIntIntRandomException() {
    RandomUtil.createRandomInt(5, 3, new Random());
  }

  @Test
  public void testCreateRandomIntIntRandom() {
    for (Random random : randoms) {
      Assert.assertEquals(RandomUtil.createRandomInt(0, random), 0);
      int maxValue = 10;
      int r;
      for (int i=0; i<=maxValue; i++) {
        do {
          r = RandomUtil.createRandomInt(maxValue, random);
          Assert.assertTrue(r >= 0);
          Assert.assertTrue(r <= maxValue);
        } while (r != i);
      }
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testCreateRandomIntIntRandomException() {
    RandomUtil.createRandomInt(-1, new Random());
  }

  @Test
  public void testCreateRandomBigIntegerInt() {
    Assert.assertEquals(RandomUtil.createRandomBigInteger(0), BigInteger.ZERO);
    int maxLength = 5; // Test the 2^5 = 32 values from 0...31
    BigInteger maxValue = BigInteger.valueOf(2).pow(maxLength).subtract(BigInteger.ONE);
    BigInteger r;
    for (BigInteger i=BigInteger.ZERO; i.compareTo(maxValue)<=0; i=i.add(BigInteger.ONE)) {
      do {
        r = RandomUtil.createRandomBigInteger(maxLength);
        Assert.assertTrue(r.signum() >= 0);
        Assert.assertTrue(r.compareTo(maxValue) <= 0);
      } while (!r.equals(i));
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testCreateRandomBigIntegerIntException() {
    RandomUtil.createRandomBigInteger(-1);
  }

  @Test
  public void testCreateRandomBigIntegerIntRandom() {
    for (Random random : randoms) {
      Assert.assertEquals(RandomUtil.createRandomBigInteger(0, random), BigInteger.ZERO);
      int maxLength = 5; // Test the 2^5 = 32 values from 0...31
      BigInteger maxValue = BigInteger.valueOf(2).pow(maxLength).subtract(BigInteger.ONE);
      BigInteger r;
      for (BigInteger i=BigInteger.ZERO; i.compareTo(maxValue)<=0; i=i.add(BigInteger.ONE)) {
        do {
          r = RandomUtil.createRandomBigInteger(maxLength, random);
          Assert.assertTrue(r.signum() >= 0);
          Assert.assertTrue(r.compareTo(maxValue) <= 0);
        } while (!r.equals(i));
      }
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testCreateRandomBigIntegerIntRandomException() {
    RandomUtil.createRandomBigInteger(-1, new Random());
  }

  @Test
  public void testCreateRandomBigIntegerBigInteger() {
    Assert.assertEquals(RandomUtil.createRandomBigInteger(BigInteger.ZERO), BigInteger.ZERO);
    BigInteger maxValue = BigInteger.valueOf(10);
    BigInteger r;
    for (BigInteger i=BigInteger.ZERO; i.compareTo(maxValue)<=0; i=i.add(BigInteger.ONE)) {
      do {
        r = RandomUtil.createRandomBigInteger(maxValue);
        Assert.assertTrue(r.signum() >= 0);
        Assert.assertTrue(r.compareTo(maxValue) <= 0);
      } while (!r.equals(i));
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testCreateRandomBigIntegerBigIntegerException1() {
    RandomUtil.createRandomBigInteger((BigInteger)null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testCreateRandomBigIntegerBigIntegerException2() {
    RandomUtil.createRandomBigInteger(BigInteger.valueOf(-1));
  }

  @Test
  public void testCreateRandomBigIntegerBigIntegerRandom() {
    for (Random random : randoms) {
      Assert.assertEquals(RandomUtil.createRandomBigInteger(BigInteger.ZERO, random), BigInteger.ZERO);
      BigInteger maxValue = BigInteger.valueOf(10);
      BigInteger r;
      for (BigInteger i=BigInteger.ZERO; i.compareTo(maxValue)<=0; i=i.add(BigInteger.ONE)) {
        do {
          r = RandomUtil.createRandomBigInteger(maxValue, random);
          Assert.assertTrue(r.signum() >= 0);
          Assert.assertTrue(r.compareTo(maxValue) <= 0);
        } while (!r.equals(i));
      }
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testCreateRandomBigIntegerBigIntegerRandomException1() {
    RandomUtil.createRandomBigInteger((BigInteger)null, new Random());
  }

  @Test (expected=IllegalArgumentException.class)
  public void testCreateRandomBigIntegerBigIntegerRandomException2() {
    RandomUtil.createRandomBigInteger(BigInteger.valueOf(-1), new Random());
  }

  @Test
  public void testCreateRandomBigIntegerBigIntegerBigInteger() {
    Assert.assertEquals(RandomUtil.createRandomBigInteger(BigInteger.TEN, BigInteger.TEN), BigInteger.TEN);
    BigInteger minValue = BigInteger.valueOf(5);
    BigInteger maxValue = BigInteger.valueOf(15);
    BigInteger r;
    for (BigInteger i=minValue; i.compareTo(maxValue)<=0; i=i.add(BigInteger.ONE)) {
      do {
        r = RandomUtil.createRandomBigInteger(minValue, maxValue);
        Assert.assertTrue(r.compareTo(minValue) >= 0);
        Assert.assertTrue(r.compareTo(maxValue) <= 0);
      } while (!r.equals(i));
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testCreateRandomBigIntegerBigIntegerBigIntegerException1() {
    RandomUtil.createRandomBigInteger((BigInteger)null, (BigInteger)null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testCreateRandomBigIntegerBigIntegerBigIntegerException2() {
    RandomUtil.createRandomBigInteger((BigInteger)null, BigInteger.TEN);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testCreateRandomBigIntegerBigIntegerBigIntegerException3() {
    RandomUtil.createRandomBigInteger(BigInteger.TEN, (BigInteger)null);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testCreateRandomBigIntegerBigIntegerBigIntegerException4() {
    RandomUtil.createRandomBigInteger(BigInteger.TEN, BigInteger.ONE);
  }

  @Test
  public void testCreateRandomBigIntegerBigIntegerBigIntegerRandom() {
    for (Random random : randoms) {
      Assert.assertEquals(RandomUtil.createRandomBigInteger(BigInteger.TEN, BigInteger.TEN, random), BigInteger.TEN);
      BigInteger minValue = BigInteger.valueOf(5);
      BigInteger maxValue = BigInteger.valueOf(15);
      BigInteger r;
      for (BigInteger i=minValue; i.compareTo(maxValue)<=0; i=i.add(BigInteger.ONE)) {
        do {
          r = RandomUtil.createRandomBigInteger(minValue, maxValue, random);
          Assert.assertTrue(r.compareTo(minValue) >= 0);
          Assert.assertTrue(r.compareTo(maxValue) <= 0);
        } while (!r.equals(i));
      }
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testCreateRandomBigIntegerBigIntegerBigIntegerRandomException1() {
    RandomUtil.createRandomBigInteger((BigInteger)null, (BigInteger)null, new Random());
  }

  @Test (expected=IllegalArgumentException.class)
  public void testCreateRandomBigIntegerBigIntegerBigIntegerRandomException2() {
    RandomUtil.createRandomBigInteger((BigInteger)null, BigInteger.TEN, new Random());
  }

  @Test (expected=IllegalArgumentException.class)
  public void testCreateRandomBigIntegerBigIntegerBigIntegerRandomException3() {
    RandomUtil.createRandomBigInteger(BigInteger.TEN, (BigInteger)null, new Random());
  }

  @Test (expected=IllegalArgumentException.class)
  public void testCreateRandomBigIntegerBigIntegerBigIntegerRandomException4() {
    RandomUtil.createRandomBigInteger(BigInteger.TEN, BigInteger.ONE, new Random());
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
            r = RandomUtil.createRandomPrime(maxLength);
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
    RandomUtil.createRandomPrime(1);
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
              r = RandomUtil.createRandomPrime(maxLength, random);
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
    RandomUtil.createRandomPrime(1, new Random());
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
            r = RandomUtil.createRandomSavePrime(maxLength);
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
    RandomUtil.createRandomSavePrime(2);
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
              r = RandomUtil.createRandomSavePrime(maxLength, random);
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
    RandomUtil.createRandomSavePrime(2, new Random());
  }

  @Test
  public void testCreateRandomPrimePairIntInt() {
    ArrayList<BigInteger[]> results = new ArrayList<BigInteger[]>();
    for (int i=1; i<=10; i++) {
      results.add(RandomUtil.createRandomPrimePair(3,2));
      results.add(RandomUtil.createRandomPrimePair(4,2));
      results.add(RandomUtil.createRandomPrimePair(5,2));
      results.add(RandomUtil.createRandomPrimePair(10,2));
      results.add(RandomUtil.createRandomPrimePair(20,2));
      results.add(RandomUtil.createRandomPrimePair(4,3));
      results.add(RandomUtil.createRandomPrimePair(5,3));
      results.add(RandomUtil.createRandomPrimePair(6,3));
      results.add(RandomUtil.createRandomPrimePair(10,3));
      results.add(RandomUtil.createRandomPrimePair(20,3));
      results.add(RandomUtil.createRandomPrimePair(5,4));
      results.add(RandomUtil.createRandomPrimePair(6,4));
      results.add(RandomUtil.createRandomPrimePair(7,4));
      results.add(RandomUtil.createRandomPrimePair(10,4));
      results.add(RandomUtil.createRandomPrimePair(20,4));
      results.add(RandomUtil.createRandomPrimePair(10,9));
      results.add(RandomUtil.createRandomPrimePair(20,19));
    }
    for (BigInteger[] pair: results) {
      Assert.assertTrue(MathUtil.isPrime(pair[0]));
      Assert.assertTrue(MathUtil.isPrime(pair[1]));
      Assert.assertEquals(pair[0].subtract(BigInteger.ONE).gcd(pair[1]), pair[1]);
    }
  }

  @Test (expected=IllegalArgumentException.class)
  public void testCreateRandomPrimePairIntIntException1() {
    RandomUtil.createRandomPrimePair(2,3);
  }

  @Test (expected=IllegalArgumentException.class)
  public void testCreateRandomPrimePairIntIntException2() {
    RandomUtil.createRandomPrimePair(3, 1);
  }

  @Test
  public void testCreateRandomPrimePairIntIntRandom() {
    for (Random random : randoms) {
      ArrayList<BigInteger[]> results = new ArrayList<BigInteger[]>();
      for (int i=1; i<=10; i++) {
        results.add(RandomUtil.createRandomPrimePair(3,2, random));
        results.add(RandomUtil.createRandomPrimePair(4,2, random));
        results.add(RandomUtil.createRandomPrimePair(5,2, random));
        results.add(RandomUtil.createRandomPrimePair(10,2, random));
        results.add(RandomUtil.createRandomPrimePair(20,2, random));
        results.add(RandomUtil.createRandomPrimePair(4,3, random));
        results.add(RandomUtil.createRandomPrimePair(5,3, random));
        results.add(RandomUtil.createRandomPrimePair(6,3, random));
        results.add(RandomUtil.createRandomPrimePair(10,3, random));
        results.add(RandomUtil.createRandomPrimePair(20,3, random));
        results.add(RandomUtil.createRandomPrimePair(5,4, random));
        results.add(RandomUtil.createRandomPrimePair(6,4, random));
        results.add(RandomUtil.createRandomPrimePair(7,4, random));
        results.add(RandomUtil.createRandomPrimePair(10,4, random));
        results.add(RandomUtil.createRandomPrimePair(20,4, random));
        results.add(RandomUtil.createRandomPrimePair(10,9, random));
        results.add(RandomUtil.createRandomPrimePair(20,19, random));
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
    RandomUtil.createRandomPrimePair(2,3, new Random());
  }

  @Test (expected=IllegalArgumentException.class)
  public void testCreateRandomPrimePairIntIntRandomException2() {
    RandomUtil.createRandomPrimePair(3, 1, new Random());
  }
}
