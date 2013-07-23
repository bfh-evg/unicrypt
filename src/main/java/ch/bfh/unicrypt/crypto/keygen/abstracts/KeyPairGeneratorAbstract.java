package ch.bfh.unicrypt.crypto.keygen.abstracts;

import java.util.Random;

import ch.bfh.unicrypt.crypto.keygen.interfaces.KeyPairGenerator;
import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.group.classes.ProductGroup;
import ch.bfh.unicrypt.math.group.interfaces.Group;

public abstract class KeyPairGeneratorAbstract extends RandomizedKeyGeneratorAbstract implements KeyPairGenerator {

  public KeyPairGeneratorAbstract(final ProductGroup keySpace) {
    super(keySpace);
    if (keySpace.getArity() != 2) {
      throw new IllegalArgumentException();
    }
  }

  @Override
  public Element generateKeyPair() {
    return this.generateKey();
  }

  @Override
  public Element generateKeyPair(final Random random) {
    return this.generateKey(random);
  }

  @Override
  public Group getPrivateKeySpace() {
    return this.getKeySpace().getGroupAt(0);
  }

  @Override
  public Group getPublicKeySpace() {
    return this.getKeySpace().getGroupAt(1);
  }

  @Override
  public Element getPrivateKey(final Element keyPair) {
    return keyPair.getElementAt(0);
  }

  @Override
  public Element getPublicKey(final Element keyPair) {
    return keyPair.getElementAt(1);
  }

}
