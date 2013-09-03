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
public class ZStarModElement extends AbstractMultiplicativeElement<ZStarMod, ZStarModElement> {

  protected ZStarModElement(final ZStarMod zStarMod, final BigInteger value) {
    super(zStarMod, value);
  }

}
