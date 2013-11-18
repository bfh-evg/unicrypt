/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.keygenerator.classes;

import ch.bfh.unicrypt.crypto.keygenerator.abstracts.AbstractKeyGenerator;
import ch.bfh.unicrypt.math.algebra.general.classes.FiniteByteArrayElement;
import ch.bfh.unicrypt.math.algebra.general.classes.FiniteByteArraySet;

/**
 *
 * @author rolfhaenni
 */
public class OneTimePadKeyGenerator
       extends AbstractKeyGenerator<FiniteByteArraySet, FiniteByteArrayElement> {

  protected OneTimePadKeyGenerator(FiniteByteArraySet finiteByteArraySet) {
    super(finiteByteArraySet);
  }

  public static OneTimePadKeyGenerator getInstance(int lenght) {
    if (lenght < 0) {
      throw new IllegalArgumentException();
    }
    return new OneTimePadKeyGenerator(FiniteByteArraySet.getInstance(lenght, true));
  }

  public static OneTimePadKeyGenerator getInstance(FiniteByteArraySet finiteByteArraySet) {
    if (!finiteByteArraySet.equalLength()) {
      throw new IllegalArgumentException();
    }
    return new OneTimePadKeyGenerator(finiteByteArraySet);
  }

}
