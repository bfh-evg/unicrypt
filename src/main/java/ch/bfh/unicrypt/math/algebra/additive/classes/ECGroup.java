package ch.bfh.unicrypt.math.algebra.additive.classes;

import ch.bfh.unicrypt.math.algebra.additive.abstracts.AbstractAdditiveCyclicGroup;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.FiniteField;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.utility.MathUtil;
import java.math.BigInteger;
import java.util.Random;

public abstract class ECGroup
       extends AbstractAdditiveCyclicGroup<ECGroupElement> {

  private FiniteField finiteField;
  private ECGroupElement generator;
  private DualisticElement a, b;
  private BigInteger order, h;
  private final DualisticElement zero = null;

  protected ECGroup(FiniteField Finitefiled, DualisticElement a,
         DualisticElement b, DualisticElement gx, DualisticElement gy,
         BigInteger order, BigInteger h) {
    super();
    this.a = a;
    this.b = b;
    this.order = order;
    this.h = h;
    this.finiteField = Finitefiled;
    this.generator = this.getElement(gx, gy);

    if (!isValid()) {
      throw new IllegalArgumentException("Curve parameters are not valid");
    }

  }

  protected ECGroup(FiniteField Finitefiled, DualisticElement a,
         DualisticElement b, BigInteger order, BigInteger h) {
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
  protected ECGroupElement abstractGetDefaultGenerator() {
    return this.generator;
  }

  protected ECGroupElement computeGenerator() {
    ECGroupElement e = this.getRandomElement().selfApply(this.getH());
    while (!this.isGenerator(e)) {
      e = this.getRandomElement();
    }
    return e;
  }

  @Override
  protected boolean abstractIsGenerator(Element element) {
    ECGroupElement e = (ECGroupElement) element;
    e = e.selfApply(this.getOrder());
    return MathUtil.isPrime(this.getOrder()) && e.isZero();
  }

  @Override
  protected abstract ECGroupElement abstractInvert(Element element);

  @Override
  protected ECGroupElement abstractGetIdentityElement() {
    return this.getElement(zero, zero);
  }

  @Override
  protected BigInteger abstractGetOrder() {
    return this.order;
  }

  @Override
  protected ECGroupElement abstractGetElement(BigInteger value) {
    if (value.equals(zero)) {
      return this.getIdentityElement();
    } else {
      BigInteger[] result = MathUtil.unpair(value);
      DualisticElement x = this.getFiniteField().getElement(result[0]);
      DualisticElement y = this.getFiniteField().getElement(result[1]);

      if (contains(x, y)) {
        return this.getElement(x, y);
      } else {
        throw new IllegalArgumentException("(" + x + "," + y
               + ") is not a point on the elliptic curve");
      }
    }
  }

  public ECGroupElement getElement(DualisticElement x, DualisticElement y) {
    if (x == zero && y == zero) {
      return this.getIdentityElement();
    } else {
      if (x == zero || y == zero) {
        throw new IllegalArgumentException("One coordinate is zero");
      } else {
        if (contains(x, y)) {
          return new ECGroupElement(this, x, y);
        } else {
          throw new IllegalArgumentException("Point is not an element of the curve" + this.toString());
        }
      }
    }
  }

  @Override
  protected ECGroupElement abstractGetRandomElement(Random random) {
    if (this.getDefaultGenerator() != null) {
      DualisticElement r = this.getFiniteField().getRandomElement(random);
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
  protected abstract ECGroupElement getRandomElementWithoutGenerator(Random random);

  @Override
  protected boolean abstractContains(BigInteger value) {
    BigInteger[] result = MathUtil.unpair(value);
    DualisticElement x = this.getFiniteField().getElement(result[0]);
    DualisticElement y = this.getFiniteField().getElement(result[1]);
    return this.contains(x, y);
  }

  /*
   * --- Abstract methods - must be implemented in concrete classes ---
   */
  protected abstract Boolean contains(DualisticElement x, DualisticElement y);

  protected abstract boolean isValid();

  /*
   * --- Getter methods for additional fields ---
   */
  protected FiniteField getFiniteField() {
    return finiteField;
  }

  protected DualisticElement getB() {
    return b;
  }

  protected DualisticElement getA() {
    return a;
  }

  protected BigInteger getH() {
    return h;
  }

}
