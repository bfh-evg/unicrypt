package ch.bfh.unicrypt.crypto.keygen.interfaces;

import ch.bfh.unicrypt.math.element.Element;

public interface DistributedKeyPairGenerator extends KeyPairGenerator {

  public Element combinePublicKeys(final Element... publicKeys);

}
