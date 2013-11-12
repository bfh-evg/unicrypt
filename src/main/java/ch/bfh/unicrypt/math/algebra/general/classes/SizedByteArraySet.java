/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.general.classes;

import ch.bfh.unicrypt.math.algebra.general.abstracts.AbstractSet;
import ch.bfh.unicrypt.math.utility.RandomUtil;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author rolfhaenni
 */
public class SizedByteArraySet
       extends AbstractSet<SizedByteArrayElement> {

  private final int maxSize;

  private SizedByteArraySet(int maxSize) {
    this.maxSize = maxSize;
  }

  public int getMaxSize() {
    return this.maxSize;
  }

  public final SizedByteArrayElement getElement(final byte[] bytes) {
    if (bytes == null || bytes.length > this.getMaxSize()) {
      throw new IllegalArgumentException();
    }
    return this.standardGetElement(bytes);
  }

  protected SizedByteArrayElement standardGetElement(byte[] bytes) {
    return new SizedByteArrayElement(this, bytes);
  }

  @Override
  protected SizedByteArrayElement abstractGetElement(BigInteger value) {
    return this.standardGetElement(value.toByteArray());
  }

  @Override
  protected BigInteger abstractGetOrder() {
    return BigInteger.valueOf(256).multiply(BigInteger.valueOf(this.getMaxSize()));
  }

  @Override
  protected SizedByteArrayElement abstractGetRandomElement(Random random) {
    return this.abstractGetElement(RandomUtil.getRandomBigInteger(8 * this.getMaxSize(), random));
  }

  @Override
  protected boolean abstractContains(BigInteger value) {
    return value.signum() >= 0;
  }
  //
  // STATIC FACTORY METHODS
  //
  private static final Map<Integer, SizedByteArraySet> instances = new HashMap<Integer, SizedByteArraySet>();

  public static SizedByteArraySet getInstance(final int maxSize) {
    if (maxSize < 0) {
      throw new IllegalArgumentException();
    }
    SizedByteArraySet instance = SizedByteArraySet.instances.get(maxSize);
    if (instance == null) {
      instance = new SizedByteArraySet(maxSize);
      SizedByteArraySet.instances.put(maxSize, instance);
    }
    return instance;
  }

}
