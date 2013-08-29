/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.general.abstracts;

import ch.bfh.unicrypt.math.general.interfaces.BooleanElement;
import ch.bfh.unicrypt.math.general.classes.BooleanSet;

/**
 *
 * @author rolfhaenni
 */
public abstract class AbstractBooleanElement<S extends BooleanSet, E extends BooleanElement> extends AbstractElement<S, E> implements BooleanElement {

  private final boolean bit;

  protected AbstractBooleanElement(final S set, final boolean bit) {
    super(set);
    this.bit = bit;
  }

  @Override
  public boolean getBoolean() {
    return this.bit;
  }

}
