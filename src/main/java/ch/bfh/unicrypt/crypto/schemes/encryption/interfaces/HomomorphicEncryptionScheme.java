package ch.bfh.unicrypt.crypto.schemes.encryption.interfaces;

import java.util.Random;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public interface HomomorphicEncryptionScheme extends RandomizedEncryptionScheme {

  public Function getIdentityEncryptionFunction();

  public Function getReEncryptionFunction();

  public Element reEncrypt(final Element publicKey, final Element ciphertext);

  public Element reEncrypt(final Element publicKey, final Element ciphertext, Random random);

  public Element reEncrypt(final Element publicKey, final Element ciphertext, final Element randomization);

}
