/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.dualistic.abstracts;

import ch.bfh.unicrypt.math.algebra.additive.abstracts.AbstractAdditiveElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.Ring;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public abstract class AbstractDualisticElement<S extends Ring, E extends DualisticElement> extends AbstractAdditiveElement<S, E> implements DualisticElement {

  protected AbstractDualisticElement(final S ring) {
    super(ring);
  }

  protected AbstractDualisticElement(final S ring, final BigInteger value) {
    super(ring);
    if (!ring.contains(value)) {
      throw new IllegalArgumentException();
    }
    this.value = value;
  }

}
