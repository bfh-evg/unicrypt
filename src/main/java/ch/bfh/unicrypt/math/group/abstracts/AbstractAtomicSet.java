/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.group.abstracts;

import ch.bfh.unicrypt.math.element.classes.AtomicElement;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public abstract class AbstractAtomicSet extends AbstractSet<AtomicElement> {

  @Override
  protected AtomicElement abstractGetElement(BigInteger value) {
    return new AtomicElement(this, value) {};
  }

}
