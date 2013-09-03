/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.multiplicative.classes;

import java.math.BigInteger;

import ch.bfh.unicrypt.math.algebra.multiplicative.abstracts.AbstractMultiplicativeElement;

/**
 *
 * @author rolfhaenni
 */
public class NTimesElement extends AbstractMultiplicativeElement<NTimes, NTimesElement> {

  protected NTimesElement(final NTimes nTimes, final BigInteger value) {
    super(nTimes, value);
  }

}
