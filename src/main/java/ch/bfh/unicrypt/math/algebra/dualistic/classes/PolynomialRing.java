/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.dualistic.classes;

import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.Ring;
import ch.bfh.unicrypt.math.algebra.general.classes.PermutationGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author rolfhaenni
 */
public class PolynomialRing extends PolynomialSemiRing implements Ring {

  protected PolynomialRing(Ring ring) {
    super(ring);
  }

  public Ring getRing() {
    return (Ring) super.getSemiRing();
  }

  //
  // The following protected methods override the standard implementation from
  // various super-classes
  //
  @Override
  public PolynomialElement invert(Element element) {
    Map<Integer, DualisticElement> coefficients = new HashMap<Integer, DualisticElement>();
    PolynomialElement poly = (PolynomialElement) element;
    for (Integer i : poly.getIndices()) {
      coefficients.put(i, poly.getCoefficient(i).minus());
    }
    return abstractGetElement(coefficients);
  }

  @Override
  public PolynomialElement applyInverse(Element element1, Element element2) {
    return this.apply(element1, this.invert(element2));
  }

  @Override
  public PolynomialElement subtract(Element element1, Element element2) {
    return this.applyInverse(element1, element2);
  }

  @Override
  public PolynomialElement minus(Element element) {
    return this.invert(element);
  }

  //
  // STATIC FACTORY METHODS
  //
  public static PolynomialRing getInstance(Ring ring) {
    if (ring == null) {
      throw new IllegalArgumentException();
    }
    return new PolynomialRing(ring);
  }

}
