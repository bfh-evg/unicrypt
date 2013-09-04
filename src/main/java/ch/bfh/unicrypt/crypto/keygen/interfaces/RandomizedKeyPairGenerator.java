/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.keygen.interfaces;

import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import java.util.Random;

/**
 *
 * @author rolfhaenni
 */
public interface RandomizedKeyPairGenerator extends KeyPairGenerator, RandomizedKeyGenerator {

  public Tuple generateKeyPair();

  public Tuple generateKeyPair(Random random);

  @Override
  public Tuple generateKey();

  @Override
  public Tuple generateKey(Random random);

}
