package ch.bfh.unicrypt.math.multiplicative.classes;

import java.math.BigInteger;
import java.util.Random;

import ch.bfh.unicrypt.math.general.interfaces.Element;
import ch.bfh.unicrypt.math.multiplicative.interfaces.MultiplicativeElement;
import ch.bfh.unicrypt.math.multiplicative.abstracts.AbstractMultiplicativeCyclicGroup;
import ch.bfh.unicrypt.math.general.interfaces.DDHGroup;
import ch.bfh.unicrypt.math.general.interfaces.Set;
import ch.bfh.unicrypt.math.utility.Factorization;
import ch.bfh.unicrypt.math.utility.SpecialFactorization;
import ch.bfh.unicrypt.math.utility.MathUtil;
import ch.bfh.unicrypt.math.utility.RandomUtil;

/**
 * This interface represents the concept of a sub-group G_m (of order m) of a
 * cyclic group of integers Z*_n with the operation of multiplication modulo n.
 * For Z*_n to be cyclic, n must be 2, 4, p^e, or 2p^e, where p>2 is prime and
 * e>0. The actual sub-group depends on the given set of prime factors of the
 * order phi(n) of Z*_n, where phi(n) is the Euler totient function. The order
 * m=|G_m| is the product of all given prime factors of phi(n). If all prime
 * factors of phi(n) are given, which implies m=phi(n), then G_m is the parent
 * group Z*_n.
 *
 * @see "Handbook of Applied Cryptography, Fact 2.132"
 * @see "Handbook of Applied Cryptography, Definition 2.100"
 * @see "Handbook of Applied Cryptography, Definition 2.166"
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public class GStarMod extends AbstractMultiplicativeCyclicGroup implements DDHGroup {

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
   *
   * @return The modulus
   */
  public final BigInteger getModulus() {
    return this.modulus;
  }

  /**
   * Returns a (possibly incomplete) prime factorization the modulus if this
   * group. An incomplete factorization implies that the group order is unknown
   * in such a case.
   *
   * @return The prime factorization
   */
  public final SpecialFactorization getModuloFactorization() {
    return this.moduloFactorization;
  }

  /**
   * Returns prime factorization of the group order phi(n) of Z*_n.
   *
   * @return The prime factorization of the group order
   */
  public final Factorization getOrderFactorization() {
    return this.orderFactorization;
  }

  public final ZStarMod getSuperGroup() {
    if (this.superGroup == null) {
      this.superGroup = ZStarMod.getInstance(this.getModuloFactorization());
    }
    return this.superGroup;
  }

  /**
   * Returns the quotient k=phi(n)/m of the orders of the two involved groups.
   *
   * @return The quotient of the two orders.
   */
  public BigInteger getOrderQuotient() {
    return this.getSuperGroup().getOrder().divide(this.getOrder());
  }

  //
  // The following protected methods override the standard implementation from
  // various super-classes
  //
  @Override
  protected MultiplicativeElement standardSelfApply(final Element element, final BigInteger amount) {
    BigInteger newAmount = amount.mod(this.getOrder());
    return this.abstractGetElement(element.getValue().modPow(newAmount, this.getModulus()));
  }

  @Override
  protected boolean standardEquals(Set set) {
    final GStarMod other = (GStarMod) set;
    return this.getModulus().equals(other.getModulus()) && this.getOrder().equals(other.getOrder());
  }

  @Override
  protected boolean standardIsCompatible(Set set) {
    return (set instanceof GStarMod);
  }

  @Override
  public int standardHashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + this.getModulus().hashCode();
    result = prime * result + this.getOrder().hashCode();
    return result;
  }

  @Override
  public String standardToString() {
    return this.getModulus().toString() + "," + this.getOrder().toString();
  }

  //
  // The following protected methods implement the abstract methods from
  // various super-classes
  //
  @Override
  protected MultiplicativeElement abstractGetRandomElement(final Random random) {
    if (this.getOrder().compareTo(this.getOrderQuotient()) > 0) { // choose between the faster method
      // Method 1
      BigInteger randomValue;
      do {
        randomValue = RandomUtil.createRandomBigInteger(BigInteger.ONE, this.getModulus().subtract(BigInteger.ONE), random);
      } while (!randomValue.gcd(this.getModulus()).equals(BigInteger.ONE));
      return this.abstractGetElement(randomValue.modPow(this.getOrderQuotient(), this.getModulus()));
    }
    // Method 2
    return this.selfApply(this.getDefaultGenerator(), RandomUtil.createRandomBigInteger(BigInteger.ONE, this.getOrder(), random));
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
    return MathUtil.eulerFunction(this.getModulus(), this.getModuloFactorization().getPrimeFactors());
  }

  @Override
  protected MultiplicativeElement abstractGetIdentityElement() {
    return this.abstractGetElement(BigInteger.ONE);
  }

  @Override
  protected MultiplicativeElement abstractApply(final Element element1, final Element element2) {
    return this.abstractGetElement(element1.getValue().multiply(element2.getValue()).mod(this.getModulus()));
  }

  @Override
  public MultiplicativeElement abstractInvert(final Element element) {
    return this.abstractGetElement(element.getValue().modInverse(this.getModulus()));
  }

  @Override
  protected MultiplicativeElement abstractGetDefaultGenerator() {
    BigInteger alpha = BigInteger.ZERO;
    MultiplicativeElement element;
    do {
      do {
        alpha = alpha.add(BigInteger.ONE);
      } while (!alpha.gcd(this.getModulus()).equals(BigInteger.ONE));
      element = this.abstractGetElement(alpha.modPow(this.getOrderQuotient(), this.getModulus()));
    } while (!this.isGenerator(element)); // this test could be skipped for a prime order
    return element;
  }

  // see Handbook of Applied Cryptography, Algorithm 4.80 and Note 4.81
  @Override
  protected MultiplicativeElement abstractGetRandomGenerator(Random random) {
    MultiplicativeElement element;
    do {
      element = this.getRandomElement(random);
    } while (!this.isGenerator(element));
    return element;
  }

  // see Handbook of Applied Cryptography, Algorithm 4.80 and Note 4.81 (the implemented)
  // method is a mix between 4.80 and 4.81
  @Override
  protected boolean abstractIsGenerator(Element element) {
    if (!this.contains(element)) {
      return false;
    }
    for (final BigInteger prime : this.getOrderFactorization().getPrimeFactors()) {
      if (element.selfApply(this.getOrder().divide(prime)).equals(this.getIdentityElement())) {
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
   *
   * @param moduloFactorization
   * @param orderFactorization
   * @throws IllegalArgumentException if {@code moduloFactorization} or
   * {@code orderFactorization} is null
   * @throws IllegalArgumentException if the value of {@code orderFactorization}
   * does not divide phi(n)
   */
  public static GStarMod getInstance(SpecialFactorization moduloFactorization, Factorization orderFactorization) {
    GStarMod group = new GStarMod(moduloFactorization, orderFactorization);
    if (!group.getOrder().mod(orderFactorization.getValue()).equals(BigInteger.ZERO)) {
      throw new IllegalArgumentException();
    }
    return group;
  }

}
