package ch.bfh.unicrypt.crypto.keygen.classes;

import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import java.math.BigInteger;
import java.util.Random;

public class KeyPairGenerator {

  private Function publicKeyFunction;
  private ProductSet keyPairSpace;
  private KeyGenerator privateKeyGenerator;

  public KeyPairGenerator(Function publicKeyFunction) {
    this.publicKeyFunction = publicKeyFunction;
  }

  public ProductSet getKeyPairSpace() {
    if (this.keyPairSpace == null) {
      this.keyPairSpace = ProductSet.getInstance(this.getPrivateKeySpace(), this.getPublicKeySpace());
    }
    return this.keyPairSpace;
  }

  public Tuple getKeyPair(BigInteger value) {
    Element privateKey = this.getPrivateKey(value);
    return this.getKeyPairSpace().getElement(privateKey, this.getPublicKey(privateKey));
  }

  public Tuple generateKeyPair() {
    return this.generateKeyPair(null);
  }

  public Tuple generateKeyPair(Random random) {
    Element privateKey = this.generatePrivateKey(random);
    return this.getKeyPairSpace().getElement(privateKey, this.getPublicKey(privateKey));
  }

  public Set getPrivateKeySpace() {
    return this.getPublicKeyFunction().getDomain();
  }

  public KeyGenerator getPrivateKeyGenerator() {
    if (this.privateKeyGenerator == null) {
      this.privateKeyGenerator = KeyGenerator.getInstance(this.getPrivateKeySpace());
    }
    return this.privateKeyGenerator;
  }

  public Element getPrivateKey(BigInteger value) {
    return this.getPrivateKeyGenerator().getKey(value);
  }

  public Element generatePrivateKey() {
    return this.generatePrivateKey(null);
  }

  public Element generatePrivateKey(Random random) {
    return this.getPrivateKeyGenerator().generateKey(random);
  }

  public Set getPublicKeySpace() {
    return this.getPublicKeyFunction().getCoDomain();
  }

  public Function getPublicKeyFunction() {
    return this.publicKeyFunction;
  }

  public Element getPublicKey(BigInteger value) {
    return this.getPublicKey(this.getPrivateKey(value));
  }

  public Element getPublicKey(Element privateKey) {
    return this.getPublicKeyFunction().apply(privateKey);
  }

}
