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
public abstract class AbstractAtomicElement<E extends AbstractAtomicElement> extends AbstractElement<E> {

  protected AbstractAtomicElement(final Set set) {
    super(set);
  }

  protected AbstractAtomicElement(final Set set, final BigInteger value) {
    super(set, value);
  }

}
