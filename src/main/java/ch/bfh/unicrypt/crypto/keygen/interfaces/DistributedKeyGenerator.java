package ch.bfh.unicrypt.crypto.keygen.interfaces;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;

public interface DistributedKeyGenerator extends KeyGenerator {

  public Element combineKeys(final Element... keys);

}
