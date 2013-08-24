/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.element.classes;

import ch.bfh.unicrypt.math.element.abstracts.AbstractAdditiveElement;
import ch.bfh.unicrypt.math.element.abstracts.AbstractElement;
import ch.bfh.unicrypt.math.group.interfaces.Set;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public class AdditiveAtomicElement extends AbstractAdditiveElement<AdditiveAtomicElement> {

  protected AdditiveAtomicElement(final Set set) {
    super(set);
  }

  protected AdditiveAtomicElement(final Set set, final BigInteger value) {
    super(set);
    if (!set.contains(value)) {
      throw new IllegalArgumentException();
    }
    this.value = value;
  }

}
