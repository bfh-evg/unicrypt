package ch.bfh.unicrypt.crypto.encryption.interfaces;

import ch.bfh.unicrypt.crypto.keygen.interfaces.KeyGenerator;
import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.general.interfaces.Group;

public interface EncryptionScheme {

  public Element encrypt(Element key, Element plaintext);

  public Element decrypt(Element key, Element ciphertext);

  public Function getDecryptionFunction();

  public Function getEncryptionFunction();

  public Function getIdentityEncryptionFunction();

  public KeyGenerator getKeyGenerator();

  public Group getPlaintextSpace();

  public Group getCiphertextSpace();

}
