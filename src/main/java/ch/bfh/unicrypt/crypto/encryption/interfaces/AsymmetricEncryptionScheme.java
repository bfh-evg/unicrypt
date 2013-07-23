package ch.bfh.unicrypt.crypto.encryption.interfaces;

import ch.bfh.unicrypt.crypto.keygen.interfaces.KeyPairGenerator;

public interface AsymmetricEncryptionScheme extends EncryptionScheme {

  @Override
  public KeyPairGenerator getKeyGenerator();

}
