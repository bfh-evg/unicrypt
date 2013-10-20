package ch.bfh.unicrypt.crypto.signature.abstracts;

import ch.bfh.unicrypt.crypto.signature.interfaces.SignatureScheme;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;

public abstract class AbstractSignatureScheme<M extends Set, S extends Set, E extends Element> implements SignatureScheme {

  @Override
  public E sign(final Element privateKey, final Element message) {
    return (E) this.getSignatureFunction().apply(privateKey, message);
  }

  @Override
  public BooleanElement verify(final Element publicKey, final Element message, final Element signature) {
    return this.getVerificationFunction().apply(publicKey, message, signature).equals(BooleanGroup.TRUE);
  }

  @Override
  public M getMessageSpace() {
    return ZPlus.Factory.getInstance();
  }

  @Override
  public S getSignatureSpace() {
    return (S) this.getSignatureFunction().getCoDomain();
  }

}
