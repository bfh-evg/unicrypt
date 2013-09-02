/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.general.classes;

import ch.bfh.unicrypt.math.algebra.general.abstracts.AbstractElement;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public class SingletonElement extends AbstractElement<SingletonGroup, SingletonElement> {

  protected SingletonElement(final SingletonGroup group, BigInteger value) {
    super(group, value);
  }

}
