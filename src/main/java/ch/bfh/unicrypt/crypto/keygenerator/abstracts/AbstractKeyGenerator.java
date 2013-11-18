package ch.bfh.unicrypt.crypto.keygenerator.abstracts;

import ch.bfh.unicrypt.crypto.keygenerator.interfaces.KeyGenerator;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.helper.UniCrypt;
import java.math.BigInteger;
import java.util.Random;

public abstract class AbstractKeyGenerator<KS extends Set, KE extends Element>
       extends UniCrypt
       implements KeyGenerator {

  private final KS keySpace;

  protected AbstractKeyGenerator(final KS keySpace) {
    this.keySpace = keySpace;
  }

  @Override
  public final KS getKeySpace() {
    return this.keySpace;
  }

  @Override
  public final KE getKey(BigInteger value) {
    return (KE) this.getKeySpace().getElement(value);
  }

  @Override
  public KE generateKey() {
    return (KE) this.getKeySpace().getRandomElement();
  }

  @Override
  public KE generateKey(Random random) {
    return (KE) this.getKeySpace().getRandomElement(random);
  }

  @Override
  protected String standardToStringContent() {
    return this.getKeySpace().toString();
  }

}
