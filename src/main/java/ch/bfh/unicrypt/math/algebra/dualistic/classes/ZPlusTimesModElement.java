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
public class ZPlusTimesModElement extends AbstractDualisticElement<ZPlusTimesMod, ZPlusTimesModElement> {

  protected ZPlusTimesModElement(final ZPlusTimesMod zPlusTimesMod, final BigInteger value) {
    super(zPlusTimesMod, value);
  }

}
