/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.monoid.classes;

import ch.bfh.unicrypt.math.element.abstracts.AbstractElement;
import ch.bfh.unicrypt.math.element.interfaces.Element;
import ch.bfh.unicrypt.math.monoid.abstracts.AbstractMonoid;
import ch.bfh.unicrypt.math.set.interfaces.Set;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author rolfhaenni
 */
public class ByteArrayMonoid extends AbstractMonoid<Element> {

  private ByteArrayMonoid() {
  }

  public final byte[] getByteArray(Element element) {
    if (!this.contains(element)) {
      throw new IllegalArgumentException();
    }
    return ((ByteArrayMonoid.ByteArrayElement) element).getByteArray();
  }

  public final Element getElement(final byte[] bytes) {
    if (bytes == null) {
      throw new IllegalArgumentException();
    }
    return this.standardGetElement(bytes);
  }

  protected Element standardGetElement(byte[] bytes) {
    return new ByteArrayMonoid.ByteArrayElement(this, bytes);
  }

  @Override
  protected Element abstractGetElement(BigInteger value) {
    return this.standardGetElement(value.toByteArray());
  }

  //
  // The following protected methods implement the abstract methods from
  // various super-classes
  //

  @Override
  protected Element abstractGetIdentityElement() {
    return this.standardGetElement(new byte[]{});
  }

  @Override
  protected Element abstractApply(Element element1, Element element2) {
    byte[] bytes1 = ((ByteArrayElement) element1).getByteArray();
    byte[] bytes2 = ((ByteArrayElement) element2).getByteArray();
    byte[] result = Arrays.copyOf(bytes1, bytes1.length + bytes2.length);
    System.arraycopy(bytes2, 0, result, bytes2.length, bytes2.length);
    return this.standardGetElement(result);
  }

  @Override
  protected BigInteger abstractGetOrder() {
    return Set.INFINITE_ORDER;
  }

  @Override
  protected Element abstractGetRandomElement(Random random) {
    throw new UnsupportedOperationException();
  }

  @Override
  protected boolean abstractContains(BigInteger value) {
    return value.signum() >= 0;
  }

  //
  // LOCAL ELEMENT CLASS
  //
  final private class ByteArrayElement extends AbstractElement<Element> {

    private final byte[] bytes;

    private ByteArrayElement(final Set set, final byte[] bytes) {
      super(set);
      this.bytes = bytes;
    }

    public byte[] getByteArray() {
      return this.bytes;
    }

    @Override
    protected BigInteger standardGetValue() {
      return new BigInteger(this.getByteArray());
    }

    @Override
    protected boolean standardEquals(Element element) {
      return Arrays.equals(this.getByteArray(), ((ByteArrayMonoid.ByteArrayElement) element).getByteArray());
    }

    @Override
    protected int standardHashCode() {
      return this.getByteArray().hashCode();
    }

    @Override
    public String standardToString() {
      return this.getByteArray().toString();
    }
  }
  //
  // STATIC FACTORY METHODS
  //
  private static ByteArrayMonoid instance;

  /**
   * Returns the singleton object of this class.
   *
   * @return The singleton object of this class
   */
  public static ByteArrayMonoid getInstance() {
    if (ByteArrayMonoid.instance == null) {
      ByteArrayMonoid.instance = new ByteArrayMonoid();
    }
    return ByteArrayMonoid.instance;
  }
}
