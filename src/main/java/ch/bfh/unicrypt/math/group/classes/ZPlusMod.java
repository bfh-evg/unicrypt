package ch.bfh.unicrypt.math.group.classes;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.group.abstracts.AbstractAdditiveCyclicGroup;
import ch.bfh.unicrypt.math.group.interfaces.Group;
import ch.bfh.unicrypt.math.group.interfaces.Set;
import ch.bfh.unicrypt.math.utility.RandomUtil;

/**
 * This class implements the cyclic group Z_n = {0,...,n-1} with the
 * operation of addition modulo n. Its identity element is 0. Every integer in Z_n
 * that is relatively prime to n is a generator of Z_n. The smallest such group is Z_1 = {0}.
 *
 * @see "Handbook of Applied Cryptography, Definition 2.113"
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public class ZPlusMod extends AbstractAdditiveCyclicGroup {

  private static final long serialVersionUID = 1L;

  private BigInteger modulus;

  protected ZPlusMod(final BigInteger modulus) {
    this.modulus = modulus;
  }

  /**
   * Returns the modulus if this group.
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
  protected Element standardSelfApply(Element element, BigInteger amount) {
    return this.standardGetElement(element.getValue().multiply(amount).mod(this.getModulus()));
  }

  @Override
  public int standardHashCode() {
    return this.getModulus().hashCode();
  }

  @Override
  public String standardToString() {
    return this.getModulus().toString();
  }

  //
  // The following protected methods implement the abstract methods from
  // various super-classes
  //

  @Override
  public boolean abstractEquals(final Set set) {
    final ZPlusMod zPlusMod = (ZPlusMod) set;
    return this.getModulus().equals(zPlusMod.getModulus());
  }

  @Override
  protected BigInteger abstractGetOrder() {
    return this.getModulus();
  }

  @Override
  protected Element abstractGetRandomElement(final Random random) {
    return this.standardGetElement(RandomUtil.createRandomBigInteger(this.getModulus().subtract(BigInteger.ONE), random));
  }

  @Override
  protected boolean abstractContains(final BigInteger value) {
    return value.signum() >= 0 && value.compareTo(this.getModulus()) < 0;
  }

  @Override
  protected Element abstractGetDefaultGenerator() {
    return this.standardGetElement(BigInteger.ONE.mod(this.getModulus())); // mod is necessary for the trivial group Z_1
  }

  @Override
  public boolean abstractIsGenerator(final Element element) {
    if (this.getModulus().equals(BigInteger.ONE)) {
      return true;
    }
    return this.getModulus().gcd(element.getValue()).equals(BigInteger.ONE);
  }

  @Override
  protected Element abstractGetIdentityElement() {
    return this.standardGetElement(BigInteger.ZERO);
  }

  @Override
  protected Element abstractApply(final Element element1, final Element element2) {
    return this.standardGetElement(element1.getValue().add(element2.getValue()).mod(this.getModulus()));
  }

  @Override
  protected Element abstractInvert(final Element element) {
    return this.standardGetElement(this.getModulus().subtract(element.getValue()).mod(this.getModulus()));
  }

  @Override
  public Element abstractGetRandomGenerator(final Random random) {
    Element element;
    do {
      element = this.getRandomElement(random);
    } while (!this.isGenerator(element));
    return element;
  }

  //
  // STATIC FACTORY METHODS
  //

  private static final Map<BigInteger,ZPlusMod> instances = new HashMap<BigInteger,ZPlusMod>();

  /**
   * Returns a the unique instance of this class for a given positive modulus.
   * @param modulus The modulus
   * @throws IllegalArgumentException if {@code modulus} is null, zero, or negative
   */
  public static ZPlusMod getInstance(final BigInteger modulus) {
    if ((modulus == null) || (modulus.signum() < 1)) {
      throw new IllegalArgumentException();
    }
    ZPlusMod instance = ZPlusMod.instances.get(modulus);
    if (instance == null) {
      instance = new ZPlusMod(modulus);
      ZPlusMod.instances.put(modulus, instance);
    }
    return instance;
  }

  public static ZPlusMod getInstance() {
    return getInstance(BigInteger.ONE);
  }

}
