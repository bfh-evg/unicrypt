package ch.bfh.unicrypt.crypto.keygen.interfaces;

import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.group.interfaces.Group;

public interface KeyGenerator {

  public Function getKeyGenerationFunction();

  public Group getKeySpace();

}
