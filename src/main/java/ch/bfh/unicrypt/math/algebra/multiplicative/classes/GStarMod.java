package ch.bfh.unicrypt.math.algebra.multiplicative.classes;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.algebra.multiplicative.abstracts.AbstractMultiplicativeCyclicGroup;
import ch.bfh.unicrypt.math.helper.factorization.Factorization;
import ch.bfh.unicrypt.math.helper.factorization.SpecialFactorization;
import ch.bfh.unicrypt.math.utility.MathUtil;
import java.math.BigInteger;
import java.util.Random;

/**
 * This interface represents the concept of a sub-group G_m (of order m) of a
 * cyclic group of integers Z*_n with the operation of multiplication modulo n.
 * For Z*_n to be cyclic, n must be 2, 4, p^e, or 2p^e, where p>2 is prime and
 * e>0. The actual sub-group depends on the given set of prime factors of the
 * order phi(n) of Z*_n, where phi(n) is the Euler totient function. The order
 * m=|G_m| is the product of all given prime factors of phi(n). If all prime
 * factors of phi(n) are given, which implies m=phi(n), then G_m is the parent
 * group Z*_n.
 * <p>
 * @see "Handbook of Applied Cryptography, Fact 2.132"
 * @see "Handbook of Applied Cryptography, Definition 2.100"
 * @see "Handbook of Applied Cryptography, Definition 2.166"
 * <p>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public class GStarMod
       extends AbstractMultiplicativeCyclicGroup<GStarModElement> {

  private final BigInteger modulus;
  private final SpecialFactorization moduloFactorization;
  private final Factorization orderFactorization;
  private ZStarMod superGroup;

  protected GStarMod(SpecialFactorization moduloFactorization, Factorization orderFactorization) {
    this.modulus = moduloFactorization.getValue();
    this.moduloFactorization = moduloFactorization;
    this.orderFactorization = orderFactorization;
  }

  /**
   * Returns the modulus if this group.
   * <p>
   * @return The modulus
   */
  public final BigInteger getModulus() {
    return this.modulus;
  }

  /**
   * Returns a (possibly incomplete) prime factorization the modulus if this
   * group. An incomplete factorization implies that the group order is unknown
   * in such a case.
   * <p>
   * @return The prime factorization
   */
  public final SpecialFactorization getModuloFactorization() {
    return this.moduloFactorization;
  }

  /**
   * Returns prime factorization of the group order phi(n) of Z*_n.
   * <p>
   * @return The prime factorization of the group order
   */
  public final Factorization getOrderFactorization() {
    return this.orderFactorization;
  }

  public final ZStarMod getZStarMod() {
    if (this.superGroup == null) {
      this.superGroup = ZStarMod.getInstance(this.getModuloFactorization());
    }
    return this.superGroup;
  }

  /**
   * Returns the quotient k=phi(n)/m of the orders of the two involved groups.
   * <p>
   * @return The quotient of the two orders.
   */
  public BigInteger getCoFactor() {
    return this.getZStarMod().getOrder().divide(this.getOrder());
  }

  //
  // The following protected methods override the standard implementation from
  // various super-classes
  //
  @Override
  protected GStarModElement standardSelfApply(final Element element, final BigInteger amount) {
    BigInteger newAmount = amount.mod(this.getOrder());
    return this.abstractGetElement(element.getValue().modPow(newAmount, this.getModulus()));
  }

  @Override
  protected boolean standardIsEqual(Set set) {
    final GStarMod other = (GStarMod) set;
    return this.getModulus().equals(other.getModulus()) && this.getOrder().equals(other.getOrder());
  }

  @Override
  protected boolean standardIsCompatible(Set set) {
    return (set instanceof GStarMod);
  }

  @Override
  public String standardToStringContent() {
    return this.getModulus().toString() + "," + this.getOrder().toString();
  }

  //
  // The following protected methods implement the abstract methods from
  // various super-classes
  //
  @Override
  protected GStarModElement abstractGetElement(BigInteger value) {
    return new GStarModElement(this, value) {
    };
  }

  @Override
  protected GStarModElement abstractGetRandomElement(final Random random) {
    ZStarModElement randomElement = this.getZStarMod().getRandomElement(random);
    return this.getElement(randomElement.power(this.getCoFactor()));
// VERSION WITH OPTIMIZED EFFICIENCY BUT LACK OF INDEPENDENCE
//    if (this.getOrder().compareTo(this.getCoFactor()) > 0) { // choose between the faster method
//      // Method 1
//      ZStarModElement randomElement = this.getZStarMod().getRandomElement(random);
//      return this.getElement(randomElement.power(this.getCoFactor()));
//    }
//    // Method 2
//    return this.getDefaultGenerator().power(this.getZModOrder().getRandomElement(random));
  }

  @Override
  protected boolean abstractContains(final BigInteger value) {
    if (value == null) {
      throw new IllegalArgumentException();
    }
    return (value.signum() >= 0) && (value.compareTo(this.getModulus()) < 0) && MathUtil.areRelativelyPrime(value, this.getModulus())
           && value.mod(this.getModulus()).modPow(this.getOrder(), this.getModulus()).equals(BigInteger.ONE);
  }

  @Override
  protected BigInteger abstractGetOrder() {
    return this.getOrderFactorization().getValue();
  }

  @Override
  protected GStarModElement abstractGetIdentityElement() {
    return this.abstractGetElement(BigInteger.ONE);
  }

  @Override
  protected GStarModElement abstractApply(final Element element1, final Element element2) {
    return this.abstractGetElement(element1.getValue().multiply(element2.getValue()).mod(this.getModulus()));
  }

  @Override
  protected GStarModElement abstractInvert(final Element element) {
    return this.abstractGetElement(element.getValue().modInverse(this.getModulus()));
  }

  /**
   * See http://en.wikipedia.org/wiki/Schnorr_group
   * <p>
   * @return
   */
  @Override
  protected GStarModElement abstractGetDefaultGenerator() {
    BigInteger alpha = BigInteger.ZERO;
    GStarModElement element;
    do {
      do {
        alpha = alpha.add(BigInteger.ONE);
      } while (!MathUtil.areRelativelyPrime(alpha, this.getModulus()));
      element = this.abstractGetElement(alpha.modPow(this.getCoFactor(), this.getModulus()));
    } while (!this.isGenerator(element)); // this test could be skipped for a prime order
    return element;
  }

  // see Handbook of Applied Cryptography, Algorithm 4.80 and Note 4.81 (the implemented)
  // method is a mix between 4.80 and 4.81
  // See also http://en.wikipedia.org/wiki/Schnorr_group
  @Override
  protected boolean abstractIsGenerator(Element element) {
    for (final BigInteger prime : this.getOrderFactorization().getPrimeFactors()) {
      if (element.selfApply(this.getOrder().divide(prime)).isEqual(this.getIdentityElement())) {
        return false;
      }
    }
    return true;
  }

  //
  // STATIC FACTORY METHODS
  //
  /**
   * This is the general static factory method for this class.
   * <p>
   * @param moduloFactorization
   * @param orderFactorization
   * @return
   * @throws IllegalArgumentException if {@literal moduloFactorization} or
   *                                  {@literal orderFactorization} is null
   * @throws IllegalArgumentException if the value of
   *                                  {@literal orderFactorization} does not
   *                                  divide phi(n)
   */
  public static GStarMod getInstance(SpecialFactorization moduloFactorization, Factorization orderFactorization) {
    GStarMod group = new GStarMod(moduloFactorization, orderFactorization);
    if (!group.getOrder().mod(orderFactorization.getValue()).equals(BigInteger.ZERO)) {
      throw new IllegalArgumentException();
    }
    return group;
  }

}
