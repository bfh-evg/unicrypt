package ch.bfh.unicrypt.crypto.schemes.encryption.interfaces;

import ch.bfh.unicrypt.crypto.schemes.Scheme;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public interface EncryptionScheme extends Scheme {

  public Set getCiphertextSpace();

  public Function getEncryptionFunction();

  public Function getDecryptionFunction();

  public Element encrypt(Element key, Element plaintext);

  public Element decrypt(Element key, Element ciphertext);

}
