package ch.bfh.unicrypt.crypto.keygenerator.old;

import ch.bfh.unicrypt.crypto.keygenerator.interfaces.KeyGenerator;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;

public interface SharedSecretKeyGenerator extends KeyGenerator {

  public Element generateKey(final Element... sharedKeys);

}
