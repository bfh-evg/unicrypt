package ch.bfh.unicrypt.crypto.keygen.interfaces;

import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;

import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import java.math.BigInteger;

public interface KeyPairGenerator extends KeyGenerator {

  public Set getPublicKeySpace();

  public Set getPrivateKeySpace();

  public Function getPrivateKeyGenerationFunction();

  public Function getPublicKeyGenerationFunction();

  public Tuple getKeyPair(BigInteger value);

  public Tuple getKeyPair(BigInteger value1, BigInteger value2);

  public Element getPrivateKey(BigInteger value);

  public Element getPrivateKey(Tuple keyPair);

  public Element getPublicKey(BigInteger value);

  public Element getPublicKey(Tuple keyPair);

  public Element getPublicKey(Element privateKey);

  @Override
  public ProductSet getKeySpace();

  @Override
  public Tuple getKey(BigInteger value);

}
