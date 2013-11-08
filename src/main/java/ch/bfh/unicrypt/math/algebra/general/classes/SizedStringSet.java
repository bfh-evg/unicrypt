/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.general.classes;

import ch.bfh.unicrypt.math.algebra.general.abstracts.AbstractSet;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author rolfhaenni
 */
public class SizedStringSet
       extends AbstractSet<SizedStringElement> {

  private final int maxSize;

  private SizedStringSet(int maxSize) {
    this.maxSize = maxSize;
  }

  public int getMaxSize() {
    return this.maxSize;
  }

  public final SizedStringElement getElement(final String string) {
    if (string == null || string.length() > this.getMaxSize()) {
      throw new IllegalArgumentException();
    }
    return this.standardGetElement(string);
  }

  protected SizedStringElement standardGetElement(String string) {
    return new SizedStringElement(this, string) {
    };
  }

  @Override
  protected SizedStringElement abstractGetElement(BigInteger value) {
    return this.standardGetElement(new String(value.toByteArray()));
  }

  @Override
  protected BigInteger abstractGetOrder() {
    throw new UnsupportedOperationException();
    // to implement this, a charset needs to be defined in this class
  }

  @Override
  protected SizedStringElement abstractGetRandomElement(Random random) {
    throw new UnsupportedOperationException();
    // to implement this, a charset needs to be defined in this class
  }

  @Override
  protected boolean abstractContains(BigInteger value) {
    return value.signum() >= 0;
  }
  //
  // STATIC FACTORY METHODS
  //
  private static final Map<Integer, SizedStringSet> instances = new HashMap<Integer, SizedStringSet>();

  public static SizedStringSet getInstance(final int maxSize) {
    if (maxSize < 0) {
      throw new IllegalArgumentException();
    }
    SizedStringSet instance = SizedStringSet.instances.get(maxSize);
    if (instance == null) {
      instance = new SizedStringSet(maxSize);
      SizedStringSet.instances.put(maxSize, instance);
    }
    return instance;
  }

}
