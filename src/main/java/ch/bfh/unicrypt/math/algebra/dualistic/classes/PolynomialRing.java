/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.dualistic.classes;

import ch.bfh.unicrypt.math.algebra.additive.interfaces.AdditiveElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.Ring;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.SemiRing;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;

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
  public Element invert(Element element) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public Element applyInverse(Element element1, Element element2) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public AdditiveElement subtract(Element element1, Element element2) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public AdditiveElement minus(Element element) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
