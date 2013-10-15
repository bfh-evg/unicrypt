/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.dualistic.abstracts;

import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialSemiRing;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.PolynomialElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.utility.MathUtil;
import java.math.BigInteger;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author rolfhaenni
 */
public abstract class AbstractPolynomialElement<P extends PolynomialSemiRing> extends AbstractDualisticElement<P, PolynomialElement> implements PolynomialElement {

  private Map<Integer, DualisticElement> coefficients;
  private int degree;

  protected AbstractPolynomialElement(final P semiRing, final Map<Integer, DualisticElement> coefficients) {
    super(semiRing);
    this.coefficients = coefficients;
    int degree = 0;
    for (Integer i : coefficients.keySet()) {
      degree = Math.max(degree, i);
    }
    this.degree = degree;
  }

  @Override
  public Set<Integer> getIndices() {
    return this.coefficients.keySet();
  }

  @Override
  public DualisticElement getCoefficient(int index) {
    if (index < 0) {
      throw new IllegalArgumentException();
    }
    DualisticElement coefficient = this.coefficients.get(index);
    if (coefficient == null) {
      return this.getSet().getSemiRing().getZeroElement();
    }
    return coefficient;
  }

  public DualisticElement evaluate(DualisticElement element) {
    DualisticElement result = this.getSet().getSemiRing().getZeroElement();
    for (Integer index : this.getIndices()) {
      result = result.add(this.getCoefficient(index).multiply(element.power(index)));
    }
    return result;
  }

  public int getDegree() {
    return this.degree;
  }

  @Override
  protected BigInteger standardGetValue() {
    BigInteger values[] = new BigInteger[this.getDegree() + 1];
    for (int i = 0; i <= this.getDegree(); i++) {
      DualisticElement element = this.coefficients.get(i);
      if (element == null) {
        values[i] = this.getSet().getSemiRing().getZeroElement().getValue();
      } else {
        values[i] = element.getValue();
      }
    }
    return MathUtil.elegantPairWithSize(values);
  }

  @Override
  protected boolean standardEquals(Element element) {
    return this.coefficients.equals(((AbstractPolynomialElement) element).coefficients);
  }

  @Override
  protected int standardHashCode() {
    return this.coefficients.hashCode();
  }

  @Override
  public String standardToStringContent() {
    String result = "f(x)=";
    String separator = "";
    for (Integer index : this.coefficients.keySet()) {
      DualisticElement coefficient = this.coefficients.get(index);
      if (!coefficient.isZero()) {
        result = result + separator + coefficient.getValue().toString();
        if (index == 1) {
          result = result + "X";
        }
        if (index > 1) {
          result = result + "X^" + index;
        }
        separator = "+";
      }
    }
    return result;
  }
}
