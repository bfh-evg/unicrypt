/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.general.classes;

import ch.bfh.unicrypt.math.algebra.general.abstracts.AbstractElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public class BooleanElement
       extends AbstractElement<BooleanSet, BooleanElement> {

  private final boolean bit;

  protected BooleanElement(final BooleanSet set, final boolean bit) {
    super(set);
    this.bit = bit;
  }

  public boolean getBoolean() {
    return this.bit;
  }

  @Override
  protected BigInteger standardGetValue() {
    if (this.getBoolean()) {
      return BigInteger.ONE;
    }
    return BigInteger.ZERO;
  }

  @Override
  protected boolean standardIsEqual(Element element) {
    return this.getBoolean() == ((BooleanElement) element).getBoolean();
  }

  @Override
  public String standardToStringContent() {
    return ((Boolean) this.getBoolean()).toString();
  }

}
