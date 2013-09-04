/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.keygen.interfaces;

import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;

/**
 *
 * @author rolfhaenni
 */
public interface PasswordKeyPairGenerator extends KeyPairGenerator, PasswordKeyGenerator {

  public Tuple generateKeyPair(String password);

  public Tuple generateKeyPair(String password, byte[] salt);

  @Override
  public Tuple generateKey(String password);

  @Override
  public Tuple generateKey(String password, byte[] salt);

}
