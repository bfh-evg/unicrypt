/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.multiplicative.classes;

import ch.bfh.unicrypt.math.algebra.multiplicative.abstracts.AbstractMultiplicativeElement;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public class ZTimesElement extends AbstractMultiplicativeElement<ZTimes, ZTimesElement> {

  protected ZTimesElement(final ZTimes zTimes, final BigInteger value) {
    super(zTimes, value);
  }

}
