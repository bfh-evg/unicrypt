package ch.bfh.unicrypt.crypto.encryption.abstracts;

import ch.bfh.unicrypt.crypto.encryption.interfaces.EncryptionScheme;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.helper.UniCrypt;

public abstract class AbstractEncryptionScheme<P extends Set, C extends Set, PE extends Element, CE extends Element> extends UniCrypt implements EncryptionScheme {

  private Function encryptionFunction;
  private Function decryptionFunction;

  protected AbstractEncryptionScheme(Function encryptionFunction, Function decryptionFunction) {
    this.encryptionFunction = encryptionFunction;
    this.decryptionFunction = decryptionFunction;
  }

  @Override
  public final P getPlaintextSpace() {
    return (P) ((ProductSet) this.getEncryptionFunction().getDomain()).getAt(1);
  }

  @Override
  public final C getCiphertextSpace() {
    return (C) ((ProductSet) this.getDecryptionFunction().getDomain()).getAt(1);
  }

  @Override
  public final Function getEncryptionFunction() {
    return this.encryptionFunction;
  }

  @Override
  public final Function getDecryptionFunction() {
    return this.decryptionFunction;
  }

  @Override
  public CE encrypt(Element key, Element plaintext) {
    return (CE) this.getEncryptionFunction().apply(key, plaintext);
  }

  @Override
  public final PE decrypt(Element key, Element ciphertext) {
    return (PE) this.getDecryptionFunction().apply(key, ciphertext);
  }

}
