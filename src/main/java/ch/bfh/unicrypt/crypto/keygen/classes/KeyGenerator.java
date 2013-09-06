package ch.bfh.unicrypt.crypto.keygen.classes;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import java.math.BigInteger;
import java.util.Random;

public class KeyGenerator {

  private final Set keySpace;

  protected KeyGenerator(final Set keySpace) {
    this.keySpace = keySpace;
  }

  public final Set getKeySpace() {
    return this.keySpace;
  }

  public final Element getKey(BigInteger value) {
    return this.getKeySpace().getElement(value);
  }

  public Element generateKey() {
    return this.getKeySpace().getRandomElement();
  }

  public Element generateKey(Random random) {
    return this.getKeySpace().getRandomElement(random);
  }

  public static KeyGenerator getInstance(Set keySpace) {
    if (keySpace == null) {
      throw new IllegalArgumentException();
    }
    return new KeyGenerator(keySpace);
  }

}
