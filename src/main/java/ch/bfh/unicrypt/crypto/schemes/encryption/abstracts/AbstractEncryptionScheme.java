package ch.bfh.unicrypt.crypto.schemes.encryption.abstracts;

import ch.bfh.unicrypt.crypto.encoder.interfaces.Encoder;
import ch.bfh.unicrypt.crypto.schemes.AbstractScheme;
import ch.bfh.unicrypt.crypto.schemes.encryption.interfaces.EncryptionScheme;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public abstract class AbstractEncryptionScheme<M extends Set, P extends Set, C extends Set, ME extends Element, CE extends Element>
       extends AbstractScheme<M>
       implements EncryptionScheme {

  private final Function encryptionFunction;
  private final Function decryptionFunction;

  protected AbstractEncryptionScheme(M messageSpace, Encoder encoder, Function encryptionFunction, Function decryptionFunction) {
    super(messageSpace, encoder);
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
  public CE encrypt(Element encryptionKey, Element message) {
    if (!this.getEncryptionKeySpace().contains(encryptionKey) || !this.getMessageSpace().contains(message)) {
      throw new IllegalArgumentException();
    }
    return (CE) this.getEncryptionFunction().apply(encryptionKey, this.encodeMessage(message));
  }

  @Override
  public final ME decrypt(Element decryptionKey, Element ciphertext) {
    if (!this.getDecryptionKeySpace().contains(decryptionKey) || !this.getCiphertextSpace().contains(ciphertext)) {
      throw new IllegalArgumentException();
    }
    return (ME) this.decodeMessage(this.getDecryptionFunction().apply(decryptionKey, ciphertext));
  }

}
