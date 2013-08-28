package ch.bfh.unicrypt.crypto.signature.interfaces;

import ch.bfh.unicrypt.crypto.keygen.interfaces.KeyPairGenerator;
import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.additive.classes.ZPlus;
import ch.bfh.unicrypt.math.general.interfaces.Group;

public interface SignatureScheme {

  public Element sign(final Element privateKey, final Element message);

  public boolean verify(final Element publicKey, final Element message, Element signature);

  public KeyPairGenerator getKeyPairGenerator();

  public Function getSignatureFunction();

  public Function getVerificationFunction();

  public ZPlus getMessageSpace();

  public Group getSignatureSpace();

}
