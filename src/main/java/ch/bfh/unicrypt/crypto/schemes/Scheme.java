package ch.bfh.unicrypt.crypto.schemes;

import ch.bfh.unicrypt.crypto.encoder.interfaces.Encoder;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;

/**
 *
 * @author rolfhaenni
 */
public interface Scheme {

  /**
   *
   * @return
   */
  public Set getMessageSpace();

  /**
   *
   * @return
   */
  public Encoder getEncoder();

}
