/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.dualistic.classes;

import ch.bfh.unicrypt.math.algebra.dualistic.abstracts.AbstractDualisticElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.utility.MathUtil;
import java.math.BigInteger;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author rolfhaenni
 */
public class PolynomialElement
       extends AbstractDualisticElement<PolynomialSemiRing, PolynomialElement> {

  private Map<Integer, DualisticElement> coefficients;
  private int degree;

  protected PolynomialElement(final PolynomialSemiRing semiRing, final Map<Integer, DualisticElement> coefficients) {
    super(semiRing);
    this.coefficients = coefficients;
    this.degree = 0;
    for (Integer i : this.getIndices()) {
      this.degree = Math.max(this.degree, i);
    }
  }

  public final Set<Integer> getIndices() {
    return this.coefficients.keySet();
  }

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
    return MathUtil.pairWithSize(values);
  }

  @Override
  protected boolean standardIsEqual(Element element) {
    return this.coefficients.equals(((PolynomialElement) element).coefficients);
  }

  @Override
  public String standardToStringContent() {
    String result = "f(x)=";
    if (this.getDegree() == 0) {
      return result + this.getSet().getSemiRing().getZeroElement().getValue();
    }
    String separator = "";
    for (Integer index : this.getIndices()) {
      DualisticElement coefficient = this.coefficients.get(index);
      if (!coefficient.isZero() || this.getDegree() == 0) {
        result = result + separator;
        if (!coefficient.isOne() || index == 0) {
          result = result + coefficient.getValue().toString();
        }
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
