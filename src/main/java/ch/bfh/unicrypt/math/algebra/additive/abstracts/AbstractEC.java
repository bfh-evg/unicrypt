package ch.bfh.unicrypt.math.algebra.additive.abstracts;

import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.FiniteField;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.utility.MathUtil;
import java.math.BigInteger;
import java.util.Random;

public abstract class AbstractEC<E extends AbstractECElement, D extends DualisticElement>
       extends AbstractAdditiveCyclicGroup<E> {

  private FiniteField finiteField;
  private E generator;
  private D a, b;
  private BigInteger order, h;
  private final D zero = null;

  protected AbstractEC(FiniteField finiteField, D a, D b, D gx, D gy, BigInteger order, BigInteger h) {
    super();
    this.a = a;
    this.b = b;
    this.order = order;
    this.h = h;
    this.finiteField = finiteField;
    this.generator = this.getElement(gx, gy);

    if (!isValid()) {
      throw new IllegalArgumentException("Curve parameters are not valid");
    }

  }

  protected AbstractEC(FiniteField Finitefiled, D a, D b, BigInteger order, BigInteger h) {
    super();
    this.a = a;
    this.b = b;
    this.order = order;
    this.h = h;
    this.finiteField = Finitefiled;
    this.generator = this.computeGenerator();

    if (!isValid()) {
      throw new IllegalArgumentException("Curve parameters are not valid");
    }
  }

  @Override
  protected E abstractGetDefaultGenerator() {
    return this.generator;
  }

  protected E computeGenerator() {
    E e = this.getRandomElement().selfApply(this.getH());
    while (!this.isGenerator(e)) {
      e = this.getRandomElement();
    }
    return e;
  }

  @Override
  protected boolean abstractIsGenerator(Element element) {
    E e = (E) element;
    e = e.selfApply(this.getOrder());
    return MathUtil.isPrime(this.getOrder()) && e.isZero();
  }

  @Override
  protected abstract E abstractInvert(Element element);

  @Override
  protected E abstractGetIdentityElement() {
    return this.getElement(zero, zero);
  }

  @Override
  protected BigInteger abstractGetOrder() {
    return this.order;
  }

  @Override
  protected E abstractGetElement(BigInteger value) {
    if (value.equals(zero)) {
      return this.getIdentityElement();
    } else {
      BigInteger[] result = MathUtil.unpair(value);
      D x = this.getFiniteField().getElement(result[0]);
      D y = this.getFiniteField().getElement(result[1]);

      if (contains(x, y)) {
        return this.getElement(x, y);
      } else {
        throw new IllegalArgumentException("(" + x + "," + y
               + ") is not a point on the elliptic curve");
      }
    }
  }

  public E getElement(D x, D y) {
    if (x == zero && y == zero) {
      return this.getIdentityElement();
    } else {
      if (x == zero || y == zero) {
        throw new IllegalArgumentException("One coordinate is zero");
      } else {
        if (contains(x, y)) {
          return abstractGetElement(x, y);
//          return new AbstractECElement(this, x, y);
        } else {
          throw new IllegalArgumentException("Point is not an element of the curve" + this.toString());
        }
      }
    }
  }

  protected abstract E abstractGetElement(D x, D y);

  @Override
  protected E abstractGetRandomElement(Random random) {
    if (this.getDefaultGenerator() != null) {
      D r = this.getFiniteField().getRandomElement(random);
      return this.getDefaultGenerator().selfApply(r);
    } else {
      return this.getRandomElementWithoutGenerator(random);
    }

  }

  /**
   * Returns random element without knowing a generator of the group
   * <p>
   * @param random
   * @return
   */
  protected abstract E getRandomElementWithoutGenerator(Random random);

  @Override
  protected boolean abstractContains(BigInteger value) {
    BigInteger[] result = MathUtil.unpair(value);
    D x = this.getFiniteField().getElement(result[0]);
    D y = this.getFiniteField().getElement(result[1]);
    return this.contains(x, y);
  }

  /*
   * --- Abstract methods - must be implemented in concrete classes ---
   */
  protected abstract Boolean contains(D x, D y);

  protected abstract boolean isValid();

  /*
   * --- Getter methods for additional fields ---
   */
  protected FiniteField getFiniteField() {
    return finiteField;
  }

  protected D getB() {
    return b;
  }

  protected D getA() {
    return a;
  }

  protected BigInteger getH() {
    return h;
  }

}
