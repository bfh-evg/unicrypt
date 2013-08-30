/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.general.interfaces;

import ch.bfh.unicrypt.math.helper.Permutation;

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
