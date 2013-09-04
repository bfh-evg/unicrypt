package ch.bfh.unicrypt.crypto.keygen.interfaces;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import java.util.Random;

public interface RandomizedKeyGenerator extends KeyGenerator {

  public Element generateKey();

  public Element generateKey(Random random);

}
