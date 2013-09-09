/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.encryption.interfaces;

import ch.bfh.unicrypt.crypto.keygen.interfaces.KeyGenerator;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;

/**
 *
 * @author rolfhaenni
 */
public interface SymmetricEncryptionScheme extends EncryptionScheme {

  public KeyGenerator getKeyGenerator();

}
