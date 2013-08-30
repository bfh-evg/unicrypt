package ch.bfh.unicrypt.crypto.keygen.abstracts;

import java.util.Random;

import ch.bfh.unicrypt.crypto.keygen.interfaces.RandomizedKeyGenerator;
import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;

public abstract class RandomizedKeyGeneratorAbstract extends KeyGeneratorAbstract implements RandomizedKeyGenerator {  

  public RandomizedKeyGeneratorAbstract(final Group keySpace) {
    super(keySpace);
  }

  @Override
  public Element generateKey() {
    return this.generateKey(null);
  }

  @Override
  public Element generateKey(final Random random) {
    return this.getKeyGenerationFunction().apply(new Element[0],random);
  }
}
