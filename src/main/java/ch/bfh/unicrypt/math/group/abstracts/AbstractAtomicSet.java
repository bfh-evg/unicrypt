/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.group.abstracts;

import ch.bfh.unicrypt.math.element.abstracts.AtomicElement;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public abstract class AbstractAtomicSet extends AbstractSet<AtomicElement> {

  protected AtomicElement abstractGetElement(BigInteger value) {
    return new AtomicElement(this, value) {};
  }

}
