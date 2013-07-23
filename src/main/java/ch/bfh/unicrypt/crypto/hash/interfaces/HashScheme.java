package ch.bfh.unicrypt.crypto.hash.interfaces;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.function.classes.HashFunction;

public interface HashScheme {

  public Element hash(Element... elements);

  public HashFunction getHashFunction();

}
