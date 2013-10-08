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
public class ZModElement extends AbstractDualisticElement<ZMod, ZModElement> {

  protected ZModElement(final ZMod zPlusTimesMod, final BigInteger value) {
    super(zPlusTimesMod, value);
  }

}
