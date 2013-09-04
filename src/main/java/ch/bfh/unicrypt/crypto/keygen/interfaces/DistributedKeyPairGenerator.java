package ch.bfh.unicrypt.crypto.keygen.interfaces;

import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;

public interface DistributedKeyPairGenerator extends KeyPairGenerator, DistributedKeyGenerator {

  public Tuple combineKeyPairs(final Tuple... keyPairs);

  public Element combinePrivateKeys(final Element... publicKeys);

  public Element combinePublicKeys(final Element... publicKeys);

  @Override
  public Tuple combineKeys(final Element... keys);

}
