package ch.bfh.unicrypt.crypto.encryption.interfaces;

import java.util.Random;

import ch.bfh.unicrypt.math.element.Element;

public interface RandomizedAsymmetricHomomorphicEncryptionScheme extends RandomizedAsymmetricEncryptionScheme, RandomizedHomomorphicEncryptionScheme {

  public Element reEncrypt(final Element publicKey, final Element ciphertext);

  public Element reEncrypt(final Element publicKey, final Element ciphertext, final Element randomization);

  public Element reEncrypt(final Element publicKey, final Element ciphertext, Random random);

}
