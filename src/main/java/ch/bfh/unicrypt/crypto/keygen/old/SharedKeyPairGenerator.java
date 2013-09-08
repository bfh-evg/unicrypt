/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.keygen.old;

import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;

/**
 *
 * @author rolfhaenni
 */
public interface SharedKeyPairGenerator extends PublicKeyGenerator, SharedSecretKeyGenerator {

  public Tuple combineKeyPairs(final Tuple... keyPairs);

  public Element combinePrivateKeys(final Element... publicKeys);

  public Element combinePublicKeys(final Element... publicKeys);

  @Override
  public Tuple combineKeys(final Element... keys);

}
