package ch.bfh.unicrypt.math.group.classes;

import java.math.BigInteger;
import java.util.Random;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.group.abstracts.AbstractMultiplicativeGroup;
import ch.bfh.unicrypt.math.group.interfaces.Group;
import ch.bfh.unicrypt.math.group.interfaces.Set;
import ch.bfh.unicrypt.math.helper.Factorization;
import ch.bfh.unicrypt.math.utility.MathUtil;
import ch.bfh.unicrypt.math.utility.RandomUtil;

/**
 * This class implements the group of integers Z*_n with the operation of multiplication modulo n. Its
 * identity element is 1. Every integer in Z*_n is relatively prime to n. The smallest such group
 * is Z*_2 = {1}.
 *
 * @see "Handbook of Applied Cryptography,  Definition 2.124"
 * @see <a href="http://en.wikipedia.org/wiki/Multiplicative_group_of_integers_modulo_n">http://en.wikipedia.org/wiki/Multiplicative_group_of_integers_modulo_n</a>
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public class ZStarMod extends AbstractMultiplicativeGroup {

  private static final long serialVersionUID = 1L;

  private final BigInteger modulus;
  private final Factorization moduloFactorization;

  /**
   * This is a private constructor of this class. It is called by the static factory methods
   * of the static nested class Factory.
   * @param modulus The given modulus
   */
  private ZStarMod(final BigInteger modulus) {
    this(modulus, new Factorization());
  }

  /**
   * This is a private constructor of this class. It is called by the static factory methods
   * of the static nested class Factory.
   * @param factorization The given factorization
   */
  private ZStarMod(final Factorization factorization) {
    this(factorization.getValue(), factorization);
  }

  /**
   * This is a private constructor of this class. It is called by the static factory methods
   * of the static nested class Factory.
   * @param modulus The given modulus
   * @param factorization The given factorization
   */
  private ZStarMod(final BigInteger modulus, final Factorization factorization) {
    this.modulus = modulus;
    this.moduloFactorization = factorization;
  }

  /**
   * Returns the modulus if this group.
   * @return The modulus
   */
  public final BigInteger getModulus() {
    return this.modulus;
  }

  /**
   * Returns a (possibly incomplete) prime factorization the modulus if this group. An incomplete
   * factorization implies that the group order is unknown in such a case.
   * @return The prime factorization
   */
  public final Factorization getModuloFactorization() {
    return this.moduloFactorization;
  }

  //
  // The following protected methods override the standard implementation from
  // various super-classes
  //

  @Override
  protected Element standardSelfApply(final Element element, final BigInteger amount) {
    BigInteger newAmount = amount;
    final BigInteger order = this.getOrder();
    if (!order.equals(Group.UNKNOWN_ORDER)) {
      newAmount = amount.mod(order);
    }
    return this.standardGetElement(element.getValue().modPow(newAmount, this.getModulus()));
  }

  @Override
  public boolean standardEquals(final Set set) {
    final ZStarMod zStarMod = (ZStarMod) set;
    return this.getModulus().equals(zStarMod.getModulus());
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
  protected Element abstractGetRandomElement(final Random random) {
    BigInteger randomValue;
    do {
      randomValue = RandomUtil.createRandomBigInteger(BigInteger.ONE, this.getModulus().subtract(BigInteger.ONE), random);
    } while (!this.contains(randomValue));
    return this.standardGetElement(randomValue);
  }

  @Override
  protected boolean abstractContains(final BigInteger value) {
    if (value == null) {
      throw new IllegalArgumentException();
    }
    return value.signum() >= 0 && value.compareTo(this.getModulus()) < 0 && MathUtil.areRelativelyPrime(value, this.getModulus());
  }

  @Override
  protected BigInteger abstractGetOrder() {
    if (!this.getModuloFactorization().getValue().equals(this.getModulus())) {
      return Group.UNKNOWN_ORDER;
    }
    return MathUtil.eulerFunction(this.getModulus(), this.getModuloFactorization().getPrimeFactors());
  }

  @Override
  protected Element abstractGetIdentityElement() {
    if (this.getModulus().equals(BigInteger.ONE)) {
      return this.standardGetElement(BigInteger.ZERO);
    }
    return this.standardGetElement(BigInteger.ONE);
  }

  @Override
  protected Element abstractApply(final Element element1, final Element element2) {
    return this.standardGetElement(element1.getValue().multiply(element2.getValue()).mod(this.getModulus()));
  }

  @Override
  public Element abstractInvert(final Element element) {
    return this.standardGetElement(element.getValue().modInverse(this.getModulus()));
  }

  //
  // STATIC FACTORY METHODS
  //

  /**
   * This is a static factory method to construct a new instance of this class for a given
   * {@code modulus >= 2}. If {@code modulus} is not prime, then a group of unknown order is returned.
   * @param modulus The modulus
   * @throws IllegalArgumentException if {@code modulus} is null or smaller than 2
   */
  public static ZStarMod getInstance(final BigInteger modulus) {
    if (modulus == null || modulus.compareTo(BigInteger.ONE) <= 0) {
      throw new IllegalArgumentException();
    }
    if (MathUtil.isPrime(modulus)) {
      return new ZStarMod(modulus, new Factorization(new BigInteger[]{modulus}));
    }
    return new ZStarMod(modulus);
  }

  /**
   * This is a static factory method to construct a new instance of this class, where the
   * group's modulus is the product of given list of prime factors. This always leads to a
   * group of known order.
   * @param primeFactors The given prime factors
   * @throws IllegalArgumentException if {@code primeFactors} is null or empty
   * @throws IllegalArgumentException if {@code primeFactors} contains null, values smaller than 2,
   * or composite numbers
   */
  public static ZStarMod getInstance(final BigInteger... primeFactors) {
    return new ZStarMod(new Factorization(primeFactors));
  }

  /**
   * This is a static factory method to construct a new instance of this class, where the
   * group's modulus is the product-of-powers of the given lists of prime factors and exponents.
   * This always leads to a group of known order.
   * @param primeFactors The given prime factors
   * @param exponents The given exponents
   * @throws IllegalArgumentException if {@code primeFactors} or {@code exponents} is null or empty
   * @throws IllegalArgumentException if {@code primeFactors} contains null, a value smaller than 2,
   * or a composite number
   * @throws IllegalArgumentException if {@code exponents} contains a value smaller than 1
   * @throws IllegalArgumentException if {@code primeFactors} and {@code exponents} have different lengths
   */
  public static ZStarMod getInstance(final BigInteger[] primeFactors, final int[] exponents) {
    return new ZStarMod(new Factorization(primeFactors, exponents));
  }

  /**
   * This is a static factory method to construct a new instance of this class, where the
   * group's modulus is value of the given prime factorization. This always leads to a group of known order.
   * @param factorization The given prime factorization
   * @throws IllegalArgumentException if {@code primeFactorization} is null
   * @throws IllegalArgumentException if {@code primeFactorization.getValue()} is 1
   */
  public static ZStarMod getInstance(final Factorization factorization) {
    if (factorization == null || factorization.getValue().compareTo(BigInteger.ONE) <= 0) {
      throw new IllegalArgumentException();
    }
    return new ZStarMod(factorization);
  }

  public static ZStarMod getInstance() {
    return getInstance(BigInteger.valueOf(2));
  }

}
