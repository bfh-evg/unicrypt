package ch.bfh.unicrypt.crypto.encryption.interfaces;

import ch.bfh.unicrypt.crypto.keygen.interfaces.KeyPairGenerator;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;

public interface AsymmetricEncryptionScheme extends EncryptionScheme {

  public KeyPairGenerator getKeyPairGenerator();

}
