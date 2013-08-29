/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.concatenative.abstracts;

import java.math.BigInteger;

import ch.bfh.unicrypt.math.concatenative.classes.StringMonoid;
import ch.bfh.unicrypt.math.concatenative.interfaces.StringElement;
import ch.bfh.unicrypt.math.general.interfaces.Element;

/**
 *
 * @author rolfhaenni
 */
public abstract class AbstractStringElement<S extends StringMonoid, E extends StringElement> extends AbstractConcatenativeElement<S, E> implements StringElement {

  private final String string;

  protected AbstractStringElement(final S monoid, final String string) {
    super(monoid);
    this.string = string;
  }

  @Override
  public String getString() {
    return this.string;
  }

  @Override
  public int getLength() {
    return this.getString().length();
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
    return "\"" + this.getString() + "\"";
  }

}
