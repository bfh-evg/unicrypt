/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.dualistic.abstracts;

import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialSemiRing;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.PolynomialElement;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author rolfhaenni
 */
public abstract class AbstractPolynomialElement<P extends PolynomialSemiRing> extends AbstractDualisticElement<P, PolynomialElement> implements PolynomialElement {

  Map<Integer, DualisticElement> coefficients;

  protected AbstractPolynomialElement(final P semiRing, final Map<Integer, DualisticElement> coefficients) {
    super(semiRing);
    this.coefficients = coefficients;
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
      return this.getSet().getSemiRing().getZero();
    }
    return coefficient;
  }

}
