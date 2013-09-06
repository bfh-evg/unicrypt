package ch.bfh.unicrypt.crypto.keygen.interfaces;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;

public interface SharedSecretKeyGenerator extends KeyGenerator {

  public Element generateKey(final Element... sharedKeys);

}
