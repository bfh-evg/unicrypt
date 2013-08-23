/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.element.abstracts;

import ch.bfh.unicrypt.math.group.interfaces.Set;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public abstract class AtomicElement extends AbstractElement<AtomicElement> {

  protected AtomicElement(final Set set) {
    super(set);
  }

  protected AtomicElement(final Set set, final BigInteger value) {
    super(set);
        if (!set.contains(value)) {
      throw new IllegalArgumentException();
    }
    this.value = value;
  }

}
