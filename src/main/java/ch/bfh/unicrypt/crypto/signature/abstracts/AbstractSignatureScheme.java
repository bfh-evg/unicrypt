package ch.bfh.unicrypt.crypto.signature.abstracts;

import ch.bfh.unicrypt.crypto.signature.interfaces.SignatureScheme;
import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.cyclicgroup.classes.BooleanGroup;
import ch.bfh.unicrypt.math.algebra.additive.classes.ZPlus;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;

public abstract class AbstractSignatureScheme implements SignatureScheme {

  @Override
  public Element sign(final Element privateKey, final Element message) {
    return this.getSignatureFunction().apply(privateKey, message);
  }

  @Override
  public boolean verify(final Element publicKey, final Element message, final Element signature) {
    return this.getVerificationFunction().apply(publicKey, message, signature).equals(BooleanGroup.TRUE);
  }

  @Override
  public ZPlus getMessageSpace() {
    return ZPlus.Factory.getInstance();
  }

  @Override
  public Group getSignatureSpace() {
    return this.getSignatureFunction().getCoDomain();
  }

}
