/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.dualistic.abstracts;

import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.FiniteField;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeCyclicGroup;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public abstract class AbstractFiniteField<E extends DualisticElement, M extends MultiplicativeCyclicGroup> extends AbstractField<E, M> implements FiniteField {

  private BigInteger characteristic;

  @Override
  public final BigInteger getCharacteristic() {
    return this.characteristic;
  }

}
