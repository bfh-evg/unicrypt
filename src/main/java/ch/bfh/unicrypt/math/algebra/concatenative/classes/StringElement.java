/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.concatenative.classes;

import java.math.BigInteger;

import ch.bfh.unicrypt.math.algebra.concatenative.abstracts.AbstractConcatenativeElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;

/**
 *
 * @author rolfhaenni
 */
public class StringElement extends AbstractConcatenativeElement<Strings, StringElement> {

  private final String string;

  protected StringElement(final Strings monoid, final String string) {
    super(monoid);
    this.string = string;
  }

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
  public String standardToStringContent() {
    return "\"" + this.getString() + "\"";
  }

}
