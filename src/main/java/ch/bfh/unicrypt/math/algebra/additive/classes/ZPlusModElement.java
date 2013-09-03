/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.additive.classes;

import java.math.BigInteger;

import ch.bfh.unicrypt.math.algebra.additive.abstracts.AbstractAdditiveElement;

/**
 *
 * @author rolfhaenni
 */
public class ZPlusModElement extends AbstractAdditiveElement<ZPlusMod, ZPlusModElement> {

  protected ZPlusModElement(final ZPlusMod zPlusMod, final BigInteger value) {
    super(zPlusMod, value);
  }

}
