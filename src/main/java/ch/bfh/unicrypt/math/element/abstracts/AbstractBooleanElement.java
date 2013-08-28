/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.element.abstracts;

import ch.bfh.unicrypt.math.element.interfaces.BooleanElement;
import ch.bfh.unicrypt.math.set.classes.BooleanSet;

/**
 *
 * @author rolfhaenni
 */
public abstract class AbstractBooleanElement extends AbstractElement<BooleanElement> implements BooleanElement {

  private final boolean bit;

  protected AbstractBooleanElement(final BooleanSet set, final boolean bit) {
    super(set);
    this.bit = bit;
  }

  @Override
  public boolean getBoolean() {
    return this.bit;
  }

}
