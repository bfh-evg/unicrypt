/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.general.classes;

import ch.bfh.unicrypt.math.algebra.general.abstracts.AbstractElement;

/**
 *
 * @author rolfhaenni
 */
public class BooleanElement extends AbstractElement<BooleanSet, BooleanElement> {

  private final boolean bit;

  protected BooleanElement(final BooleanSet set, final boolean bit) {
    super(set);
    this.bit = bit;
  }

  public boolean getBoolean() {
    return this.bit;
  }

}
