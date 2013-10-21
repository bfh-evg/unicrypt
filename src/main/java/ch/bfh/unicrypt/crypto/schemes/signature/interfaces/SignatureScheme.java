package ch.bfh.unicrypt.crypto.schemes.signature.interfaces;

import ch.bfh.unicrypt.crypto.keygenerator.interfaces.KeyPairGenerator;
import ch.bfh.unicrypt.crypto.schemes.Scheme;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public interface SignatureScheme extends Scheme {

  public KeyPairGenerator getKeyPairGenerator();

  public Set getSignatureSpace();

  public Function getSignatureFunction();

  public Function getVerificationFunction();

  public Element sign(final Element privateKey, final Element message);

  public BooleanElement verify(final Element publicKey, final Element message, Element signature);

}
