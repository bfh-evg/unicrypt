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
public class NPlusTimesElement extends AbstractDualisticElement<NPlusTimes, NPlusTimesElement> {

  protected NPlusTimesElement(final NPlusTimes nPlusTimes, final BigInteger value) {
    super(nPlusTimes, value);
  }

}
