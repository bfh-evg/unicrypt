package ch.bfh.unicrypt.crypto.hash.interfaces;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.function.classes.HashFunction;

public interface HashScheme {

  public Element hash(Element... elements);

  public HashFunction getHashFunction();

}
