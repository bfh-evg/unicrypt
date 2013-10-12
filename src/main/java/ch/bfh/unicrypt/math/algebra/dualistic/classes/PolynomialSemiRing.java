/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.dualistic.classes;

import java.math.BigInteger;
import java.util.Random;

import ch.bfh.unicrypt.math.algebra.dualistic.abstracts.AbstractSemiRing;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.SemiRing;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;

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

  public DualisticElement evaluate(DualisticElement element) {
    return element; // TODO
  }

  public DualisticElement getRandomElement(int maxDegree) {
    return this.getRandomElement(maxDegree, null);
  }

  public DualisticElement getRandomElement(int maxDegree, Random random) {
    return null; // TODO
  }

  //
  // The following protected methods override the standard implementation from
  // various super-classes
  //
  @Override
  protected PolynomialElement abstractApply(Element element1, Element element2) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  protected PolynomialElement abstractGetIdentityElement() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  protected PolynomialElement abstractMultiply(Element element1, Element element2) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  protected PolynomialElement abstractGetOne() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  protected BigInteger abstractGetOrder() {
    return Set.INFINITE_ORDER;
  }

  @Override
  protected PolynomialElement abstractGetElement(BigInteger value) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  protected PolynomialElement abstractGetRandomElement(Random random) {
    throw new UnsupportedOperationException();
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
