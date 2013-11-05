package ch.bfh.unicrypt.crypto.schemes;

import ch.bfh.unicrypt.crypto.encoder.interfaces.Encoder;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.helper.UniCrypt;

/**
 *
 * @author rolfhaenni
 * @param <M>
 */
public abstract class AbstractScheme<M extends Set>
       extends UniCrypt
       implements Scheme {

  private final M messageSpace;
  private final Encoder encoder;

  public AbstractScheme(M messageSpace, Encoder encoder) {
    this.messageSpace = messageSpace;
    this.encoder = encoder;
  }

  @Override
  public M getMessageSpace() {
    return this.messageSpace;
  }

  @Override
  public Encoder getEncoder() {
    return this.encoder;
  }

  protected Element encodeMessage(Element message) {
    return this.getEncoder().encode(this.getEncoder().getDomain().getElement(message));
  }

  protected Element decodeMessage(Element element) {
    return this.getMessageSpace().getElement(this.getEncoder().decode(element));
  }

}
