/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.element.abstracts;

import ch.bfh.unicrypt.math.element.interfaces.ByteArrayElement;
import ch.bfh.unicrypt.math.element.interfaces.Element;
import ch.bfh.unicrypt.math.monoid.classes.ByteArrayMonoid;
import java.math.BigInteger;
import java.util.Arrays;

/**
 *
 * @author rolfhaenni
 */
public abstract class AbstractByteArrayElement extends AbstractElement<ByteArrayElement> implements ByteArrayElement {

  private final byte[] bytes;

  protected AbstractByteArrayElement(final ByteArrayMonoid monoid, final byte[] bytes) {
    super(monoid);
    this.bytes = bytes;
  }

  @Override
  public byte[] getBytes() {
    return this.bytes;
  }

  @Override
  protected BigInteger standardGetValue() {
    return new BigInteger(this.getBytes());
  }

  @Override
  protected boolean standardEquals(Element element) {
    return Arrays.equals(this.getBytes(), ((ByteArrayElement) element).getBytes());
  }

  @Override
  protected int standardHashCode() {
    return this.getBytes().hashCode();
  }

  @Override
  public String standardToString() {
    return this.getBytes().toString();
  }
}
