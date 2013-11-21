package ch.bfh.unicrypt.crypto.schemes.signature.abstracts;

public abstract class AbstractRandomizedSignatureScheme {
//import java.util.Random;
//
//import ch.bfh.unicrypt.crypto.schemes.signature.interfaces.RandomizedSignatureScheme;
//
//public abstract class AbstractRandomizedSignatureScheme extends AbstractSignatureScheme implements RandomizedSignatureScheme {
//
//  @Override
//  public Element sign(final Element privateKey, final Element message) {
//    return this.sign(privateKey, message, (Random) null);
//  }
//
//  @Override
//  public Element sign(final Element privateKey, final Element message, final Random random) {
//    return this.sign(privateKey, message, this.getRandomizationSpace().getRandomElement(random));
//  }
//
//  @Override
//  public Element sign(final Element privateKey, final Element message, final Element randomization) {
//    return this.getSignatureFunction().apply(privateKey, message, randomization);
//  }
//
}
