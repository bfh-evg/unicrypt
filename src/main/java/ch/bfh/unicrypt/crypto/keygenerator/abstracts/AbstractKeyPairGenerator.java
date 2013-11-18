package ch.bfh.unicrypt.crypto.keygenerator.abstracts;

import ch.bfh.unicrypt.crypto.keygenerator.interfaces.KeyGenerator;
import ch.bfh.unicrypt.crypto.keygenerator.interfaces.KeyPairGenerator;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.helper.UniCrypt;
import java.math.BigInteger;
import java.util.Random;

public abstract class AbstractKeyPairGenerator<PRS extends Set, PUS extends Set, PRE extends Element, PUE extends Element>
       extends UniCrypt
       implements KeyPairGenerator {

  private final Function publicKeyFunction;
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
  public PRS getPrivateKeySpace() {
    return (PRS) this.getPublicKeyFunction().getDomain();
  }

  @Override
  public KeyGenerator getPrivateKeyGenerator() {
    if (this.privateKeyGenerator == null) {
      this.privateKeyGenerator = new AbstractKeyGenerator<PRS, PRE>(this.getPrivateKeySpace()) {
      };
    }
    return this.privateKeyGenerator;
  }

  @Override
  public PRE getPrivateKey(BigInteger value) {
    return (PRE) this.getPrivateKeyGenerator().getKey(value);
  }

  @Override
  public PRE generatePrivateKey() {
    return this.generatePrivateKey(null);
  }

  @Override
  public PRE generatePrivateKey(Random random) {
    return (PRE) this.getPrivateKeyGenerator().generateKey(random);
  }

  @Override
  public PUS getPublicKeySpace() {
    return (PUS) this.getPublicKeyFunction().getCoDomain();
  }

  @Override
  public Function getPublicKeyFunction() {
    return this.publicKeyFunction;
  }

  @Override
  public PUE getPublicKey(BigInteger value) {
    return (PUE) this.getPublicKey(this.getPrivateKey(value));
  }

  @Override
  public PUE getPublicKey(Element privateKey) {
    return (PUE) this.getPublicKeyFunction().apply(privateKey);
  }

  @Override
  protected String standardToStringContent() {
    return this.getPrivateKeySpace().toString() + ", " + this.getPublicKeySpace().toString();
  }

}
