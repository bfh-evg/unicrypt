/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.dualistic.interfaces;

import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeCyclicGroup;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public interface FiniteField extends Field {

  public BigInteger getCharacteristic();

}
