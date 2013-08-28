/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.element.abstracts;

import ch.bfh.unicrypt.math.element.interfaces.Element;
import ch.bfh.unicrypt.math.element.interfaces.StringElement;
import ch.bfh.unicrypt.math.monoid.classes.StringMonoid;
import ch.bfh.unicrypt.math.set.interfaces.Set;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public abstract class AbstractStringElement extends AbstractElement<StringElement> implements StringElement {

  private final String string;

  protected AbstractStringElement(final StringMonoid monoid, final String string) {
    super(monoid);
    this.string = string;
  }

  @Override
  public String getString() {
    return this.string;
  }

  @Override
  protected BigInteger standardGetValue() {
    return new BigInteger(this.getString().getBytes());
  }

  @Override
  protected boolean standardEquals(Element element) {
    return this.getString().equals(((StringElement) element).getString());
  }

  @Override
  protected int standardHashCode() {
    return this.getString().hashCode();
  }

  @Override
  public String standardToString() {
    return this.getString();
  }
}
