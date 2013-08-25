/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.element.classes;

import ch.bfh.unicrypt.math.element.abstracts.AbstractAtomicElement;
import ch.bfh.unicrypt.math.element.interfaces.Element;
import ch.bfh.unicrypt.math.group.interfaces.Set;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public class AtomicElement extends AbstractAtomicElement<AtomicElement> {

  protected AtomicElement(final Set set) {
    super(set);
  }

  protected AtomicElement(final Set set, final BigInteger value) {
    super(set, value);
  }

}
