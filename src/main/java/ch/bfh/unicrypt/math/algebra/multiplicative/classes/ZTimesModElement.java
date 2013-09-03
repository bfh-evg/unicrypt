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
public class ZTimesModElement extends AbstractMultiplicativeElement<ZTimesMod, ZTimesModElement> {

  protected ZTimesModElement(final ZTimesMod zTimesMod, final BigInteger value) {
    super(zTimesMod, value);
  }

}
