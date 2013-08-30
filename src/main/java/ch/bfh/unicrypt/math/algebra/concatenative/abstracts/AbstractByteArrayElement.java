/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.concatenative.abstracts;

import java.math.BigInteger;
import java.util.Arrays;

import ch.bfh.unicrypt.math.algebra.concatenative.classes.ByteArrayMonoid;
import ch.bfh.unicrypt.math.algebra.concatenative.interfaces.ByteArrayElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

/**
 *
 * @author rolfhaenni
 */
public abstract class AbstractByteArrayElement<S extends ByteArrayMonoid, E extends ByteArrayElement> extends AbstractConcatenativeElement<S, E> implements ByteArrayElement {

  private final byte[] bytes;

  protected AbstractByteArrayElement(final S monoid, final byte[] bytes) {
    super(monoid);
    this.bytes = bytes;
  }

  @Override
  public byte[] getBytes() {
    return this.bytes;
  }

  @Override
  public int getLength() {
    return this.getBytes().length;
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
    String str = Base64.encode(this.getBytes());
    return str.substring(0, str.length() - 1);
  }

}
