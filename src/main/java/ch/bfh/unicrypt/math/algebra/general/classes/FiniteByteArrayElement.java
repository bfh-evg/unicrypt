/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.general.classes;

import ch.bfh.unicrypt.math.algebra.general.abstracts.AbstractElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import java.math.BigInteger;
import java.util.Arrays;

/**
 *
 * @author rolfhaenni
 */
public class FiniteByteArrayElement
       extends AbstractElement<FiniteByteArraySet, FiniteByteArrayElement> {

  private final byte[] bytes;

  protected FiniteByteArrayElement(final FiniteByteArraySet set, final byte[] bytes) {
    super(set);
    this.bytes = bytes;
  }

  public byte[] getByteArray() {
    return this.bytes;
  }

  public int getLength() {
    return this.getByteArray().length;
  }

  @Override
  protected BigInteger standardGetValue() {
    if (this.getSet().equalLength()) {
      return new BigInteger(1, this.getByteArray());
    }
    BigInteger value = BigInteger.ZERO;
    BigInteger size = BigInteger.valueOf(256);
    for (byte b : this.getByteArray()) {
      int intValue = b & 0xFF;
      value = value.multiply(size).add(BigInteger.valueOf(intValue + 1));
    }
    return value;
  }

  @Override
  protected boolean standardIsEqual(Element element) {
    return Arrays.equals(this.getByteArray(), ((FiniteByteArrayElement) element).getByteArray());
  }

  @Override
  public String standardToStringContent() {
    String str = "";
    String delimiter = "";
    for (int i = 0; i < this.getLength(); i++) {
      str = str + delimiter + String.format("%02X", BigInteger.valueOf(this.getByteArray()[i] & 0xFF));
      delimiter = "|";
    }
    return "\"" + str + "\"";
  }

}
