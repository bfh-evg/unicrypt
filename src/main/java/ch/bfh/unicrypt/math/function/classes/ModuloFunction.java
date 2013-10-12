package ch.bfh.unicrypt.math.function.classes;

import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import java.math.BigInteger;
import java.util.Random;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

/**
 * This class represents the concept of an identity function f:X->Z_2 with
 * f(x)=x mod N for all elements x in X.
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 1.0
 */
public class ModuloFunction extends AbstractFunction<Set, ZMod, ZModElement> {

  private BigInteger modulus;

  private ModuloFunction(final Set domain, final ZMod coDomain, final BigInteger modulus) {
    super(domain, coDomain);
    this.modulus = modulus;
  }

  public BigInteger getModulus() {
    return this.modulus;
  }

  @Override
  protected boolean standardEquals(Function function) {
    return this.getModulus().equals(((ModuloFunction) function).getModulus());
  }

  @Override
  protected int standardHashCode() {
    return this.getModulus().hashCode();
  }

  //
  // The following protected method implements the abstract method from {@code AbstractFunction}
  //
  @Override
  protected ZModElement abstractApply(final Element element, final Random random) {
    return this.getCoDomain().getElement(element.getValue().mod(this.getModulus()));
  }

  //
  // STATIC FACTORY METHODS
  //
  /**
   * This is the standard constructor for this class. It creates an identity
   * function for a given group.
   *
   * @param domain The given Group
   * @throws IllegalArgumentException if the group is null
   */
  public static ModuloFunction getInstance(final Set domain, BigInteger modulus) {
    return new ModuloFunction(domain, ZMod.getInstance(modulus), modulus);
  }

}
