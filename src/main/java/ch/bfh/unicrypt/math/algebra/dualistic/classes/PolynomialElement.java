/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.dualistic.classes;

import java.math.BigInteger;

import ch.bfh.unicrypt.math.algebra.dualistic.abstracts.AbstractDualisticElement;

/**
 *
 * @author rolfhaenni
 */
public class PolynomialElement extends AbstractDualisticElement<PolynomialSemiRing, PolynomialElement> {

  protected PolynomialElement(final PolynomialSemiRing semiRing, final BigInteger value) {
    super(semiRing, value);
  }

}
