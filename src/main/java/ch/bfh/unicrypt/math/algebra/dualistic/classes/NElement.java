/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.dualistic.classes;

import ch.bfh.unicrypt.math.algebra.dualistic.abstracts.AbstractDualisticElement;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public class NElement
       extends AbstractDualisticElement<N, NElement> {

  protected NElement(final N n, final BigInteger value) {
    super(n, value);
  }

}
