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
 * <p>
 * @see "Handbook of Applied Cryptography, Definition 2.113"
 * <p>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public class ZMod
       extends AbstractCyclicRing<ZModElement> {

  private BigInteger modulus;

  protected ZMod(final BigInteger modulus) {
    this.modulus = modulus;
  }

  /**
   * Returns the modulus if this group.
   * <p>
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
  protected ZModElement standardSelfApply(Element element, BigInteger amount) {
    return this.abstractGetElement(element.getValue().multiply(amount).mod(this.getModulus()));
  }

  @Override
  protected ZModElement standardPower(Element element, BigInteger amount) {
    return this.abstractGetElement(element.getValue().modPow(amount, this.getModulus()));
  }

  @Override
  public boolean standardIsEqual(final Set set) {
    final ZMod zPlusTimesMod = (ZMod) set;
    return this.getModulus().equals(zPlusTimesMod.getModulus());
  }

  @Override
  protected boolean standardIsCompatible(Set set) {
    return (set instanceof ZMod);
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
  protected ZModElement abstractApply(Element element1, Element element2) {
    return this.abstractGetElement(element1.getValue().add(element2.getValue()).mod(this.getModulus()));
  }

  @Override
  protected ZModElement abstractGetIdentityElement() {
    return this.abstractGetElement(BigInteger.ZERO);
  }

  @Override
  protected ZModElement abstractInvert(Element element) {
    return this.abstractGetElement(this.getModulus().subtract(element.getValue()).mod(this.getModulus()));
  }

  @Override
  protected ZModElement abstractMultiply(Element element1, Element element2) {
    return this.abstractGetElement(element1.getValue().multiply(element2.getValue()).mod(this.getModulus()));
  }

  @Override
  protected ZModElement abstractGetOne() {
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
  protected ZModElement abstractGetElement(BigInteger value) {
    return new ZModElement(this, value);
  }

  @Override
  protected ZModElement abstractGetRandomElement(Random random) {
    return this.abstractGetElement(RandomUtil.getRandomBigInteger(this.getModulus().subtract(BigInteger.ONE), random));
  }

  @Override
  protected boolean abstractContains(BigInteger value) {
    return value.signum() >= 0 && value.compareTo(this.getModulus()) < 0;
  }

  @Override
  protected ZModElement abstractGetDefaultGenerator() {
    return this.abstractGetElement(BigInteger.ONE.mod(this.getModulus())); // mod is necessary for the trivial group Z_1
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
  private static final Map<BigInteger, ZMod> instances = new HashMap<BigInteger, ZMod>();

  public static ZMod getInstance(final int modulus) {
    return ZMod.getInstance(BigInteger.valueOf(modulus));
  }

  /**
   * Returns a the unique instance of this class for a given positive modulus.
   * <p>
   * @param modulus The modulus
   * @return
   * @throws IllegalArgumentException if {@literal modulus} is null, zero, or
   *                                  negative
   */
  public static ZMod getInstance(final BigInteger modulus) {
    if ((modulus == null) || (modulus.compareTo(BigInteger.valueOf(2)) < 0)) {
      throw new IllegalArgumentException();
    }
    ZMod instance = ZMod.instances.get(modulus);
    if (instance == null) {
      instance = new ZMod(modulus);
      ZMod.instances.put(modulus, instance);
    }
    return instance;
  }

  public static ZMod getRandomInstance(int bitLength, Random random) {
    if (bitLength < 2) {
      throw new IllegalArgumentException();
    }
    return ZMod.getInstance(RandomUtil.getRandomBigInteger(bitLength, random));
  }

  public static ZMod getRandomInstance(int bitLength) {
    return ZMod.getRandomInstance(bitLength, (Random) null);
  }

}
