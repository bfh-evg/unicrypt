package ch.bfh.unicrypt.crypto.schemes.scheme.abstracts;

import ch.bfh.unicrypt.crypto.schemes.scheme.interfaces.Scheme;
import ch.bfh.unicrypt.math.helper.UniCrypt;

/**
 *
 * @author rolfhaenni
 */
public abstract class AbstractScheme
       extends UniCrypt
       implements Scheme {

  @Override
  protected String standardToStringContent() {
    return this.getMessageSpace().toString();
  }

}
