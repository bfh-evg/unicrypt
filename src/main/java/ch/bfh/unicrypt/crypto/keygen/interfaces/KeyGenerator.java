package ch.bfh.unicrypt.crypto.keygen.interfaces;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import java.math.BigInteger;

public interface KeyGenerator {

  public Set getKeySpace();

  public Element getKey(BigInteger value);

  public Function getKeyGenerationFunction();

}
