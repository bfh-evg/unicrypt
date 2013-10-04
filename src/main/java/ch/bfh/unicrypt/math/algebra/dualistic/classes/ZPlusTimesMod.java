/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.dualistic.classes;

import ch.bfh.unicrypt.math.algebra.dualistic.abstracts.AbstractCyclicRing;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.utility.MathUtil;
import ch.bfh.unicrypt.math.utility.RandomUtil;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * This class implements the cyclic group Z_n = {0,...,n-1} with the operation
 * of addition modulo n. Its identity element is 0. Every integer in Z_n that is
 * relatively prime to n is a generator of Z_n. The smallest such group is Z_1 =
 * {0}.
 *
 * @see "Handbook of Applied Cryptography, Definition 2.113"
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public class ZPlusTimesMod extends AbstractCyclicRing<ZPlusTimesModElement> {

  private BigInteger modulus;

  protected ZPlusTimesMod(final BigInteger modulus) {
    this.modulus = modulus;
  }

  /**
   * Returns the modulus if this group.
   *
   * @return The modulus
   */
  public final BigInteger getModulus() {
    return this.modulus;
  }

  //
  // The following protected methods override the standard implementation from
  // various super-classes
  //
  @Override
  protected ZPlusTimesModElement standardSelfApply(Element element, BigInteger amount) {
    return this.abstractGetElement(element.getValue().multiply(amount).mod(this.getModulus()));
  }

  @Override
  protected ZPlusTimesModElement standardPower(Element element, BigInteger amount) {
    return this.abstractGetElement(element.getValue().modPow(amount, this.getModulus()));
  }

  @Override
  public boolean standardEquals(final Set set) {
    final ZPlusTimesMod zPlusTimesMod = (ZPlusTimesMod) set;
    return this.getModulus().equals(zPlusTimesMod.getModulus());
  }

  @Override
  protected boolean standardIsCompatible(Set set) {
    return (set instanceof ZPlusTimesMod);
  }

  @Override
  public int standardHashCode() {
    return this.getModulus().hashCode();
  }

  @Override
  public String standardToStringContent() {
    return this.getModulus().toString();
  }

  //
  // The following protected methods implement the abstract methods from
  // various super-classes
  //
  @Override
  protected ZPlusTimesModElement abstractApply(Element element1, Element element2) {
    return this.abstractGetElement(element1.getValue().add(element2.getValue()).mod(this.getModulus()));
  }

  @Override
  protected ZPlusTimesModElement abstractGetIdentityElement() {
    return this.abstractGetElement(BigInteger.ZERO);
  }

  @Override
  protected ZPlusTimesModElement abstractInvert(Element element) {
    return this.abstractGetElement(this.getModulus().subtract(element.getValue()).mod(this.getModulus()));
  }

  @Override
  protected ZPlusTimesModElement abstractMultiply(Element element1, Element element2) {
    return this.abstractGetElement(element1.getValue().multiply(element2.getValue()).mod(this.getModulus()));
  }

  @Override
  protected ZPlusTimesModElement abstractGetOne() {
    if (this.getModulus().equals(BigInteger.ONE)) {
      return this.abstractGetElement(BigInteger.ZERO);
    }
    return this.abstractGetElement(BigInteger.ONE);
  }

  @Override
  protected BigInteger abstractGetOrder() {
    return this.getModulus();
  }

  @Override
  protected ZPlusTimesModElement abstractGetElement(BigInteger value) {
    return new ZPlusTimesModElement(this, value) {
    };
  }

  @Override
  protected ZPlusTimesModElement abstractGetRandomElement(Random random) {
    return this.abstractGetElement(RandomUtil.createRandomBigInteger(this.getModulus().subtract(BigInteger.ONE), random));
  }

  @Override
  protected boolean abstractContains(BigInteger value) {
    return value.signum() >= 0 && value.compareTo(this.getModulus()) < 0;
  }

  @Override
  protected ZPlusTimesModElement abstractGetDefaultGenerator() {
    return this.abstractGetElement(BigInteger.ONE.mod(this.getModulus())); // mod is necessary for the trivial group Z_1
  }

  @Override
  protected ZPlusTimesModElement abstractGetRandomGenerator(Random random) {
    ZPlusTimesModElement element;
    do {
      element = this.getRandomElement(random);
    } while (!this.isGenerator(element));
    return element;
  }

  @Override
  protected boolean abstractIsGenerator(Element element) {
    if (this.getModulus().equals(BigInteger.ONE)) {
      return true;
    }
    return MathUtil.areRelativelyPrime(element.getValue(), this.getModulus());
  }
  //
  // STATIC FACTORY METHODS
  //
  private static final Map<BigInteger, ZPlusTimesMod> instances = new HashMap<BigInteger, ZPlusTimesMod>();

  public static ZPlusTimesMod getInstance(final int modulus) {
    return ZPlusTimesMod.getInstance(BigInteger.valueOf(modulus));
  }

  /**
   * Returns a the unique instance of this class for a given positive modulus.
   *
   * @param modulus The modulus
   * @return
   * @throws IllegalArgumentException if {@code modulus} is null, zero, or
   * negative
   */
  public static ZPlusTimesMod getInstance(final BigInteger modulus) {
    if ((modulus == null) || (modulus.signum() < 1)) {
      throw new IllegalArgumentException();
    }
    ZPlusTimesMod instance = ZPlusTimesMod.instances.get(modulus);
    if (instance == null) {
      instance = new ZPlusTimesMod(modulus);
      ZPlusTimesMod.instances.put(modulus, instance);
    }
    return instance;
  }

  public static ZPlusTimesMod getRandomInstance(int bitLength, Random random) {
    if (bitLength < 1) {
      throw new IllegalArgumentException();
    }
    return ZPlusTimesMod.getInstance(RandomUtil.createRandomBigInteger(bitLength, random));
  }

  public static ZPlusTimesMod getRandomInstance(int bitLength) {
    return ZPlusTimesMod.getRandomInstance(bitLength, (Random) null);
  }

}
