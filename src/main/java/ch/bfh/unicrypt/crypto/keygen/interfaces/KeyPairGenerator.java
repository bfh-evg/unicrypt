package ch.bfh.unicrypt.crypto.keygen.interfaces;

import java.util.Random;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;

public interface KeyPairGenerator extends RandomizedKeyGenerator {

  public Element generateKeyPair();

  public Element generateKeyPair(Random random);

  public Group getPublicKeySpace();

  public Group getPrivateKeySpace();

  public Element getPublicKey(Element keyPair);

  public Element getPrivateKey(Element keyPair);

  public Function getPrivateKeyGeneratorFunction();

  public Function getPublicKeyGeneratorFunction();

}
