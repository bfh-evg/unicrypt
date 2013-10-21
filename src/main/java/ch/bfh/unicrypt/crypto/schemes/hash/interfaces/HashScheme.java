package ch.bfh.unicrypt.crypto.schemes.hash.interfaces;

import ch.bfh.unicrypt.crypto.schemes.Scheme;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.classes.HashFunction;

public interface HashScheme extends Scheme {

  public Set getHashSpace();

  public HashFunction getHashFunction();

  public Element hash(Element element);

}
