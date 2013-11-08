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
public class ZElement extends AbstractDualisticElement<Z, ZElement> {

  protected ZElement(final Z zPlusTimes, final BigInteger value) {
    super(zPlusTimes, value);
  }

}
