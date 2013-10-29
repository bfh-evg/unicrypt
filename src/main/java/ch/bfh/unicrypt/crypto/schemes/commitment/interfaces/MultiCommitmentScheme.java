/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.schemes.commitment.interfaces;

import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;

/**
 *
 * @author rolfhaenni
 */
public interface MultiCommitmentScheme extends CommitmentScheme {

  public int getArity();

  @Override
  public ProductSet getMessageSpace();

}
