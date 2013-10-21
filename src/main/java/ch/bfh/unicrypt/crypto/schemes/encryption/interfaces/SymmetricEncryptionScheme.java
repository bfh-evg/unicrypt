/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.schemes.encryption.interfaces;

import ch.bfh.unicrypt.crypto.keygenerator.interfaces.KeyGenerator;

/**
 *
 * @author rolfhaenni
 */
public interface SymmetricEncryptionScheme extends EncryptionScheme {

  public KeyGenerator getKeyGenerator();

}
