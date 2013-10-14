/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.dualistic.classes;

import ch.bfh.unicrypt.math.algebra.dualistic.abstracts.AbstractPolynomialElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.PolynomialElement;
import java.math.BigInteger;
import java.util.Random;

import ch.bfh.unicrypt.math.algebra.dualistic.abstracts.AbstractSemiRing;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.SemiRing;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public class PolynomialSemiRing extends AbstractSemiRing<PolynomialElement> {

  private SemiRing semiRing;

  protected PolynomialSemiRing(SemiRing semiRing) {
    this.semiRing = semiRing;
  }

  public SemiRing getSemiRing() {
    return this.semiRing;
  }

  public PolynomialElement getRandomElement(int degree) {
    return this.getRandomElement(degree, null);
  }

  public PolynomialElement getRandomElement(int degree, Random random) {
    if (degree < 0) {
      throw new IllegalArgumentException();
    }
    Map<Integer, DualisticElement> coefficients = new HashMap<Integer, DualisticElement>();
    for (int i = 0; i <= degree; i++) {
      DualisticElement coefficient = this.getSemiRing().getRandomElement(random);
      if (!coefficient.isZero()) {
        coefficients.put(i, coefficient);
      }
    }
    return abstractGetElement(coefficients);
  }

  //
  // The following protected methods override the standard implementation from
  // various super-classes
  //
  @Override
  public boolean standardEquals(final Set set) {
    final PolynomialSemiRing other = (PolynomialSemiRing) set;
    return this.getSemiRing().equals(other.getSemiRing());
  }

  @Override
  public int standardHashCode() {
    return this.getSemiRing().hashCode();
  }

  @Override
  public String standardToStringContent() {
    return this.getSemiRing().toString();
  }

  //
  // The following protected methods override the standard implementation from
  // various super-classes
  //
  @Override
  protected PolynomialElement abstractApply(Element element1, Element element2) {
    Map<Integer, DualisticElement> newCoefficients = new HashMap<Integer, DualisticElement>();
    PolynomialElement poly1 = (PolynomialElement) element1;
    PolynomialElement poly2 = (PolynomialElement) element2;
    for (Integer i : poly1.getIndices()) {
      newCoefficients.put(i, poly1.getCoefficient(i));
    }
    for (Integer i : poly2.getIndices()) {
      DualisticElement coefficient = newCoefficients.get(i);
      if (coefficient == null) {
        newCoefficients.put(i, poly1.getCoefficient(i));
      } else {
        newCoefficients.put(i, coefficient.add(poly2.getCoefficient(i)));
      }
    }
    return abstractGetElement(newCoefficients);
  }

  @Override
  protected PolynomialElement abstractGetIdentityElement() {
    return abstractGetElement(new HashMap<Integer, DualisticElement>());
  }

  @Override
  protected PolynomialElement abstractMultiply(Element element1, Element element2) {
    Map<Integer, DualisticElement> newCoefficients = new HashMap<Integer, DualisticElement>();
    PolynomialElement poly1 = (PolynomialElement) element1;
    PolynomialElement poly2 = (PolynomialElement) element2;
    for (Integer i : poly1.getIndices()) {
      for (Integer j : poly2.getIndices()) {
        Integer k = i + j;
        DualisticElement coefficient = poly1.getCoefficient(i).add(poly2.getCoefficient(j));
        DualisticElement newCoefficient = newCoefficients.get(k);
        if (newCoefficient == null) {
          newCoefficients.put(k, coefficient);
        } else {
          newCoefficients.put(k, newCoefficient.add(coefficient));
        }
      }
    }
    return abstractGetElement(newCoefficients);
  }

  @Override
  protected PolynomialElement abstractGetOne() {
    Map<Integer, DualisticElement> coefficients = new HashMap<Integer, DualisticElement>();
    coefficients.put(0, this.getSemiRing().getOneElement());
    return abstractGetElement(coefficients);
  }

  @Override
  protected BigInteger abstractGetOrder() {
    return Set.INFINITE_ORDER;
  }

  PolynomialElement abstractGetElement(Map<Integer, DualisticElement> coefficients) {
    return new AbstractPolynomialElement<PolynomialSemiRing>(this, coefficients) {
    };
  }

  @Override
  protected PolynomialElement abstractGetElement(BigInteger value) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  protected PolynomialElement abstractGetRandomElement(Random random) {
    throw new UnsupportedOperationException("Not possible for infinite order.");
  }

  @Override
  protected boolean abstractContains(BigInteger value) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  //
  // STATIC FACTORY METHODS
  //
  public static PolynomialSemiRing getInstance(SemiRing semiRing) {
    if (semiRing == null) {
      throw new IllegalArgumentException();
    }
    return new PolynomialSemiRing(semiRing);
  }

}
