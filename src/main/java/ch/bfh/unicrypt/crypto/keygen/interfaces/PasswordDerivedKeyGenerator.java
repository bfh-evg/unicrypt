/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.keygen.interfaces;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;

/**
 *
 * @author rolfhaenni
 */
public interface PasswordDerivedKeyGenerator extends KeyGenerator {

  public Element generateKey(String password);

  public Element generateKey(String password, byte[] salt);

}
