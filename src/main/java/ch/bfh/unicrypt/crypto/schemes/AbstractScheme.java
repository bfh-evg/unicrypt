/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.schemes;

import ch.bfh.unicrypt.crypto.encoder.interfaces.Encoder;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;

/**
 *
 * @author rolfhaenni
 */
public abstract class AbstractScheme<M extends Set> implements Scheme {

  private Encoder encoder;

  public AbstractScheme(Encoder encoder) {
    this.encoder = encoder;
  }

  @Override
  public Encoder getEncoder() {
    return this.encoder;
  }

  @Override
  public M getMessageSpace() {
    return (M) this.getEncoder().getMessageSpace();
  }

}
