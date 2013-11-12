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
public class GStarModElement
       extends AbstractMultiplicativeElement<GStarMod, GStarModElement> {

  protected GStarModElement(final GStarMod gStarMod, final BigInteger value) {
    super(gStarMod, value);
  }

}
