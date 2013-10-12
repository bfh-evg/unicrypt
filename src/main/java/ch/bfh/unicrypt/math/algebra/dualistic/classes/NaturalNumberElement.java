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
public class NaturalNumberElement extends AbstractDualisticElement<NaturalNumbers, NaturalNumberElement> {

  protected NaturalNumberElement(final NaturalNumbers nPlusTimes, final BigInteger value) {
    super(nPlusTimes, value);
  }

}
