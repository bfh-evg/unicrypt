package ch.bfh.unicrypt.crypto.keygen.abstracts;

import java.math.BigInteger;
import java.util.Random;

import ch.bfh.unicrypt.crypto.keygen.interfaces.KeyGenerator;
import ch.bfh.unicrypt.crypto.keygen.interfaces.KeyPairGenerator;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.helper.UniCrypt;

public abstract class AbstractKeyPairGenerator<S extends Set, T extends Set, E extends Element, F extends Element> extends UniCrypt implements KeyPairGenerator {

  private Function publicKeyFunction;
  private ProductSet keyPairSpace;
  private AbstractKeyGenerator privateKeyGenerator;

  protected AbstractKeyPairGenerator(Function publicKeyFunction) {
    this.publicKeyFunction = publicKeyFunction;
  }

  @Override
  public ProductSet getKeyPairSpace() {
    if (this.keyPairSpace == null) {
      this.keyPairSpace = ProductSet.getInstance(this.getPrivateKeySpace(), this.getPublicKeySpace());
    }
    return this.keyPairSpace;
  }

  @Override
  public Tuple getKeyPair(BigInteger value) {
    Element privateKey = this.getPrivateKey(value);
    return this.getKeyPairSpace().getElement(privateKey, this.getPublicKey(privateKey));
  }

  @Override
  public Tuple generateKeyPair() {
    return this.generateKeyPair(null);
  }

  @Override
  public Tuple generateKeyPair(Random random) {
    Element privateKey = this.generatePrivateKey(random);
    return this.getKeyPairSpace().getElement(privateKey, this.getPublicKey(privateKey));
  }

  @Override
  public S getPrivateKeySpace() {
    return (S) this.getPublicKeyFunction().getDomain();
  }

  @Override
  public KeyGenerator getPrivateKeyGenerator() {
    if (this.privateKeyGenerator == null) {
      this.privateKeyGenerator = new AbstractKeyGenerator<S, E>(this.getPrivateKeySpace()) {
      };
    }
    return this.privateKeyGenerator;
  }

  @Override
  public E getPrivateKey(BigInteger value) {
    return (E) this.getPrivateKeyGenerator().getKey(value);
  }

  @Override
  public E generatePrivateKey() {
    return this.generatePrivateKey(null);
  }

  @Override
  public E generatePrivateKey(Random random) {
    return (E) this.getPrivateKeyGenerator().generateKey(random);
  }

  @Override
  public T getPublicKeySpace() {
    return (T) this.getPublicKeyFunction().getCoDomain();
  }

  @Override
  public Function getPublicKeyFunction() {
    return this.publicKeyFunction;
  }

  @Override
  public F getPublicKey(BigInteger value) {
    return (F) this.getPublicKey(this.getPrivateKey(value));
  }

  @Override
  public F getPublicKey(Element privateKey) {
    return (F) this.getPublicKeyFunction().apply(privateKey);
  }

  @Override
  protected String standardToStringContent() {
    return this.getPrivateKeySpace().toString() + ", " + this.getPublicKeySpace().toString();
  }

}
