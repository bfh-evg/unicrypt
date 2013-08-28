/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.element.interfaces;

import ch.bfh.unicrypt.math.utility.Permutation;

/**
 *
 * @author rolfhaenni
 */
public interface PermutationElement extends Element {

  /**
   * Returns the corresponding permutation
   *
   * @return The permutation
   */
  public Permutation getPermutation();

}
