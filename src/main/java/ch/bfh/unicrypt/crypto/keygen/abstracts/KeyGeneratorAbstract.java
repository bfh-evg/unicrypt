package ch.bfh.unicrypt.crypto.keygen.abstracts;

import ch.bfh.unicrypt.crypto.keygen.interfaces.KeyGenerator;
import ch.bfh.unicrypt.math.group.interfaces.Group;

public abstract class KeyGeneratorAbstract implements KeyGenerator {

  private final Group keySpace;

  public KeyGeneratorAbstract(final Group keySpace) {
    if (keySpace == null) {
      throw new IllegalArgumentException();
    }
    this.keySpace = keySpace;
  }

 
  @Override
  public Group getKeySpace() {
    return this.keySpace;
  }

}
