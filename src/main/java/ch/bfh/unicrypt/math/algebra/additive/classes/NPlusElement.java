/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.additive.classes;

import ch.bfh.unicrypt.math.algebra.additive.abstracts.AbstractAdditiveElement;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public class NPlusElement extends AbstractAdditiveElement<NPlus, NPlusElement> {

  protected NPlusElement(final NPlus nPlus, final BigInteger value) {
    super(nPlus, value);
  }

}
