/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.general.classes;

import ch.bfh.unicrypt.math.algebra.general.abstracts.AbstractElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.math.BigInteger;
import java.util.Arrays;

/**
 *
 * @author rolfhaenni
 */
public class SizedByteArrayElement
       extends AbstractElement<SizedByteArraySet, SizedByteArrayElement> {

  private final byte[] bytes;

  protected SizedByteArrayElement(final SizedByteArraySet set, final byte[] bytes) {
    super(set);
    this.bytes = bytes;
  }

  public byte[] getBytes() {
    return this.bytes;
  }

  public int getLength() {
    return this.getBytes().length;
  }

  @Override
  protected BigInteger standardGetValue() {
    return new BigInteger(this.getBytes());
  }

  @Override
  protected boolean standardIsEqual(Element element) {
    return Arrays.equals(this.getBytes(), ((SizedByteArrayElement) element).getBytes());
  }

  @Override
  public String standardToStringContent() {
    String str = Base64.encode(this.getBytes());
    return str.substring(0, str.length() - 1);
  }

}
