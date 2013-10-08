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
public class IntegerElement extends AbstractDualisticElement<Integers, IntegerElement> {

  protected IntegerElement(final Integers zPlusTimes, final BigInteger value) {
    super(zPlusTimes, value);
  }

}
