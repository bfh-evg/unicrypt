package ch.bfh.unicrypt.crypto.encryption.interfaces;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import java.util.Random;

public interface HomomorphicEncryptionScheme extends RandomizedEncryptionScheme {

  public Function getIdentityEncryptionFunction();

  public Function getReEncryptionFunction();

  public Element reEncrypt(final Element publicKey, final Element ciphertext);

  public Element reEncrypt(final Element publicKey, final Element ciphertext, Random random);

  public Element reEncrypt(final Element publicKey, final Element ciphertext, final Element randomization);

}
