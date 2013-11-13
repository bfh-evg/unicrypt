/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.general.classes;

import ch.bfh.unicrypt.math.algebra.general.abstracts.AbstractSet;
import ch.bfh.unicrypt.math.utility.RandomUtil;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author rolfhaenni
 */
public class FiniteByteArraySet
       extends AbstractSet<FiniteByteArrayElement> {

  private final int length;
  private final boolean equalLength;

  private FiniteByteArraySet(int length, boolean equalLength) {
    this.length = length;
    this.equalLength = equalLength;
  }

  public int getLength() {
    return this.length;
  }

  public boolean equalLength() {
    return this.equalLength;
  }

  public final FiniteByteArrayElement getElement(final byte[] bytes) {
    if (bytes == null || bytes.length > this.getLength() || (this.equalLength() && bytes.length < this.getLength())) {
      throw new IllegalArgumentException();
    }
    return this.standardGetElement(bytes);
  }

  protected FiniteByteArrayElement standardGetElement(byte[] bytes) {
    return new FiniteByteArrayElement(this, bytes);
  }

  @Override
  protected FiniteByteArrayElement abstractGetElement(BigInteger value) {
    byte[] result = new byte[this.getLength()];
    Arrays.fill(result, (byte) 0);
    if (this.equalLength()) {
      byte[] bytes = value.toByteArray();
      int offset = (bytes[0] == 0) ? 1 : 0;
      System.arraycopy(bytes, offset, result, this.getLength() - bytes.length + offset, bytes.length - offset);
      return this.standardGetElement(result);
    } else {
      BigInteger size = BigInteger.valueOf(256);
      int i = this.getLength();
      while (!value.equals(BigInteger.ZERO)) {
        i--;
        value = value.subtract(BigInteger.ONE);
        result[i] = value.mod(size).byteValue();
        value = value.divide(size);
      }
      return this.standardGetElement(Arrays.copyOfRange(result, i, this.getLength()));
    }
  }

  @Override
  protected BigInteger abstractGetOrder() {
    BigInteger size = BigInteger.valueOf(256);
    if (this.equalLength) {
      return size.pow(this.getLength());
    }
    BigInteger order = BigInteger.ONE;
    for (int i = 0; i < this.getLength(); i++) {
      order = order.multiply(size).add(BigInteger.ONE);
    }
    return order;
  }

  @Override
  protected FiniteByteArrayElement abstractGetRandomElement(Random random) {
    return this.abstractGetElement(RandomUtil.getRandomBigInteger(this.getOrder().subtract(BigInteger.ONE), random));
  }

  @Override
  protected boolean abstractContains(BigInteger value
  ) {
    return (value.signum() >= 0) && (value.compareTo(this.getOrder()) < 0);
  }

  //
  // STATIC FACTORY METHODS
  //
  public static FiniteByteArraySet getInstance(final int length) {
    return FiniteByteArraySet.getInstance(length, false);
  }

  public static FiniteByteArraySet getInstance(final int length, final boolean equalLength) {
    if (length < 0) {
      throw new IllegalArgumentException();
    }
    return new FiniteByteArraySet(length, equalLength);
  }

}
