package ch.bfh.unicrypt.math.algebra.additive.classes;

import ch.bfh.unicrypt.math.algebra.additive.abstracts.AbstractEC;
import ch.bfh.unicrypt.math.algebra.additive.abstracts.AbstractECElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.utility.MathUtil;
import java.math.BigInteger;
import java.util.Random;

public class ECZModPrime
       extends AbstractEC<ECGroupFpElement, ZModPrime> {

  protected ECZModPrime(ZModPrime Finitefiled, ZModPrime a,
         ZModPrime b, ZModPrime gx, ZModPrime gy,
         BigInteger order, BigInteger h) {
    super(Finitefiled, a, b, gx, gy, order, h);
  }

  protected ECZModPrime(ZModPrime Finitefiled, ZModPrime a,
         ZModPrime b, BigInteger order, BigInteger h) {
    super(Finitefiled, a, b, order, h);
  }

  @Override
  protected AbstractECElement abstractApply(Element element1, Element element2) {
    ZModPrime s, rx, ry, px, py, qx, qy;
    AbstractECElement p = (AbstractECElement) element1;
    AbstractECElement q = (AbstractECElement) element2;
    px = p.getX();
    py = p.getY();
    qx = q.getX();
    qy = q.getY();

    if (p.isZero()) {
      return q;
    } else {
      if (q.isZero()) {
        return p;
      } else {
        if (p.equals(q.invert())) {
          return this.getIdentityElement();
        } else {
          if (element1.isEqual(element2)) {
            ZModPrime three = this.getFiniteField().getElement(3);
            ZModPrime two = this.getFiniteField().getElement(2);
            s = ((px.power(2).multiply(three)).apply(this.getA())).divide(py.multiply(two));
            rx = s.power(2).apply(px.multiply(two).invert());
            ry = s.multiply(px.subtract(rx)).apply(py.invert());
            return this.getElement(rx, ry);
          } else {
            s = py.apply(qy.invert()).divide(px.apply(qx.invert()));
            rx = (s.power(2).apply(px.invert()).apply(qx.invert()));
            ry = py.invert().add(s.multiply(px.apply(rx.invert())));
            return this.getElement(rx, ry);
          }
        }
      }
    }

  }

  @Override
  protected AbstractECElement abstractInvert(Element element) {
    AbstractECElement r = (AbstractECElement) element;

    if (r.isZero()) {
      return this.getIdentityElement();
    }

    return new AbstractECElement(this, r.getX(), r.getY().invert());
  }

  @Override
  protected Boolean contains(ZModPrime x, ZModPrime y) {
    y = y.power(2);
    x = x.power(3).add(x.multiply(this.getA())).add(this.getB());

    return y.equals(x);
  }

  @Override
  protected AbstractECElement getRandomElementWithoutGenerator(Random random) {
    BigInteger p = ((ZModPrime) this.getFiniteField()).getModulus();
    ZModPrime x = this.getFiniteField().getRandomElement(random);
    ZModPrime y = x.power(3).add(this.getA().multiply(x)).add(this.getB());
    boolean neg = x.getValue().mod(new BigInteger("2")).equals(BigInteger.ONE);

    while (!MathUtil.hasSqrtModPrime(y.getValue(), p)) {
      x = this.getFiniteField().getRandomElement(random);
      y = x.power(3).add(this.getA().multiply(x)).add(this.getB());
    }

    //if neg is true return solution 2(p-sqrt) of sqrtModPrime else solution 1
    if (neg) {
      y = this.getFiniteField().getElement(p.subtract(MathUtil.sqrtModPrime(y.getValue(), p)));
    } else {
      y = this.getFiniteField().getElement(MathUtil.sqrtModPrime(y.getValue(), p));
    }

    return this.getElement(x, y);
  }

  @Override
  protected boolean isValid() {
    boolean c1, c2, c3, c4, c5, c61, c62;

    ZModPrime i4 = getFiniteField().getElement(4);
    ZModPrime i27 = getFiniteField().getElement(27);
    c1 = !getA().power(3).multiply(i4).add(i27.multiply(getB().power(2))).equals(BigInteger.ZERO);

    c2 = contains(this.getDefaultGenerator());

    c3 = MathUtil.arePrime(getOrder());

    c4 = 0 >= getH().compareTo(new BigInteger("4"));

    c5 = getIdentityElement().equals(getDefaultGenerator().selfApply(getOrder()));

    c61 = true; //_> Must be corrected!
    for (int i = 1; i < 20; i++) {

    }

    c62 = !getOrder().multiply(getH()).equals(getP());

    return c1 && c2 && c3 && c4 && c5 && c61 && c62;
  }

  /**
   * Return modulus of the finite field ZModPrime
   * <p>
   * @return
   */
  protected BigInteger getP() {
    return ((ZMod) this.getFiniteField()).getModulus();
  }

  @Override
  public String toString() {
    return "y²=x³+" + getA().getValue() + "x+" + getB().getValue();
  }

  /**
   * Returns an elliptic curve over Fp y²=x³+ax+b
   * <p>
   * @param f     Finite field of type ZModPrime
   * @param a     Element of Fp respresnting a in the curve equation
   * @param b     Element of Fp respresnting b in the curve equation
   * @param order Order of the the used subgroup
   * @param h     Co-factor h*order= N -> total order of the group
   * @return
   */
  public static ECZModPrime getInstance(ZModPrime f, ZModPrime a, ZModPrime b, BigInteger order, BigInteger h) {
    return new ECZModPrime(f, a, b, order, h);
  }

  /**
   * Returns an elliptic curve over Fp y²=x³+ax+b
   * <p>
   * @param f     Finite field of type ZModPrime
   * @param a     Element of Fp respresnting a in the curve equation
   * @param b     Element of Fp respresnting b in the curve equation
   * @param gx    x-coordinate of the generator
   * @param gy    y-coordinate of the generator
   * @param order Order of the the used subgroup
   * @param h     Co-factor h*order= N -> total order of the group
   * @return
   */
  public static ECZModPrime getInstance(ZModPrime f, ZModPrime a, ZModPrime b, ZModPrime gx, ZModPrime gy, BigInteger order, BigInteger h) {
    return new ECZModPrime(f, a, b, gx, gy, order, h);
  }

}
