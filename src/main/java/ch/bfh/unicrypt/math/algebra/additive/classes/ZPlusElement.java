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
public class ZPlusElement extends AbstractAdditiveElement<ZPlus, ZPlusElement> {

  protected ZPlusElement(final ZPlus zPlus, final BigInteger value) {
    super(zPlus, value);
  }

}
