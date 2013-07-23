package ch.bfh.unicrypt.crypto.keygen.interfaces;

import ch.bfh.unicrypt.math.element.Element;

public interface DDHGroupDistributedKeyPairGenerator extends DDHGroupKeyPairGenerator, DistributedKeyPairGenerator {

  @Override
  public Element combinePublicKeys(Element... publicKeys);

}
