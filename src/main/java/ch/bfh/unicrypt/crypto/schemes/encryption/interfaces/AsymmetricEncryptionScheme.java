package ch.bfh.unicrypt.crypto.schemes.encryption.interfaces;

import ch.bfh.unicrypt.crypto.keygenerator.interfaces.KeyPairGenerator;

public interface AsymmetricEncryptionScheme extends EncryptionScheme {

  public KeyPairGenerator getKeyPairGenerator();

}
