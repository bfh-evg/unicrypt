package ch.bfh.unicrypt.math.group.classes;

import java.math.BigInteger;
import java.util.Random;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.group.interfaces.DDHGroup;
import ch.bfh.unicrypt.math.group.interfaces.MultiplicativeCyclicGroup;
import ch.bfh.unicrypt.math.helper.Factorization;
import ch.bfh.unicrypt.math.helper.SpecialFactorization;
import ch.bfh.unicrypt.math.utility.MathUtil;
import ch.bfh.unicrypt.math.utility.RandomUtil;

/**
 * This interface represents the concept of a sub-group G_m (of order m) of a cyclic group of integers Z*_n 
 * with the operation of multiplication modulo n. For Z*_n to be cyclic, n must be 2, 4, p^e, or 2p^e, where 
 * p>2 is prime and e>0. The actual sub-group depends on the given set of prime factors of the order phi(n) 
 * of Z*_n, where phi(n) is the Euler totient function. The order m=|G_m| is the product of all given 
 * prime factors of phi(n). If all prime factors of phi(n) are given, which implies m=phi(n), then 
 * G_m is the parent group Z*_n. 
 * 
 * @see "Handbook of Applied Cryptography,  Fact 2.132"
 * @see "Handbook of Applied Cryptography,  Definition 2.100"
 * @see "Handbook of Applied Cryptography,  Definition 2.166"
 * 
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public class GStarMod extends ZStarMod implements MultiplicativeCyclicGroup, DDHGroup {

  // This class should inherit from both ZStarModClass and AbstractMultiplicativeCyclicGroup, but since
  // Java does not support multiple inheritance, we copy some code from AbstractMultiplicativeCyclicGroup

  private static final long serialVersionUID = 1L;

  private final Factorization orderFactorization;
  private Element defaultGenerator;

  protected GStarMod(SpecialFactorization moduloFactorization, Factorization orderFactorization) {
    super(moduloFactorization);
    this.orderFactorization = orderFactorization;
  }

  /**
   * Returns the quotient k=phi(n)/m of the orders of the two involved groups. 
   * @return The quotient of the two orders.
   */
  public BigInteger getOrderQuotient() {
    return this.getSuperGroup().getOrder().divide(this.getOrder());
  }

  /**
   * Returns prime factorization of the group order phi(n) of Z*_n. 
   * @return The prime factorization of the group order
   */
  public Factorization getOrderFactorization() {
    return this.orderFactorization;
  }

  @Override
  public Element getDefaultGenerator() {
    if (this.defaultGenerator == null) {
      this.defaultGenerator = this.computeDefaultGenerator();
    }
    return this.defaultGenerator;
  }

  @Override
  public Element getRandomGenerator() {
    return this.getRandomGenerator(null);
  }


  // see Handbook of Applied Cryptography, Algorithm 4.80 and Note 4.81
  @Override
  public Element getRandomGenerator(final Random random) {
    Element element;
    do {
      element = this.getRandomElement(random);
    } while (!this.isGenerator(element));
    return element;
  }

  // see Handbook of Applied Cryptography, Algorithm 4.80 and Note 4.81 (the implemented)
  // method is a mix between 4.80 and 4.81
  @Override
  public boolean isGenerator(final Element element) {
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

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = (prime * result) + this.getOrder().hashCode();
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (!super.equals(obj)) {
      return false;
    }
    if (obj instanceof GStarMod) {
      final GStarMod other = (GStarMod) obj;
      return this.getOrder().equals(other.getOrder());
    }
    return false;
  }

  @Override
  public String toString() {
    return "" + this.getClass().getSimpleName() + "[modulo=" + this.getModulus() + ",order=" + this.getOrder() + "]";
  }

  //
  // The following protected methods override the standard implementation from {@code AbstractGroup}
  //

  // The following method is not very nice, because it copies some code from the parent class. However, it is not
  // possible to call super.createRandomElement(), this would be incorrect. 
  // VERSION2: a subgroup should have access to the parent group to call the parent method from there.
  @Override
  protected Element abstractGetRandomElement(final Random random) {
    if (this.getOrder().compareTo(this.getOrderQuotient()) > 0) { // choose between the faster method
      // Method 1
      BigInteger randomValue;
      do {
        randomValue = RandomUtil.createRandomBigInteger(BigInteger.ONE, this.getModulus().subtract(BigInteger.ONE), random);
      } while (!randomValue.gcd(this.getModulus()).equals(BigInteger.ONE));
      return this.abstractGetElement(randomValue.modPow(this.getOrderQuotient(), this.getModulus()));
    }
    // Method 2
    return this.getDefaultGenerator().selfApply(RandomUtil.createRandomBigInteger(BigInteger.ONE, this.getOrder(), random));
  }

  @Override
  protected boolean abstractContains(final BigInteger value) {
    return super.contains(value) && value.mod(this.getModulus()).modPow(this.getOrder(), this.getModulus()).equals(BigInteger.ONE);
  }

  protected Element computeDefaultGenerator() {
    BigInteger alpha = BigInteger.ZERO;
    Element element;
    do {
      do {
        alpha = alpha.add(BigInteger.ONE);
      } while (!alpha.gcd(this.getModulus()).equals(BigInteger.ONE));
      element = this.abstractGetElement(alpha.modPow(this.getOrderQuotient(), this.getModulus()));
    } while (!this.isGenerator(element)); // this test could be skipped for a prime order
    return element;
  }

  //
  // STATIC FACTORY METHODS
  //

  /**
   * This is the general static factory method for this class. 
   * @param moduloFactorization
   * @param orderFactorization
   * @throws IllegalArgumentException if {@code moduloFactorization} or {@code orderFactorization} is null
   * @throws IllegalArgumentException if the value of {@code orderFactorization} does not divide phi(n)
   */
  public static GStarMod createInstance(SpecialFactorization moduloFactorization, Factorization orderFactorization) {
    GStarMod group = new GStarMod(moduloFactorization, orderFactorization);
    if (!group.getOrder().mod(orderFactorization.getValue()).equals(BigInteger.ZERO)) {
      throw new IllegalArgumentException();
    }
    return group;
  }

  /**
   * This is a special case of the general constructor where {@code exponent} and {@code doubling} is not specified
   * @see #GStarMod.Factory.createInstance(BigInteger, int, boolean, Factorization)
   */
  public static GStarMod createInstance(final BigInteger prime, final Factorization orderFactorization) {
    return GStarMod.createInstance(prime, 1, false, orderFactorization);
  }

  /**
   * This is a special case of the general constructor where {@code exponent} and {@code orderExponents} are not specified
   * @see #GStarMod.Factory.createInstance(BigInteger, int, boolean, Factorization)
   */
  public static GStarMod createInstance(final BigInteger prime, final boolean doubling, final Factorization orderFactorization) {
    return GStarMod.createInstance(prime, 1, doubling, orderFactorization);
  }

  /**
   * This is a special case of the general constructor where {@code doubling} and {@code orderExponents} are not specified
   * @see #GStarMod.Factory.createInstance(BigInteger, int, boolean, Factorization)
   */
  public static GStarMod createInstance(final BigInteger prime, final int exponent, final Factorization orderFactorization) {
    return GStarMod.createInstance(prime, exponent, false, orderFactorization);
  }

  /**
   * This is the general static factory method for this class. Returns a new instance for the general case n=p^k or n=2p^k, 
   * where p is prime and e>=1 (and e=1 for p=2). 
   * @param prime The given prime number p
   * @param exponent The given exponent e
   * @param doubling A Boolean value indicating whether n=p^k ({@code false}) or n=2p^k ({@code true}) 
   * @param orderFactorization The given prime factorization of phi(n)
   * @throws IllegalArgumentException if {@code prime} is null or not prime
   * @throws IllegalArgumentException if {@code exponent<1}
   * @throws IllegalArgumentException if {@code prime=2} and {@code exponent>1}
   * @throws IllegalArgumentException if {@code orderPrimeFactors} is null, contains null or values that are not prime 
   * @throws IllegalArgumentException if {@code orderExponents} is null or contains values<1
   * @throws IllegalArgumentException if the lengths of {@code orderPrimeFactors} and {@code orderExponents} are different
   * @throws IllegalArgumentException  if the product-of-powers of {@code orderPrimeFactors} and {@code orderExponents} does not divide the group order
   */
  public static GStarMod createInstance(final BigInteger prime, final int exponent, final boolean doubling, final Factorization orderFactorization) {
    return GStarMod.createInstance(new SpecialFactorization(prime, exponent, doubling), orderFactorization);
  }

  /**
   * This is the general static factory method of this class, we It creates and return an instance of this class
   * for a given safe prime p. The second argument {@code subGroup} indicates whether the subgroup
   * of order q=(p-1)/2 is taken or the full group Z*_p.
   * @param safePrime The given safe prime
   * @param subGroup The Boolean indicator
   * @throws IllegalArgumentException if safePrime is null or not a safe prime
   */
  public static GStarMod createInstance(final BigInteger safePrime, final boolean subGroup) {
    if (safePrime == null || !MathUtil.isSavePrime(safePrime)) {
      throw new IllegalArgumentException();
    }
    final BigInteger primeFactor = safePrime.subtract(BigInteger.ONE).divide(BigInteger.valueOf(2));
    if (subGroup) {
      return GStarMod.createInstance(safePrime, new Factorization(new BigInteger[]{primeFactor}));
    }
    return  GStarMod.createInstance(safePrime, new Factorization(new BigInteger[]{primeFactor, BigInteger.valueOf(2)}));
  }

  /**
   * This is a special case of the general constructor where {@code subGroup} is true.
   * @see #GStarSave.Factory.createInstance(BigInteger, boolean)
   */
  public static GStarMod createInstance(final BigInteger safePrime) {
    return GStarMod.createInstance(safePrime, true);
  }


}
