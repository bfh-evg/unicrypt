package ch.bfh.unicrypt.math.algebra.multiplicative.classes;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.algebra.multiplicative.abstracts.AbstractMultiplicativeMonoid;
import ch.bfh.unicrypt.math.utility.RandomUtil;

/**
 * This class implements the monoid Z_n = {0,...,n-1} with the operation of
 * multiplication modulo n. Its identity element is 1, except in the case of Z_1
 * = {0}, where the identity element is 0.
 *
 * @see "Handbook of Applied Cryptography, Definition 2.113"
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public class ZTimesMod extends AbstractMultiplicativeMonoid<ZTimesModElement> {

  private BigInteger modulus;

  protected ZTimesMod(final BigInteger modulus) {
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
  protected ZTimesModElement standardSelfApply(Element element, BigInteger amount) {
    return this.abstractGetElement(element.getValue().modPow(amount, this.getModulus()));
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
  protected ZTimesModElement abstractGetElement(BigInteger value) {
    return new ZTimesModElement(this, value) {
    };
  }

  @Override
  protected BigInteger abstractGetOrder() {
    return this.getModulus();
  }

  @Override
  protected ZTimesModElement abstractGetRandomElement(final Random random) {
    return this.abstractGetElement(RandomUtil.createRandomBigInteger(this.getModulus().subtract(BigInteger.ONE), random));
  }

  @Override
  protected boolean abstractContains(final BigInteger value) {
    return value.signum() >= 0 && value.compareTo(this.getModulus()) < 0;
  }

  @Override
  protected ZTimesModElement abstractGetIdentityElement() {
    if (this.getModulus().equals(BigInteger.ONE)) {
      return this.abstractGetElement(BigInteger.ZERO);
    }
    return this.abstractGetElement(BigInteger.ONE);
  }

  @Override
  protected ZTimesModElement abstractApply(final Element element1, final Element element2) {
    return this.abstractGetElement(element1.getValue().multiply(element2.getValue()).mod(this.getModulus()));
  }

  @Override
  public boolean standardEquals(final Set set) {
    final ZTimesMod zTimesMod = (ZTimesMod) set;
    return this.getModulus().equals(zTimesMod.getModulus());
  }

  @Override
  protected boolean standardIsCompatible(Set set) {
    return (set instanceof ZTimesMod);
  }
  //
  // STATIC FACTORY METHODS
  //
  private static final Map<BigInteger, ZTimesMod> instances = new HashMap<BigInteger, ZTimesMod>();

  public static ZTimesMod getInstance(final int modulus) {
    return ZTimesMod.getInstance(BigInteger.valueOf(modulus));
  }

  /**
   * Returns a the unique instance of this class for a given positive modulus.
   *
   * @param modulus The modulus
   * @throws IllegalArgumentException if {@code modulus} is null, zero, or
   * negative
   */
  public static ZTimesMod getInstance(final BigInteger modulus) {
    if ((modulus == null) || (modulus.signum() < 1)) {
      throw new IllegalArgumentException();
    }
    ZTimesMod instance = ZTimesMod.instances.get(modulus);
    if (instance == null) {
      instance = new ZTimesMod(modulus);
      ZTimesMod.instances.put(modulus, instance);
    }
    return instance;
  }

  public static ZTimesMod getRandomInstance(int bitLength, Random random) {
    if (bitLength < 1) {
      throw new IllegalArgumentException();
    }
    return ZTimesMod.getInstance(RandomUtil.createRandomBigInteger(bitLength, random));
  }

  public static ZTimesMod getRandomInstance(int bitLength) {
    return ZTimesMod.getRandomInstance(bitLength, (Random) null);
  }

}
