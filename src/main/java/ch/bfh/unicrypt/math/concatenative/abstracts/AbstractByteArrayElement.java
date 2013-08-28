/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.concatenative.abstracts;

import ch.bfh.unicrypt.math.concatenative.interfaces.ByteArrayElement;
import ch.bfh.unicrypt.math.general.interfaces.Element;
import ch.bfh.unicrypt.math.concatenative.classes.ByteArrayMonoid;
import ch.bfh.unicrypt.math.general.abstracts.AbstractElement;
import java.math.BigInteger;
import java.util.Arrays;

/**
 *
 * @author rolfhaenni
 */
public abstract class AbstractByteArrayElement extends AbstractConcatenativeElement<ByteArrayElement> implements ByteArrayElement {

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
